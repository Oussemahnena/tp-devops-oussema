package tn.esprit.tpprojet2025.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpprojet2025.Entities.Equipe;
import tn.esprit.tpprojet2025.Entities.Entreprise;
import tn.esprit.tpprojet2025.Repositories.EquipeRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for EquipeServicesImpl")
class EquipeServicesImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServicesImpl equipeServices;

    private Equipe equipe;
    private Entreprise entreprise;

    @BeforeEach
    void setUp() {
        entreprise = new Entreprise();
        entreprise.setIdEntreprise(1L);
        entreprise.setNom("Test Entreprise");
        entreprise.setAdresse("123 Rue de Test");

        equipe = new Equipe();
        equipe.setIdEquipe(1L);
        equipe.setNomEquipe("Test Equipe");
        equipe.setSpecialite("Développement Web");
        equipe.setEntreprise(entreprise);
    }

    @Test
    @DisplayName("Should save equipe successfully")
    void testAjouterEquipe() {
        // Given
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // When
        Equipe result = equipeServices.AjouterEquipe(equipe);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdEquipe()).isEqualTo(1L);
        assertThat(result.getNomEquipe()).isEqualTo("Test Equipe");
        assertThat(result.getSpecialite()).isEqualTo("Développement Web");
        assertThat(result.getEntreprise()).isEqualTo(entreprise);
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    @DisplayName("Should return all equipes")
    void testAfficherListeEquipes() {
        // Given
        Equipe equipe2 = new Equipe();
        equipe2.setIdEquipe(2L);
        equipe2.setNomEquipe("Test Equipe 2");
        equipe2.setSpecialite("Data Science");
        equipe2.setEntreprise(entreprise);

        List<Equipe> equipes = Arrays.asList(equipe, equipe2);
        when(equipeRepository.findAll()).thenReturn(equipes);

        // When
        List<Equipe> result = equipeServices.afficherListeEquipes();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(equipe, equipe2);
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return equipe by ID")
    void testAfficherEquipeSelonID() {
        // Given
        when(equipeRepository.findById(anyLong())).thenReturn(Optional.of(equipe));

        // When
        Equipe result = equipeServices.afficherEquipeSelonID(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdEquipe()).isEqualTo(1L);
        assertThat(result.getNomEquipe()).isEqualTo("Test Equipe");
        assertThat(result.getSpecialite()).isEqualTo("Développement Web");
        verify(equipeRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when equipe not found by ID")
    void testAfficherEquipeSelonID_NotFound() {
        // Given
        when(equipeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            equipeServices.afficherEquipeSelonID(999L);
        });
        verify(equipeRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update equipe successfully")
    void testModifierEquipe() {
        // Given
        equipe.setNomEquipe("Updated Equipe");
        equipe.setSpecialite("Mobile Development");
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // When
        Equipe result = equipeServices.modifierEquipe(equipe);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomEquipe()).isEqualTo("Updated Equipe");
        assertThat(result.getSpecialite()).isEqualTo("Mobile Development");
        verify(equipeRepository, times(1)).save(equipe);
    }

    @Test
    @DisplayName("Should delete equipe by ID")
    void testSupprimerEquipe() {
        // Given
        doNothing().when(equipeRepository).deleteById(anyLong());

        // When
        equipeServices.supprimerEquipe(1L);

        // Then
        verify(equipeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle null input for ajouter equipe")
    void testAjouterEquipe_NullInput() {
        // Given
        when(equipeRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            equipeServices.AjouterEquipe(null);
        });
    }

    @Test
    @DisplayName("Should return empty list when no equipes exist")
    void testAfficherListeEquipes_EmptyList() {
        // Given
        when(equipeRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Equipe> result = equipeServices.afficherListeEquipes();

        // Then
        assertThat(result).isEmpty();
        verify(equipeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save equipe with entreprise relationship")
    void testAjouterEquipe_WithEntreprise() {
        // Given
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // When
        Equipe result = equipeServices.AjouterEquipe(equipe);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEntreprise()).isNotNull();
        assertThat(result.getEntreprise().getIdEntreprise()).isEqualTo(1L);
        assertThat(result.getEntreprise().getNom()).isEqualTo("Test Entreprise");
        verify(equipeRepository, times(1)).save(equipe);
    }
}