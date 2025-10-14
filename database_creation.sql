-- =============================================
-- SGBD : MariaDB / MySQL
-- =============================================

DROP TABLE IF EXISTS visite;
DROP TABLE IF EXISTS evaluation;
DROP TABLE IF EXISTS apprenti;
DROP TABLE IF EXISTS maitre_apprentissage;
DROP TABLE IF EXISTS entreprise;
DROP TABLE IF EXISTS utilisateur;

-- =============================================
-- Table : utilisateur (tuteur-enseignant)
-- Pour Spring Security
-- =============================================
CREATE TABLE utilisateur (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             username VARCHAR(100) NOT NULL UNIQUE COMMENT 'Login Spring Security',
                             password VARCHAR(255) NOT NULL COMMENT 'Mot de passe encodé BCrypt',
                             prenom VARCHAR(100) NOT NULL,
                             nom VARCHAR(100) NOT NULL,
                             email VARCHAR(150) NOT NULL,
                             role VARCHAR(50) DEFAULT 'ROLE_TUTEUR' COMMENT 'Préfixe ROLE_ obligatoire pour Spring Security',
                             enabled BOOLEAN DEFAULT TRUE COMMENT 'Compte actif/inactif',
                             date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             date_derniere_connexion TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE entreprise (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            raison_sociale VARCHAR(200) NOT NULL,
                            adresse TEXT,
                            informations_acces TEXT COMMENT 'Badge, pièce identité, étage, etc.'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE maitre_apprentissage (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      nom VARCHAR(100) NOT NULL,
                                      prenom VARCHAR(100) NOT NULL,
                                      poste VARCHAR(150),
                                      email VARCHAR(150),
                                      telephone VARCHAR(20),
                                      remarques TEXT,
                                      entreprise_id BIGINT NOT NULL,
                                      FOREIGN KEY (entreprise_id) REFERENCES entreprise(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE apprenti (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nom VARCHAR(100) NOT NULL,
                          prenom VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL,
                          telephone VARCHAR(20),
                          programme VARCHAR(100) COMMENT 'Ex: M2-PRO',
                          annee_academique VARCHAR(20) NOT NULL COMMENT 'Ex: 2023-2024',
                          majeure VARCHAR(150) COMMENT 'Ex: Digital Transformation',
                          niveau VARCHAR(10) DEFAULT 'I1' COMMENT 'I1, I2 ou I3',
                          archive BOOLEAN DEFAULT FALSE COMMENT 'true si apprenti archivé',

    -- Informations entreprise
                          entreprise_id BIGINT NOT NULL,
                          maitre_apprentissage_id BIGINT NOT NULL,

    -- Mission
                          mission_mots_cles TEXT COMMENT 'Ex: DevOps, node.js, CI/CD',
                          mission_metier_cible VARCHAR(200) COMMENT 'Référence Cigref',
                          mission_commentaires TEXT,

    -- Tuteur enseignant
                          tuteur_enseignant_id BIGINT NOT NULL,
                          feedback_tuteur TEXT COMMENT 'Commentaires libres du TE',

                          remarques_generales TEXT,
                          date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                          FOREIGN KEY (entreprise_id) REFERENCES entreprise(id) ON DELETE RESTRICT,
                          FOREIGN KEY (maitre_apprentissage_id) REFERENCES maitre_apprentissage(id) ON DELETE RESTRICT,
                          FOREIGN KEY (tuteur_enseignant_id) REFERENCES utilisateur(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Index pour améliorer les performances de recherche
CREATE INDEX idx_apprenti_nom ON apprenti(nom);
CREATE INDEX idx_apprenti_annee ON apprenti(annee_academique);
CREATE INDEX idx_apprenti_archive ON apprenti(archive);
CREATE INDEX idx_apprenti_niveau ON apprenti(niveau);

CREATE TABLE visite (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        apprenti_id BIGINT NOT NULL,
                        date_visite DATE,
                        format VARCHAR(20) COMMENT 'Visio ou Présentiel',
                        commentaires TEXT,
                        date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (apprenti_id) REFERENCES apprenti(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE evaluation (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            apprenti_id BIGINT NOT NULL,

    -- Évaluation mémoire/rapport
                            memoire_theme VARCHAR(255),
                            memoire_note DECIMAL(4,2) COMMENT 'Note sur 20',
                            memoire_commentaires TEXT,

    -- Évaluation soutenance
                            soutenance_date DATE,
                            soutenance_note DECIMAL(4,2) COMMENT 'Note sur 20',
                            soutenance_commentaires TEXT,

                            remarques_generales TEXT,
                            date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            date_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

                            FOREIGN KEY (apprenti_id) REFERENCES apprenti(id) ON DELETE CASCADE,
                            UNIQUE KEY unique_evaluation_apprenti (apprenti_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;