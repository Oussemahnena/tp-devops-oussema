package tn.esprit.tpprojet2025.Services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpprojet2025.Entities.*;
import tn.esprit.tpprojet2025.Repositories.EquipeRepository;
import tn.esprit.tpprojet2025.Repositories.ProjetDetailRepository;
import tn.esprit.tpprojet2025.Repositories.ProjetRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for ProjetServicesImpl")
class ProjetServicesImplTest {

    @Mock
    private ProjetRepository projetRepository;

    @Mock
    private ProjetDetailRepository projetDetailRepository;

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private ProjetServicesImpl projetServices;

    private Projet projet;
    private ProjetDetail projetDetail;
    private Equipe equipe;
    private Entreprise entreprise;

    @BeforeEach
    void setUp() {
        // Setup Entreprise
        entreprise = new Entreprise();
        entreprise.setIdEntreprise(1L);
        entreprise.setNom("Test Entreprise");

        // Setup ProjetDetail
        projetDetail = new ProjetDetail();
        projetDetail.setIdProjetDetail(1L);
        projetDetail.setDescription("Description test");
        projetDetail.setCout(1000L);

        // Setup Equipe
        equipe = new Equipe();
        equipe.setIdEquipe(1L);
        equipe.setNomEquipe("Test Equipe");
        equipe.setSpecialite("Développement");
        equipe.setEntreprise(entreprise);
        equipe.setProjets(new HashSet<>());

        // Setup Projet
        projet = new Projet();
        projet.setIdProjet(1L);
        projet.setNomProjet("Test Projet");
        projet.setTypeProjet(TypeProjet.DEV);
        projet.setProjetDetail(projetDetail);
        projet.setEquipes(new HashSet<>());
    }

