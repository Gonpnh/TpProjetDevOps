package ytg.projetjavaytg.Services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ytg.projetjavaytg.Models.Apprenti;
import ytg.projetjavaytg.Models.Entreprise;
import ytg.projetjavaytg.Models.MaitreApprentissage;
import ytg.projetjavaytg.Models.Utilisateur;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExcelImportService {

    private final ApprentiService apprentiService;
    private final EntrepriseService entrepriseService;
    private final MaitreApprentissageService maitreApprentissageService;
    private final UtilisateurService utilisateurService;
    private final AnneeAcademiqueService anneeAcademiqueService;

    public ExcelImportService(ApprentiService apprentiService,
                             EntrepriseService entrepriseService,
                             MaitreApprentissageService maitreApprentissageService,
                             UtilisateurService utilisateurService,
                             AnneeAcademiqueService anneeAcademiqueService) {
        this.apprentiService = apprentiService;
        this.entrepriseService = entrepriseService;
        this.maitreApprentissageService = maitreApprentissageService;
        this.utilisateurService = utilisateurService;
        this.anneeAcademiqueService = anneeAcademiqueService;
    }

    @Transactional
    public ImportResult importApprentisFromExcel(MultipartFile file) throws IOException {
        System.out.println("=== DEBUT SERVICE IMPORT ===");
        List<String> errors = new ArrayList<>();
        List<Apprenti> createdApprentis = new ArrayList<>();
        int totalRows = 0;
        int successfulImports = 0;

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            System.out.println("Workbook créé, nombre de feuilles: " + workbook.getNumberOfSheets());
            Sheet sheet = workbook.getSheetAt(0);
            System.out.println("Feuille sélectionnée, nombre de lignes: " + sheet.getLastRowNum());

            // Lire les en-têtes (première ligne)
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("Erreur: Pas de ligne d'en-têtes");
                throw new IllegalArgumentException("Le fichier Excel doit contenir une ligne d'en-têtes");
            }

            System.out.println("Ligne d'en-têtes trouvée, nombre de colonnes: " + headerRow.getLastCellNum());
            Map<String, Integer> columnMapping = createColumnMapping(headerRow);
            System.out.println("Mapping des colonnes: " + columnMapping);

            // Valider que les colonnes obligatoires sont présentes
            validateRequiredColumns(columnMapping, errors);

            if (!errors.isEmpty()) {
                System.out.println("Erreurs de validation: " + errors);
                return new ImportResult(createdApprentis, errors, totalRows, successfulImports);
            }

            // Traiter chaque ligne de données
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                totalRows++;
                System.out.println("Traitement ligne " + (i + 1));

                try {
                    Apprenti apprenti = processRow(row, columnMapping);
                    if (apprenti != null) {
                        Apprenti created = apprentiService.createApprenti(apprenti);
                        createdApprentis.add(created);
                        successfulImports++;
                        System.out.println("Apprenti créé: " + apprenti.getNom() + " " + apprenti.getPrenom());
                    }
                } catch (Exception e) {
                    String errorMsg = "Ligne " + (i + 1) + ": " + e.getMessage();
                    System.out.println("Erreur: " + errorMsg);
                    errors.add(errorMsg);
                }
            }
        }

        System.out.println("=== FIN SERVICE IMPORT ===");
        System.out.println("Total lignes: " + totalRows + ", Succès: " + successfulImports + ", Erreurs: " + errors.size());
        return new ImportResult(createdApprentis, errors, totalRows, successfulImports);
    }

    private Map<String, Integer> createColumnMapping(Row headerRow) {
        Map<String, Integer> mapping = new HashMap<>();

        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null) {
                String header = cell.getStringCellValue().trim().toLowerCase();
                mapping.put(header, i);
            }
        }

        return mapping;
    }

    private void validateRequiredColumns(Map<String, Integer> columnMapping, List<String> errors) {
        List<String> requiredColumns = List.of("nom", "prenom", "email", "entreprise", "maitre_apprentissage", "tuteur_enseignant");

        for (String required : requiredColumns) {
            if (!columnMapping.containsKey(required)) {
                errors.add("Colonne obligatoire manquante: " + required);
            }
        }
    }

    private Apprenti processRow(Row row, Map<String, Integer> columnMapping) throws Exception {
        Apprenti apprenti = new Apprenti();

        // Champs obligatoires
        apprenti.setNom(getCellStringValue(row, columnMapping.get("nom")));
        apprenti.setPrenom(getCellStringValue(row, columnMapping.get("prenom")));
        apprenti.setEmail(getCellStringValue(row, columnMapping.get("email")));

        // Vérifier si l'email existe déjà
        if (apprentiService.getAllApprentis().stream()
                .anyMatch(a -> a.getEmail().equals(apprenti.getEmail()))) {
            throw new Exception("Un apprenti avec cet email existe déjà: " + apprenti.getEmail());
        }

        // Champs optionnels
        if (columnMapping.containsKey("telephone")) {
            apprenti.setTelephone(getCellStringValue(row, columnMapping.get("telephone")));
        }

        if (columnMapping.containsKey("programme")) {
            apprenti.setProgramme(getCellStringValue(row, columnMapping.get("programme")));
        }

        if (columnMapping.containsKey("majeure")) {
            apprenti.setMajeure(getCellStringValue(row, columnMapping.get("majeure")));
        }

        if (columnMapping.containsKey("niveau")) {
            String niveau = getCellStringValue(row, columnMapping.get("niveau"));
            apprenti.setNiveau(niveau != null && !niveau.isEmpty() ? niveau : "I1");
        } else {
            apprenti.setNiveau("I1");
        }

        // Année académique
        if (columnMapping.containsKey("annee_academique")) {
            String annee = getCellStringValue(row, columnMapping.get("annee_academique"));
            apprenti.setAnneeAcademique(annee != null && !annee.isEmpty() ? annee :
                anneeAcademiqueService.getAnneeAcademiqueEnCours());
        } else {
            apprenti.setAnneeAcademique(anneeAcademiqueService.getAnneeAcademiqueEnCours());
        }

        // Champs de mission optionnels
        if (columnMapping.containsKey("mission_mots_cles")) {
            apprenti.setMissionMotsCles(getCellStringValue(row, columnMapping.get("mission_mots_cles")));
        }

        if (columnMapping.containsKey("mission_metier_cible")) {
            apprenti.setMissionMetierCible(getCellStringValue(row, columnMapping.get("mission_metier_cible")));
        }

        if (columnMapping.containsKey("mission_commentaires")) {
            apprenti.setMissionCommentaires(getCellStringValue(row, columnMapping.get("mission_commentaires")));
        }

        if (columnMapping.containsKey("remarques_generales")) {
            apprenti.setRemarquesGenerales(getCellStringValue(row, columnMapping.get("remarques_generales")));
        }

        // Entreprise (obligatoire)
        String entrepriseNom = getCellStringValue(row, columnMapping.get("entreprise"));
        if (entrepriseNom == null || entrepriseNom.trim().isEmpty()) {
            throw new Exception("Nom d'entreprise manquant");
        }

        System.out.println("Recherche de l'entreprise: '" + entrepriseNom + "'");
        Entreprise entreprise = entrepriseService.getAllEntreprises().stream()
                .filter(e -> e.getRaisonSociale().equalsIgnoreCase(entrepriseNom.trim()))
                .findFirst()
                .orElse(null);

        if (entreprise == null) {
            // Afficher toutes les entreprises disponibles pour debug
            System.out.println("Entreprises disponibles: " +
                entrepriseService.getAllEntreprises().stream()
                    .map(e -> "'" + e.getRaisonSociale() + "'")
                    .collect(java.util.stream.Collectors.joining(", ")));
            throw new Exception("Entreprise non trouvée: '" + entrepriseNom + "'. Vérifiez que l'entreprise existe dans le système.");
        }
        apprenti.setEntreprise(entreprise);
        System.out.println("Entreprise trouvée: " + entreprise.getRaisonSociale());

        // Maître d'apprentissage (obligatoire)
        String maitreNom = getCellStringValue(row, columnMapping.get("maitre_apprentissage"));
        if (maitreNom == null || maitreNom.trim().isEmpty()) {
            throw new Exception("Nom du maître d'apprentissage manquant");
        }

        System.out.println("Recherche du maître d'apprentissage: '" + maitreNom + "'");
        MaitreApprentissage maitre = maitreApprentissageService.getAllMaitresApprentissage().stream()
                .filter(m -> {
                    String fullName1 = (m.getNom() + " " + m.getPrenom()).trim();
                    String fullName2 = (m.getPrenom() + " " + m.getNom()).trim();
                    return fullName1.equalsIgnoreCase(maitreNom.trim()) ||
                           fullName2.equalsIgnoreCase(maitreNom.trim());
                })
                .findFirst()
                .orElse(null);

        if (maitre == null) {
            // Afficher tous les maîtres disponibles pour debug
            System.out.println("Maîtres d'apprentissage disponibles: " +
                maitreApprentissageService.getAllMaitresApprentissage().stream()
                    .map(m -> "'" + m.getNom() + " " + m.getPrenom() + "'")
                    .collect(java.util.stream.Collectors.joining(", ")));
            throw new Exception("Maître d'apprentissage non trouvé: '" + maitreNom + "'. Vérifiez que le maître d'apprentissage existe dans le système.");
        }
        apprenti.setMaitreApprentissage(maitre);
        System.out.println("Maître d'apprentissage trouvé: " + maitre.getNom() + " " + maitre.getPrenom());

        // Tuteur enseignant (obligatoire)
        String tuteurNom = getCellStringValue(row, columnMapping.get("tuteur_enseignant"));
        if (tuteurNom == null || tuteurNom.trim().isEmpty()) {
            throw new Exception("Nom du tuteur enseignant manquant");
        }

        System.out.println("Recherche du tuteur enseignant: '" + tuteurNom + "'");
        Utilisateur tuteur = utilisateurService.getAllUtilisateurs().stream()
                .filter(u -> {
                    String fullName1 = (u.getNom() + " " + u.getPrenom()).trim();
                    String fullName2 = (u.getPrenom() + " " + u.getNom()).trim();
                    return fullName1.equalsIgnoreCase(tuteurNom.trim()) ||
                           fullName2.equalsIgnoreCase(tuteurNom.trim());
                })
                .findFirst()
                .orElse(null);

        if (tuteur == null) {
            // Afficher tous les tuteurs disponibles pour debug
            System.out.println("Tuteurs enseignants disponibles: " +
                utilisateurService.getAllUtilisateurs().stream()
                    .map(u -> "'" + u.getNom() + " " + u.getPrenom() + "'")
                    .collect(java.util.stream.Collectors.joining(", ")));
            throw new Exception("Tuteur enseignant non trouvé: '" + tuteurNom + "'. Vérifiez que le tuteur enseignant existe dans le système.");
        }
        apprenti.setTuteurEnseignant(tuteur);
        System.out.println("Tuteur enseignant trouvé: " + tuteur.getNom() + " " + tuteur.getPrenom());

        // Valeurs par défaut
        apprenti.setArchive(false);
        apprenti.setDateCreation(Instant.now());
        apprenti.setDateModification(Instant.now());

        return apprenti;
    }

    private String getCellStringValue(Row row, Integer columnIndex) {
        if (columnIndex == null) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((long) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    public static class ImportResult {
        private final List<Apprenti> createdApprentis;
        private final List<String> errors;
        private final int totalRows;
        private final int successfulImports;

        public ImportResult(List<Apprenti> createdApprentis, List<String> errors, int totalRows, int successfulImports) {
            this.createdApprentis = createdApprentis;
            this.errors = errors;
            this.totalRows = totalRows;
            this.successfulImports = successfulImports;
        }

        public List<Apprenti> getCreatedApprentis() { return createdApprentis; }
        public List<String> getErrors() { return errors; }
        public int getTotalRows() { return totalRows; }
        public int getSuccessfulImports() { return successfulImports; }
        public boolean hasErrors() { return !errors.isEmpty(); }
    }
}
