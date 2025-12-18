package tn.esprit.tpprojet2025.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpprojet2025.Entities.ProjetDetail;
import tn.esprit.tpprojet2025.Repositories.ProjetDetailRepository;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ProjetDetailServicesImpl")
class ProjetDetailServicesImplTest {

    @Mock
    private ProjetDetailRepository projetDetailRepository;

    @InjectMocks
    private ProjetDetailServicesImpl projetDetailServices;

    private ProjetDetail projetDetail;

    @BeforeEach
    void setUp() {
        projetDetail = new ProjetDetail();
        projetDetail.setIdProjetDetail(1L);
        projetDetail.setDescription("Description du projet de test");
        projetDetail.setTechnologie("Spring Boot, Angular");
        projetDetail.setCout(15000L);
        projetDetail.setDateDebut(LocalDate.of(2024, 1, 1));
    }

    @Test
    @DisplayName("Should save projetDetail successfully")
    void testAjouterProjetDetail() {
        // Given
        when(projetDetailRepository.save(any(ProjetDetail.class))).thenReturn(projetDetail);

        // When
        ProjetDetail result = projetDetailServices.AjouterProjetDetail(projetDetail);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdProjetDetail()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Description du projet de test");
        assertThat(result.getTechnologie()).isEqualTo("Spring Boot, Angular");
        assertThat(result.getCout()).isEqualTo(15000L);
        assertThat(result.getDateDebut()).isEqualTo(LocalDate.of(2024, 1, 1));
        verify(projetDetailRepository, times(1)).save(projetDetail);
    }

    @Test
    @DisplayName("Should return all projetDetails")
    void testAfficherListeProjetDetail() {
        // Given
        ProjetDetail projetDetail2 = new ProjetDetail();
        projetDetail2.setIdProjetDetail(2L);
        projetDetail2.setDescription("Deuxième projet de test");
        projetDetail2.setTechnologie("React, Node.js");
        projetDetail2.setCout(20000L);
        projetDetail2.setDateDebut(LocalDate.of(2024, 2, 1));

        List<ProjetDetail> projetDetails = Arrays.asList(projetDetail, projetDetail2);
        when(projetDetailRepository.findAll()).thenReturn(projetDetails);

        // When
        List<ProjetDetail> result = projetDetailServices.afficherListeProjetDetail();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(projetDetail, projetDetail2);
        verify(projetDetailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return projetDetail by ID")
    void testAfficherProjetDetailSelonID() {
        // Given
        when(projetDetailRepository.findById(anyLong())).thenReturn(Optional.of(projetDetail));

        // When
        ProjetDetail result = projetDetailServices.afficherProjetDetailSelonID(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdProjetDetail()).isEqualTo(1L);
        assertThat(result.getDescription()).isEqualTo("Description du projet de test");
        assertThat(result.getTechnologie()).isEqualTo("Spring Boot, Angular");
        verify(projetDetailRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when projetDetail not found by ID")
    void testAfficherProjetDetailSelonID_NotFound() {
        // Given
        when(projetDetailRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            projetDetailServices.afficherProjetDetailSelonID(999L);
        });
        verify(projetDetailRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update projetDetail successfully")
    void testModifierProjetDetail() {
        // Given
        projetDetail.setDescription("Description mise à jour");
        projetDetail.setTechnologie("Vue.js, Express");
        projetDetail.setCout(25000L);
        when(projetDetailRepository.save(any(ProjetDetail.class))).thenReturn(projetDetail);

        // When
        ProjetDetail result = projetDetailServices.modifierProjetDetail(projetDetail);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Description mise à jour");
        assertThat(result.getTechnologie()).isEqualTo("Vue.js, Express");
        assertThat(result.getCout()).isEqualTo(25000L);
        verify(projetDetailRepository, times(1)).save(projetDetail);
    }

    @Test
    @DisplayName("Should delete projetDetail by ID")
    void testSupprimerProjetDetail() {
        // Given
        doNothing().when(projetDetailRepository).deleteById(anyLong());

        // When
        projetDetailServices.supprimerProjetDetail(1L);

        // Then
        verify(projetDetailRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle null input for ajouter projetDetail")
    void testAjouterProjetDetail_NullInput() {
        // Given
        when(projetDetailRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            projetDetailServices.AjouterProjetDetail(null);
        });
    }

    @Test
    @DisplayName("Should return empty list when no projetDetails exist")
    void testAfficherListeProjetDetail_EmptyList() {
        // Given
        when(projetDetailRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<ProjetDetail> result = projetDetailServices.afficherListeProjetDetail();

        // Then
        assertThat(result).isEmpty();
        verify(projetDetailRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should save projetDetail with complete data")
    void testAjouterProjetDetail_CompleteData() {
        // Given
        ProjetDetail newProjetDetail = new ProjetDetail();
        newProjetDetail.setDescription("Projet complet avec toutes les données");
        newProjetDetail.setTechnologie("Java, Spring, MySQL");
        newProjetDetail.setCout(30000L);
        newProjetDetail.setDateDebut(LocalDate.of(2024, 6, 1));

        when(projetDetailRepository.save(any(ProjetDetail.class))).thenReturn(newProjetDetail);

        // When
        ProjetDetail result = projetDetailServices.AjouterProjetDetail(newProjetDetail);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDescription()).isEqualTo("Projet complet avec toutes les données");
        assertThat(result.getTechnologie()).isEqualTo("Java, Spring, MySQL");
        assertThat(result.getCout()).isEqualTo(30000L);
        assertThat(result.getDateDebut()).isEqualTo(LocalDate.of(2024, 6, 1));
        verify(projetDetailRepository, times(1)).save(newProjetDetail);
    }

    @Test
    @DisplayName("Should handle projetDetail with zero cost")
    void testAjouterProjetDetail_ZeroCost() {
        // Given
        projetDetail.setCout(0L);
        when(projetDetailRepository.save(any(ProjetDetail.class))).thenReturn(projetDetail);

        // When
        ProjetDetail result = projetDetailServices.AjouterProjetDetail(projetDetail);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCout()).isEqualTo(0L);
        verify(projetDetailRepository, times(1)).save(projetDetail);
    }

    @Test
    @DisplayName("Should handle projetDetail with future date")
    void testAjouterProjetDetail_FutureDate() {
        // Given
        LocalDate futureDate = LocalDate.of(2025, 12, 31);
        projetDetail.setDateDebut(futureDate);
        when(projetDetailRepository.save(any(ProjetDetail.class))).thenReturn(projetDetail);

        // When
        ProjetDetail result = projetDetailServices.AjouterProjetDetail(projetDetail);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getDateDebut()).isEqualTo(futureDate);
        verify(projetDetailRepository, times(1)).save(projetDetail);
    }
}