package tn.esprit.tpprojet2025.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpprojet2025.Entities.Entreprise;
import tn.esprit.tpprojet2025.Repositories.EntrepriseRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for EntrepriseServicesImpl")
class EntrepriseServicesImplTest {

    @Mock
    private EntrepriseRepository entrepriseRepository;

    @InjectMocks
    private EntrepriseServicesImpl entrepriseServices;

    private Entreprise entreprise;

    @BeforeEach
    void setUp() {
        entreprise = new Entreprise();
        entreprise.setIdEntreprise(1L);
        entreprise.setNom("Test Entreprise");
        entreprise.setAdresse("123 Rue de Test");
    }

    @Test
    @DisplayName("Should save entreprise successfully")
    void testAjouterEntreprise() {
        // Given
        when(entrepriseRepository.save(any(Entreprise.class))).thenReturn(entreprise);

        // When
        Entreprise result = entrepriseServices.AjouterEntreprise(entreprise);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdEntreprise()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Test Entreprise");
        assertThat(result.getAdresse()).isEqualTo("123 Rue de Test");
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    @DisplayName("Should return all entreprises")
    void testAfficherListeEntreprise() {
        // Given
        Entreprise entreprise2 = new Entreprise();
        entreprise2.setIdEntreprise(2L);
        entreprise2.setNom("Test Entreprise 2");
        entreprise2.setAdresse("456 Rue de Test");

        List<Entreprise> entreprises = Arrays.asList(entreprise, entreprise2);
        when(entrepriseRepository.findAll()).thenReturn(entreprises);

        // When
        List<Entreprise> result = entrepriseServices.afficherListeEntreprise();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(entreprise, entreprise2);
        verify(entrepriseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return entreprise by ID")
    void testAfficherEntrepriseSelonID() {
        // Given
        when(entrepriseRepository.findById(anyLong())).thenReturn(Optional.of(entreprise));

        // When
        Entreprise result = entrepriseServices.afficherEntrepriseSelonID(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdEntreprise()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Test Entreprise");
        verify(entrepriseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when entreprise not found by ID")
    void testAfficherEntrepriseSelonID_NotFound() {
        // Given
        when(entrepriseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            entrepriseServices.afficherEntrepriseSelonID(999L);
        });
        verify(entrepriseRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update entreprise successfully")
    void testModifierEntreprise() {
        // Given
        entreprise.setNom("Updated Entreprise");
        when(entrepriseRepository.save(any(Entreprise.class))).thenReturn(entreprise);

        // When
        Entreprise result = entrepriseServices.modifierEntreprise(entreprise);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Updated Entreprise");
        verify(entrepriseRepository, times(1)).save(entreprise);
    }

    @Test
    @DisplayName("Should delete entreprise by ID")
    void testSupprimerEntreprise() {
        // Given
        doNothing().when(entrepriseRepository).deleteById(anyLong());

        // When
        entrepriseServices.supprimerEntreprise(1L);

        // Then
        verify(entrepriseRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle null input for ajouter entreprise")
    void testAjouterEntreprise_NullInput() {
        // Given
        when(entrepriseRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            entrepriseServices.AjouterEntreprise(null);
        });
    }

    @Test
    @DisplayName("Should return empty list when no entreprises exist")
    void testAfficherListeEntreprise_EmptyList() {
        // Given
        when(entrepriseRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Entreprise> result = entrepriseServices.afficherListeEntreprise();

        // Then
        assertThat(result).isEmpty();
        verify(entrepriseRepository, times(1)).findAll();
    }
}