package tn.esprit.spring.kaddem.Test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;
import tn.esprit.spring.kaddem.services.EquipeServiceImpl;
import tn.esprit.spring.kaddem.services.EtudiantServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipeServiceImplTest {

    @InjectMocks
    private EquipeServiceImpl equipeService;
  
    @Mock
    private EquipeRepository equipeRepository;


    @Test
    void testRetrieveEquipe() {
        Equipe sampleEquipe = new Equipe();
        sampleEquipe.setIdEquipe(1);
        sampleEquipe.setNomEquipe("Sample Equipe");

        when(equipeRepository.findById(1)).thenReturn(Optional.of(sampleEquipe));

        Equipe result = equipeService.retrieveEquipe(1);

        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        assertEquals("Sample Equipe", result.getNomEquipe());
    }

    @Test
    void testAddEquipe() {
        Equipe sampleEquipe = new Equipe();
        sampleEquipe.setIdEquipe(1);
        sampleEquipe.setNomEquipe("Sample Equipe");

        when(equipeRepository.save(any())).thenReturn(sampleEquipe);

        Equipe result = equipeService.addEquipe(sampleEquipe);

        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        assertEquals("Sample Equipe", result.getNomEquipe());
    }

    @Test
    void testUpdateEquipe() {
        Equipe sampleEquipe = new Equipe();
        sampleEquipe.setIdEquipe(1);
        sampleEquipe.setNomEquipe("Sample Equipe");

        when(equipeRepository.save(any())).thenReturn(sampleEquipe);

        Equipe result = equipeService.updateEquipe(sampleEquipe);

        assertNotNull(result);
        assertEquals(1, result.getIdEquipe());
        assertEquals("Sample Equipe", result.getNomEquipe());
    }

    @Test
    void testAddAndDeleteEquipe() {
        Equipe sampleEquipe = new Equipe();
        sampleEquipe.setIdEquipe(1);
        sampleEquipe.setNomEquipe("Sample Equipe");

        when(equipeRepository.save(any())).thenReturn(sampleEquipe);

        Equipe addedEquipe = equipeService.addEquipe(sampleEquipe);

        assertNotNull(addedEquipe);

        when(equipeRepository.findById(1)).thenReturn(Optional.of(sampleEquipe));
        doNothing().when(equipeRepository).delete(sampleEquipe);

        equipeService.deleteEquipe(1);

        verify(equipeRepository, times(1)).delete(sampleEquipe);
    }

    @Test
    void testRetrieveAllEquipes() {
        Equipe equipe1 = new Equipe();
        equipe1.setIdEquipe(1);
        equipe1.setNomEquipe("Equipe 1");

        Equipe equipe2 = new Equipe();
        equipe2.setIdEquipe(2);
        equipe2.setNomEquipe("Equipe 2");

        List<Equipe> sampleEquipes = Arrays.asList(equipe1, equipe2);

        when(equipeRepository.findAll()).thenReturn(sampleEquipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getIdEquipe());
        assertEquals("Equipe 1", result.get(0).getNomEquipe());
        assertEquals(2, result.get(1).getIdEquipe());
        assertEquals("Equipe 2", result.get(1).getNomEquipe());
    }

    @Test
    void testDeleteEquipeWithNonExistentEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> equipeService.deleteEquipe(1));

        verify(equipeRepository, never()).delete(any());
    }

    @Test
    void testErrorHandling() {
        when(equipeRepository.save(any())).thenThrow(new RuntimeException("Repository error"));

        Equipe sampleEquipe = new Equipe();

        assertThrows(RuntimeException.class, () -> equipeService.addEquipe(sampleEquipe));

        verify(equipeRepository, never()).delete(any());
    }

    @Test
    void testRetrieveEquipeWithNonExistentEquipe() {
        when(equipeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> equipeService.retrieveEquipe(1));
    }

    @Test
    void testRetrieveAllEquipesWhenEmpty() {
        when(equipeRepository.findAll()).thenReturn(Collections.emptyList());

        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateEquipeWithInvalidData() {
        Equipe sampleEquipe = new Equipe();
        sampleEquipe.setIdEquipe(1);
        sampleEquipe.setNomEquipe("");

        when(equipeRepository.save(any())).thenThrow(new DataIntegrityViolationException("Data integrity violation"));

        assertThrows(DataIntegrityViolationException.class, () -> equipeService.updateEquipe(sampleEquipe));
    }

    @Test
    void testAddMultipleEquipes() {
        Equipe equipe1 = new Equipe();
        equipe1.setIdEquipe(1);
        equipe1.setNomEquipe("Equipe 1");

        Equipe equipe2 = new Equipe();
        equipe2.setIdEquipe(2);
        equipe2.setNomEquipe("Equipe 2");

        when(equipeRepository.save(eq(equipe1))).thenReturn(equipe1);
        when(equipeRepository.save(eq(equipe2))).thenReturn(equipe2);

        Equipe addedEquipe1 = equipeService.addEquipe(equipe1);
        Equipe addedEquipe2 = equipeService.addEquipe(equipe2);
    }
}
