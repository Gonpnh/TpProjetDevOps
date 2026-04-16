package ytg.projetjavaytg.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ytg.projetjavaytg.Models.AnneeAcademique;
import ytg.projetjavaytg.Repositories.AnneeAcademiqueRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AnneeAcademiqueServiceTest {

    @Mock
    private AnneeAcademiqueRepository anneeAcademiqueRepository;

    @InjectMocks
    private AnneeAcademiqueService anneeAcademiqueService;

    @Test
    public void testGetAnneeAcademiqueEnCours_ActiveExists() {
        // Arrange
        AnneeAcademique anneeActive = new AnneeAcademique();
        anneeActive.setAnnee("2024-2025");
        anneeActive.setActive(true);

        when(anneeAcademiqueRepository.findByActiveTrue()).thenReturn(Optional.of(anneeActive));

        // Act
        String resultat = anneeAcademiqueService.getAnneeAcademiqueEnCours();

        // Assert
        assertEquals("2024-2025", resultat);
        verify(anneeAcademiqueRepository).findByActiveTrue();
    }
}
