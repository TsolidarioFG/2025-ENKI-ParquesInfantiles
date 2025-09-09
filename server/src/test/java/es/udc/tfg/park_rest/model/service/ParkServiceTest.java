package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.GameElementDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.util.ImageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkServiceTest {

  @Mock
  private ParkDao parkDao;

  @Mock
  private UsuarioDao usuarioDao;

  @Mock
  private GameElementDao gameElementDao;

  @Mock
  private ImageService imageService;

  @InjectMocks
  private ParkService parkService;

  @Test
  public void testFindAll() {
    Park park = new Park();
    park.setId(1L);
    when(parkDao.findAll()).thenReturn(List.of(park));

    // Call once and store result
    Collection<ParkDTO> result = parkService.findAll();

    // Perform assertions on stored result
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());

    // Verify exactly one call
    verify(parkDao, times(1)).findAll();
  }

  @Test
  public void testFindById() throws NotFoundException {
    Park park = new Park();
    park.setId(1L);
    when(parkDao.findById(1L)).thenReturn(park);

    ParkDTO result = parkService.findById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(parkDao).findById(1L);
  }

  @Test
  public void testCreate() {
    ParkDTO dto = new ParkDTO();
    dto.setName("Test Park");

    doAnswer(invocation -> {
      Park park = invocation.getArgument(0);
      park.setId(1L);
      return null;
    }).when(parkDao).create(any(Park.class));

    ParkDTO result = parkService.create(dto);
    assertNotNull(result);
    assertEquals("Test Park", result.getName());
    verify(parkDao).create(any(Park.class));
  }

  @Test
  public void testUpdate() throws NotFoundException {
    Park existingPark = new Park();
    existingPark.setId(1L);
    existingPark.setName("Old Name");

    ParkDTO dto = new ParkDTO();
    dto.setId(1L);
    dto.setName("New Name");

    when(parkDao.findById(1L)).thenReturn(existingPark);
    doNothing().when(parkDao).update(any(Park.class));

    ParkDTO result = parkService.update(dto);
    assertNotNull(result);
    assertEquals("New Name", result.getName());
    verify(parkDao).findById(1L);
    verify(parkDao).update(any(Park.class));
  }

  @Test
  public void testDeleteById() throws NotFoundException, OperationNotAllowed {
    Park park = new Park();
    park.setId(1L);
    Usuario user = new Usuario();

    when(parkDao.findById(1L)).thenReturn(park);
    when(usuarioDao.findUsuariosByFavoritoId(1L)).thenReturn(List.of(user));
    when(gameElementDao.findByParkId(1L)).thenReturn(Collections.emptyList());
    doNothing().when(parkDao).deleteById(1L);

    assertDoesNotThrow(() -> parkService.deleteById(1L));
    verify(parkDao).findById(1L);
    verify(usuarioDao).findUsuariosByFavoritoId(1L);
    verify(gameElementDao).findByParkId(1L);
    verify(parkDao).deleteById(1L);
  }

}
