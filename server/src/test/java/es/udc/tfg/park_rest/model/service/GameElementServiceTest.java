package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.repository.GameElementDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.service.dto.GameElementDTO;
import es.udc.tfg.park_rest.model.service.util.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameElementServiceTest {

  @Mock
  private GameElementDao gameElementDao;

  @Mock
  private ParkDao parkDao;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private GameElementService gameElementService;

  @Test
  public void testFindAll() {
    GameElement element = new GameElement();
    element.setId(1L);
    when(gameElementDao.findAll()).thenReturn(List.of(element));

    assertFalse(gameElementService.findAll().isEmpty());
    assertEquals(1, gameElementService.findAll().size());
    verify(gameElementDao, times(2)).findAll();
  }

  @Test
  public void testFindById() throws NotFoundException {
    GameElement element = new GameElement();
    element.setId(1L);
    when(gameElementDao.findById(1L)).thenReturn(element);

    GameElementDTO result = gameElementService.findById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(gameElementDao).findById(1L);
  }

  @Test
  public void testFindByIdNotFound() {
    when(gameElementDao.findById(1L)).thenReturn(null);

    assertThrows(NotFoundException.class, () -> gameElementService.findById(1L));
    verify(gameElementDao).findById(1L);
  }

  @Test
  public void testCreate() throws NotFoundException {
    GameElementDTO dto = new GameElementDTO();
    dto.setParkId(1L);
    dto.setElementName("Test Element");

    Park park = new Park();
    park.setId(1L);

    when(parkDao.findById(1L)).thenReturn(park);
    doAnswer(invocation -> {
      GameElement element = invocation.getArgument(0);
      element.setId(1L); // Simulate the ID generation
      return null;
    }).when(gameElementDao).create(any(GameElement.class));

    GameElementDTO result = gameElementService.create(dto);
    assertNotNull(result);
    assertEquals(1L, result.getParkId());
    assertEquals("Test Element", result.getElementName());
    verify(parkDao).findById(1L);
    verify(gameElementDao).create(any(GameElement.class));
  }

  @Test
  public void testUpdate() throws NotFoundException {
    GameElement existingElement = new GameElement();
    existingElement.setId(1L);
    existingElement.setElementName("Old Name");

    GameElementDTO dto = new GameElementDTO();
    dto.setId(1L);
    dto.setElementName("New Name");

    when(gameElementDao.findById(1L)).thenReturn(existingElement);
    doNothing().when(gameElementDao).update(any(GameElement.class));

    GameElementDTO result = gameElementService.update(dto);
    assertNotNull(result);
    assertEquals("New Name", result.getElementName());
    verify(gameElementDao).findById(1L);
    verify(gameElementDao).update(any(GameElement.class));
  }

  @Test
  public void testFindByParkId() {
    GameElement element = new GameElement();
    element.setId(1L);
    when(gameElementDao.findByParkId(1L)).thenReturn(List.of(element));

    List<GameElementDTO> results = (List<GameElementDTO>) gameElementService.findByParkId(1L);
    assertFalse(results.isEmpty());
    assertEquals(1, results.size());
    assertEquals(1L, results.get(0).getId());
    verify(gameElementDao).findByParkId(1L);
  }

  @Test
  public void testDeleteById() throws NotFoundException {
    GameElement element = new GameElement();
    element.setId(1L);

    when(gameElementDao.findById(1L)).thenReturn(element);
    doNothing().when(gameElementDao).deleteById(1L);

    assertDoesNotThrow(() -> gameElementService.deleteById(1L));
    verify(gameElementDao).findById(1L);
    verify(gameElementDao).deleteById(1L);
  }

}
