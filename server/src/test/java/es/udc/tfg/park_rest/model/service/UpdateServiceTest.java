package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Update;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UpdateDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.UpdateDTO;
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
public class UpdateServiceTest {

  @Mock
  private UpdateDao updateDao;

  @Mock
  private ParkDao parkDao;

  @Mock
  private UsuarioDao usuarioDao;

  @InjectMocks
  private UpdateService updateService;

  @Test
  public void testFindAll() {
    Update update = new Update();
    update.setId(1L);
    Usuario user = new Usuario();
    user.setId(1L);
    update.setUser(user);

    when(updateDao.findAll()).thenReturn(List.of(update));

    // Call the service method once and store the result
    Collection<UpdateDTO> result = updateService.findAll();

    // Perform assertions on the stored result
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());

    // Verify the DAO was called exactly once
    verify(updateDao, times(1)).findAll();
  }

  @Test
  public void testCreate() throws NotFoundException {
    UpdateDTO dto = new UpdateDTO();
    dto.setUserId(1L);
    dto.setTitle("Test Update");

    Usuario user = new Usuario();
    user.setId(1L);

    when(usuarioDao.findById(1L)).thenReturn(user);
    doAnswer(invocation -> {
      Update update = invocation.getArgument(0);
      update.setId(1L);
      return null;
    }).when(updateDao).create(any(Update.class));

    UpdateDTO result = updateService.create(dto);
    assertNotNull(result);
    assertEquals("Test Update", result.getTitle());
    verify(usuarioDao).findById(1L);
    verify(updateDao).create(any(Update.class));
  }

  @Test
  public void testGetUpdatesByPark() throws NotFoundException {
    Park park = new Park();
    park.setId(1L);
    Update update = new Update();
    update.setId(1L);

    Usuario user = new Usuario();
    user.setId(1L);
    update.setUser(user);

    when(parkDao.findById(1L)).thenReturn(park);
    when(updateDao.findByParkId(1L)).thenReturn(List.of(update));

    List<UpdateDTO> results = updateService.getUpdatesByPark(1L);
    assertFalse(results.isEmpty());
    assertEquals(1, results.size());
    verify(parkDao).findById(1L);
    verify(updateDao).findByParkId(1L);
  }

  @Test
  public void testDeleteById() throws NotFoundException, OperationNotAllowed {
    Update update = new Update();
    update.setId(1L);

    when(updateDao.findById(1L)).thenReturn(update);
    doNothing().when(updateDao).deleteById(1L);

    assertDoesNotThrow(() -> updateService.deleteById(1L));
    verify(updateDao).findById(1L);
    verify(updateDao).deleteById(1L);
  }

  @Test
  public void testUpdate() throws NotFoundException {
    Update existingUpdate = new Update();
    existingUpdate.setId(1L);
    existingUpdate.setTitle("Old Title");
    Usuario user = new Usuario();
    user.setId(1L);
    existingUpdate.setUser(user);
    UpdateDTO dto = new UpdateDTO();
    dto.setId(1L);
    dto.setTitle("New Title");

    when(updateDao.findById(1L)).thenReturn(existingUpdate);
    doNothing().when(updateDao).update(any(Update.class));

    UpdateDTO result = updateService.update(dto);
    assertNotNull(result);
    assertEquals("New Title", result.getTitle());
    verify(updateDao).findById(1L);
    verify(updateDao).update(any(Update.class));
  }
}
