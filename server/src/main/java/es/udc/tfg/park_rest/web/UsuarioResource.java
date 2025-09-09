package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.service.UsuarioService;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

  @Autowired
  private UsuarioService usuarioService;

  @GetMapping
  public Collection<UsuarioDTOPrivate> findAll() {
    return usuarioService.findAll();
  }

  @PostMapping("/{usuarioId}/favoritos/{parkId}")
  public ResponseEntity<Void> añadirAFavoritos(@PathVariable Long usuarioId, @PathVariable Long parkId) throws NotFoundException {
    usuarioService.añadirAFavoritos(usuarioId, parkId);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{usuarioId}/favoritos/{parkId}")
  public ResponseEntity<Void> eliminarDeFavoritos(@PathVariable Long usuarioId, @PathVariable Long parkId) throws NotFoundException {
    usuarioService.eliminarDeFavoritos(usuarioId, parkId);
    return ResponseEntity.ok().build();
  }
  @GetMapping("/me/favoritos")
  public Collection<ParkDTO> getCurrentUserFavorites() {
    return usuarioService.getCurrentUserFavorites();
  }
}
