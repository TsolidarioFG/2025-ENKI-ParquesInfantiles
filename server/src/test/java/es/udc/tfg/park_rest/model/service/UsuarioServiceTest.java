package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.UserAuthority;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.enums.UserType;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.UsuarioNombreUsuarioExistsException;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import es.udc.tfg.park_rest.security.SecurityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

  @Mock
  private UsuarioDao usuarioDao;

  @Mock
  private ParkDao parkDao;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UsuarioService usuarioService;

  private Usuario testUser;
  private Park testPark;

  @BeforeEach
  void setUp() {
    testUser = new Usuario();
    testUser.setId(1L);
    testUser.setNombreUsuario("testuser");
    testUser.setContraseña("password");
    testUser.setNombre("Test");
    testUser.setApellido("User");
    testUser.setTelefono("123456789");
    testUser.setEmail("test@example.com");
    testUser.setUserType(UserType.FAMILIAR_O_ACOMPAÑANTE);
    testUser.setAuthority(UserAuthority.USER);

    testPark = new Park();
    testPark.setId(1L);
    testPark.setName("Test Park");
  }

  @Test
  void testFindAll() {
    when(usuarioDao.findAll()).thenReturn(Arrays.asList(testUser));

    Collection<UsuarioDTOPrivate> result = usuarioService.findAll();

    assertEquals(1, result.size());
    assertEquals("testuser", result.iterator().next().getNombreUsuario());
    verify(usuarioDao, times(1)).findAll();
  }

  @Test
  void testRegisterUsuarioSuccess() throws UsuarioNombreUsuarioExistsException {
    when(usuarioDao.findByNombreUsuario("newuser")).thenReturn(null);
    when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

    usuarioService.registerUsuario("newuser", "password", "New", "User", "987654321", "new@example.com", false, UserType.FAMILIAR_O_ACOMPAÑANTE);

    verify(usuarioDao, times(1)).create(any(Usuario.class));
  }

  @Test
  void testRegisterUsuarioThrowsExceptionWhenUsernameExists() {
    when(usuarioDao.findByNombreUsuario("existinguser")).thenReturn(testUser);

    assertThrows(UsuarioNombreUsuarioExistsException.class, () -> {
      usuarioService.registerUsuario("existinguser", "password", "New", "User", "987654321", "new@example.com", false, UserType.FAMILIAR_O_ACOMPAÑANTE);
    });
  }

  @Test
  void testUpdateUsuario() {
    try (MockedStatic<SecurityUtils> utilities = mockStatic(SecurityUtils.class)) {
      utilities.when(SecurityUtils::getCurrentUserLogin).thenReturn("testuser");
      when(usuarioDao.findByNombreUsuario("testuser")).thenReturn(testUser);

      UsuarioDTOPrivate result = usuarioService.updateUsuario("Updated", "User", "987654321", "testuser", "updated@example.com", UserType.FAMILIAR_O_ACOMPAÑANTE);

      assertEquals("Updated", result.getNombre());
      assertEquals("User", result.getApellido());
      assertEquals("987654321", result.getTelefono());
      assertEquals("updated@example.com", result.getEmail());
      assertEquals(UserType.FAMILIAR_O_ACOMPAÑANTE, result.getUserType());
      verify(usuarioDao, times(1)).update(testUser);
    }
  }

  @Test
  void testUpdateUsuarioWithPassword() {
    try (MockedStatic<SecurityUtils> utilities = mockStatic(SecurityUtils.class)) {
      utilities.when(SecurityUtils::getCurrentUserLogin).thenReturn("testuser");
      when(usuarioDao.findByNombreUsuario("testuser")).thenReturn(testUser);
      when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");

      UsuarioDTOPrivate usuarioDTO = new UsuarioDTOPrivate();
      usuarioDTO.setContraseña("newpassword");
      usuarioDTO.setNombre("Updated");
      usuarioDTO.setApellido("User");
      usuarioDTO.setTelefono("987654321");
      usuarioDTO.setEmail("updated@example.com");
      usuarioDTO.setNombreUsuario("testuser");

      UsuarioDTOPrivate result = usuarioService.updateUsuarioWithPassword(usuarioDTO);

      assertEquals("Updated", result.getNombre());
      verify(usuarioDao, times(1)).update(testUser);
    }
  }

  @Test
  void testGetCurrentUserWithAuthority() {
    try (MockedStatic<SecurityUtils> utilities = mockStatic(SecurityUtils.class)) {
      utilities.when(SecurityUtils::getCurrentUserLogin).thenReturn("testuser");
      when(usuarioDao.findByNombreUsuario("testuser")).thenReturn(testUser);

      UsuarioDTOPrivate result = usuarioService.getCurrentUserWithAuthority();

      assertNotNull(result);
      assertEquals("testuser", result.getNombreUsuario());
    }
  }

  @Test
  void testAñadirAFavoritos() throws NotFoundException {
    when(usuarioDao.findById(1L)).thenReturn(testUser);
    when(parkDao.findById(1L)).thenReturn(testPark);

    usuarioService.añadirAFavoritos(1L, 1L);

    assertTrue(testUser.getFavoritos().contains(testPark));
    verify(usuarioDao, times(1)).update(testUser);
  }

  @Test
  void testAñadirAFavoritosThrowsUsuarioNotFoundException() {
    when(usuarioDao.findById(1L)).thenReturn(null);

    assertThrows(NotFoundException.class, () -> {
      usuarioService.añadirAFavoritos(1L, 1L);
    });
  }

  @Test
  void testEliminarDeFavoritos() throws NotFoundException {
    testUser.getFavoritos().add(testPark);
    when(usuarioDao.findById(1L)).thenReturn(testUser);
    when(parkDao.findById(1L)).thenReturn(testPark);

    usuarioService.eliminarDeFavoritos(1L, 1L);

    assertFalse(testUser.getFavoritos().contains(testPark));
    verify(usuarioDao, times(1)).update(testUser);
  }

  @Test
  void testGetCurrentUserFavorites() {
    try (MockedStatic<SecurityUtils> utilities = mockStatic(SecurityUtils.class)) {
      testUser.getFavoritos().add(testPark);
      utilities.when(SecurityUtils::getCurrentUserLogin).thenReturn("testuser");
      when(usuarioDao.findByNombreUsuario("testuser")).thenReturn(testUser);

      Collection<ParkDTO> result = usuarioService.getCurrentUserFavorites();

      assertEquals(1, result.size());
      assertEquals("Test Park", result.iterator().next().getName());
    }
  }

  @Test
  void testGetCurrentUserFavoritesWhenNoUser() {
    try (MockedStatic<SecurityUtils> utilities = mockStatic(SecurityUtils.class)) {
      utilities.when(SecurityUtils::getCurrentUserLogin).thenReturn(null);

      Collection<ParkDTO> result = usuarioService.getCurrentUserFavorites();

      assertTrue(result.isEmpty());
    }
  }
}
