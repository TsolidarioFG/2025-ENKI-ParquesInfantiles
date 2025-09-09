package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Usuario;

import java.util.Collection;
import java.util.List;

public interface UsuarioDao {
  void create(Usuario usuario);
  Collection<Usuario> findAll();
  Usuario findById(Long id);
  Usuario findByNombreUsuario(String nombreUsuario);

  List<Usuario> findUsuariosByFavoritoId(Long platoId);
  void update(Usuario usuario);
}
