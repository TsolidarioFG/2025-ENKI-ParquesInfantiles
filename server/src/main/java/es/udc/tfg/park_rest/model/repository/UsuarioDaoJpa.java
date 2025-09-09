package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import jakarta.persistence.TypedQuery;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class UsuarioDaoJpa  extends GenericDaoJpa implements UsuarioDao{
  @Override
  public void create(Usuario usuario) {
    entityManager.persist(usuario);
  }


  @Override
  public Collection<Usuario> findAll() {
    return entityManager.createQuery("from Usuario", Usuario.class).getResultList();
  }

  @Override
  public Usuario findById(Long id) {
    return entityManager.find(Usuario.class, id);
  }

  public Usuario findByNombreUsuario(String nombreUsuario) {
    TypedQuery<Usuario> query = entityManager.createQuery("from Usuario u where u.nombreUsuario = :nombreUsuario", Usuario.class)
      .setParameter("nombreUsuario", nombreUsuario);
    return DataAccessUtils.singleResult(query.getResultList());
  }

  @Override
  public List<Usuario> findUsuariosByFavoritoId(Long platoId) {
    return entityManager.createQuery(
        "SELECT u FROM Usuario u JOIN u.favoritos f WHERE f.id = :platoId", Usuario.class)
      .setParameter("platoId", platoId)
      .getResultList();
  }

  @Override
  public void update(Usuario usuario) {
    entityManager.merge(usuario);
  }
}
