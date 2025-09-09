package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.UserAuthority;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.UsuarioNombreUsuarioExistsException;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import es.udc.tfg.park_rest.security.SecurityUtils;
import es.udc.tfg.park_rest.model.enums.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true, rollbackFor = Exception.class)
public class UsuarioService {

  @Autowired
  private UsuarioDao usuarioDao;

  @Autowired
  private ParkDao parkDao;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PreAuthorize("hasAuthority('ADMIN')")
  public Collection<UsuarioDTOPrivate> findAll() {
    return usuarioDao.findAll().stream()
      .map(UsuarioDTOPrivate::new)
      .collect(Collectors.toList());
  }



  @Transactional(readOnly = false)
  public void registerUsuario(String nombreUsuario, String contraseña, String nombre, String apellido, String telefono, String email, boolean isAdmin, UserType userType)
    throws UsuarioNombreUsuarioExistsException {
    if (usuarioDao.findByNombreUsuario(nombreUsuario) != null) {
      throw new UsuarioNombreUsuarioExistsException(nombreUsuario);
    }

    Usuario usuario = new Usuario();
    usuario.setNombreUsuario(nombreUsuario);
    usuario.setContraseña(passwordEncoder.encode(contraseña));
    usuario.setNombre(nombre);
    usuario.setApellido(apellido);
    usuario.setTelefono(telefono);
    usuario.setEmail(email);
    usuario.setUserType(userType);
    usuario.setAuthority(UserAuthority.USER);
    if (isAdmin) {
      usuario.setAuthority(UserAuthority.ADMIN);
    }

    usuarioDao.create(usuario);
  }
  @Transactional
  public UsuarioDTOPrivate updateUsuario(String nombre, String apellido, String telefono, String nombreUsuario, String email, UserType userType) {
    Usuario usuario = usuarioDao.findByNombreUsuario(SecurityUtils.getCurrentUserLogin());

    if (usuario == null) {
      throw new RuntimeException("Usuario no encontrado");
    }

    usuario.setNombre(nombre);
    usuario.setApellido(apellido);
    usuario.setTelefono(telefono);
    usuario.setEmail(email);
    usuario.setUserType(userType);


    if (nombreUsuario != null && !nombreUsuario.equals(usuario.getNombreUsuario())) {
      // Verifica que el nombre de usuario no esté ya en uso
      if (usuarioDao.findByNombreUsuario(nombreUsuario) != null) {
        throw new RuntimeException("El nombre de usuario ya está en uso");
      }
      usuario.setNombreUsuario(nombreUsuario);
    }

    usuarioDao.update(usuario);
    return new UsuarioDTOPrivate(usuario);

  }
  public UsuarioDTOPrivate getCurrentUserWithAuthority() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      return new UsuarioDTOPrivate(usuarioDao.findByNombreUsuario(currentUserLogin));
    }
    return null;
  }
  @Transactional
  public UsuarioDTOPrivate updateUsuarioWithPassword(UsuarioDTOPrivate usuarioDTO) {
    Usuario usuario = usuarioDao.findByNombreUsuario(SecurityUtils.getCurrentUserLogin());

    if (usuario == null) {
      throw new RuntimeException("Usuario no encontrado");
    }

    if (usuarioDTO.getContraseña() != null && !usuarioDTO.getContraseña().isEmpty()) {
      usuario.setContraseña(passwordEncoder.encode(usuarioDTO.getContraseña()));
    }

    usuario.setNombre(usuarioDTO.getNombre());
    usuario.setApellido(usuarioDTO.getApellido());
    usuario.setTelefono(usuarioDTO.getTelefono());
    usuario.setEmail(usuarioDTO.getEmail());

    if (usuarioDTO.getNombreUsuario() != null && !usuarioDTO.getNombreUsuario().equals(usuario.getNombreUsuario())) {
      if (usuarioDao.findByNombreUsuario(usuarioDTO.getNombreUsuario()) != null) {
        throw new RuntimeException("El nombre de usuario ya está en uso");
      }
      usuario.setNombreUsuario(usuarioDTO.getNombreUsuario());
    }

    usuarioDao.update(usuario);
    return new UsuarioDTOPrivate(usuario);
  }


  @Transactional(readOnly = false)
  public void añadirAFavoritos(Long usuarioId, Long parkId) throws NotFoundException {
    Usuario usuario = usuarioDao.findById(usuarioId);
    if (usuario == null) {
      throw new NotFoundException(usuarioId.toString(), Usuario.class);
    }
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException(parkId.toString(), Park.class);
    }

    if (!usuario.getFavoritos().contains(park)) {
      usuario.getFavoritos().add(park);
      usuarioDao.update(usuario);
    }
  }

  @Transactional(readOnly = false)
  public void eliminarDeFavoritos(Long usuarioId, Long parkId) throws NotFoundException {
    Usuario usuario = usuarioDao.findById(usuarioId);
    if (usuario == null) {
      throw new NotFoundException(usuarioId.toString(), Usuario.class);
    }
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException(parkId.toString(), Park.class);
    }
    if (usuario.getFavoritos().contains(park)) {
      usuario.getFavoritos().remove(park);
      usuarioDao.update(usuario);
    }
  }
  @Transactional(readOnly = true)
  public Collection<ParkDTO> getCurrentUserFavorites() {
    String currentUserLogin = SecurityUtils.getCurrentUserLogin();
    if (currentUserLogin != null) {
      Usuario usuario = usuarioDao.findByNombreUsuario(currentUserLogin);
      if (usuario != null) {
        return usuario.getFavoritos().stream()
          .map(ParkDTO::new)
          .collect(Collectors.toList());
      }
    }
    return Collections.emptyList();
  }

}
