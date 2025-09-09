package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class ParkDaoJpa extends GenericDaoJpa implements ParkDao{
  @Override
  public void create(Park park) {
    entityManager.persist(park);
  }
  @Override
  public void delete(Park park) {
    entityManager.remove(park);
  }
  @Override
  public void deleteById(Long id) {
    Park park = findById(id);
    delete(park);
  }
  @Override
  public Park findById(Long id) {
    try {
      return entityManager.createQuery(
          "SELECT p FROM Park p LEFT JOIN FETCH p.gameElements WHERE p.id = :id", Park.class)
        .setParameter("id", id)
        .getSingleResult();
    } catch (NoResultException e) {
      return null; // O puedes lanzar una NotFoundException
    }
  }


  @Override
  public Collection<Park> findAll() {
    return entityManager.createQuery("SELECT DISTINCT p FROM Park p LEFT JOIN FETCH p.gameElements", Park.class)
      .getResultList();
  }
  @Override
  public List<Park> findByNameContaining(String name) {
    String jpql = "SELECT p FROM Park p WHERE LOWER(p.name) LIKE LOWER(:namePattern)";
    TypedQuery<Park> query = entityManager.createQuery(jpql, Park.class);
    query.setParameter("namePattern", "%" + name + "%");
    return query.getResultList();
  }


  @Override
  public void update(Park park) {
    entityManager.merge(park);
  }
}