    @Test
    @DisplayName("Should save projet successfully")
    void testAjouterProjet() {
        // Given
        when(projetRepository.save(any(Projet.class))).thenReturn(projet);

        // When
        Projet result = projetServices.AjouterProjet(projet);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdProjet()).isEqualTo(1L);
        assertThat(result.getNomProjet()).isEqualTo("Test Projet");
        assertThat(result.getTypeProjet()).isEqualTo(TypeProjet.DEV);
        verify(projetRepository, times(1)).save(projet);
    }

    @Test
    @DisplayName("Should return all projets")
    void testAfficherListeProjets() {
        // Given
        Projet projet2 = new Projet();
        projet2.setIdProjet(2L);
        projet2.setNomProjet("Test Projet 2");
        projet2.setTypeProjet(TypeProjet.DS);

        List<Projet> projets = Arrays.asList(projet, projet2);
        when(projetRepository.findAll()).thenReturn(projets);

        // When
        List<Projet> result = projetServices.afficherListeProjets();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).contains(projet, projet2);
        verify(projetRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return projet by ID")
    void testAfficherProjetSelonID() {
        // Given
        when(projetRepository.findById(anyLong())).thenReturn(Optional.of(projet));

        // When
        Projet result = projetServices.afficherProjetSelonID(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getIdProjet()).isEqualTo(1L);
        assertThat(result.getNomProjet()).isEqualTo("Test Projet");
        verify(projetRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when projet not found by ID")
    void testAfficherProjetSelonID_NotFound() {
        // Given
        when(projetRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            projetServices.afficherProjetSelonID(999L);
        });
        verify(projetRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Should update projet successfully")
    void testModifierProjet() {
        // Given
        projet.setNomProjet("Updated Projet");
        when(projetRepository.save(any(Projet.class))).thenReturn(projet);

        // When
        Projet result = projetServices.modifierProjet(projet);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNomProjet()).isEqualTo("Updated Projet");
        verify(projetRepository, times(1)).save(projet);
    }

    @Test
    @DisplayName("Should delete projet by ID")
    void testSupprimerProjet() {
        // Given
        doNothing().when(projetRepository).deleteById(anyLong());

        // When
        projetServices.supprimerProjet(1L);

        // Then
        verify(projetRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should assign ProjetDetail to Projet successfully")
    void testAssignProjetDetailToProjet() {
        // Given
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(projetDetailRepository.findById(1L)).thenReturn(Optional.of(projetDetail));
        when(projetRepository.save(any(Projet.class))).thenReturn(projet);

        // When
        projetServices.assignProjetDetailToProjet(1L, 1L);

        // Then
        verify(projetRepository, times(1)).findById(1L);
        verify(projetDetailRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).save(projet);
        assertThat(projet.getProjetDetail()).isEqualTo(projetDetail);
    }

    @Test
    @DisplayName("Should assign Projet to Equipe successfully")
    void testAssignProjetToEquipe() {
        // Given
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // When
        projetServices.assignProjetToEquipe(1L, 1L);

        // Then
        verify(projetRepository, times(1)).findById(1L);
        verify(equipeRepository, times(1)).findById(1L);
        verify(equipeRepository, times(1)).save(equipe);
        assertThat(equipe.getProjets()).contains(projet);
    }

    @Test
    @DisplayName("Should add projet and assign ProjetDetail successfully")
    void testAddProjetAndAssignProjetDetailToProjet() {
        // Given
        when(projetDetailRepository.findById(1L)).thenReturn(Optional.of(projetDetail));
        when(projetRepository.save(any(Projet.class))).thenReturn(projet);

        // When
        Projet result = projetServices.addProjetAndAssignProjetDetailToProjet(1L, projet);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getProjetDetail()).isEqualTo(projetDetail);
        verify(projetDetailRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).save(projet);
    }

    @Test
    @DisplayName("Should unassign ProjetDetail from Projet successfully")
    void testUnassignProjetDetailFromProjet() {
        // Given
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(projetRepository.save(any(Projet.class))).thenReturn(projet);

        // When
        projetServices.UnassignProjetDetailFromProjet(1L);

        // Then
        verify(projetRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).save(projet);
        assertThat(projet.getProjetDetail()).isNull();
    }

    @Test
    @DisplayName("Should unassign Projet from Equipe successfully")
    void testUnassignProjetFromEquipe() {
        // Given
        equipe.getProjets().add(projet);
        when(equipeRepository.findById(1L)).thenReturn(Optional.of(equipe));
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(equipeRepository.save(any(Equipe.class))).thenReturn(equipe);

        // When
        projetServices.UnassignProjetFromEquipe(1L, 1L);

        // Then
        verify(equipeRepository, times(1)).findById(1L);
        verify(projetRepository, times(1)).findById(1L);
        verify(equipeRepository, times(1)).save(equipe);
        assertThat(equipe.getProjets()).doesNotContain(projet);
    }

    @Test
    @DisplayName("Should handle null input for ajouter projet")
    void testAjouterProjet_NullInput() {
        // Given
        when(projetRepository.save(null)).thenThrow(IllegalArgumentException.class);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            projetServices.AjouterProjet(null);
        });
    }

    @Test
    @DisplayName("Should return empty list when no projets exist")
    void testAfficherListeProjets_EmptyList() {
        // Given
        when(projetRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<Projet> result = projetServices.afficherListeProjets();

        // Then
        assertThat(result).isEmpty();
        verify(projetRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should throw exception when assigning non-existent ProjetDetail")
    void testAssignProjetDetailToProjet_ProjetDetailNotFound() {
        // Given
        when(projetRepository.findById(1L)).thenReturn(Optional.of(projet));
        when(projetDetailRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            projetServices.assignProjetDetailToProjet(1L, 999L);
        });
    }

    @Test
    @DisplayName("Should throw exception when assigning to non-existent Projet")
    void testAssignProjetDetailToProjet_ProjetNotFound() {
        // Given
        when(projetRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(java.util.NoSuchElementException.class, () -> {
            projetServices.assignProjetDetailToProjet(999L, 1L);
        });
    }
}