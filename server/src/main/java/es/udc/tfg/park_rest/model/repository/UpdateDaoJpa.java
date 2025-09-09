package es.udc.tfg.park_rest.model.repository;


import es.udc.tfg.park_rest.model.domain.Update;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UpdateDaoJpa extends GenericDaoJpa implements UpdateDao {

  @Override
  public void create(Update update) {
    entityManager.persist(update);
  }
  @Override
  public void delete(Update update) {
    entityManager.remove(update);
  }

  @Override
  public void deleteupdates(List<Update> updates) {
    for (Update update : updates) {
      if (entityManager.contains(update)) {
        entityManager.remove(update);
      } else {
        entityManager.remove(entityManager.merge(update));
      }
    }
  }

  @Override
  public List<Update> findAll() {
    return entityManager.createQuery("from Update", Update.class).getResultList();
  }

  @Override
  public Update findById(Long id) {
    return entityManager.find(Update.class, id);
  }

  @Override
  public List<Update> findByParkId(Long parkId) {
    return entityManager.createQuery(
        "SELECT r FROM Update r WHERE r.park.id = :parkId", Update.class)
      .setParameter("parkId", parkId)
      .getResultList();
  }
  @Override
  public void deleteById(Long id) {
    Update update = findById(id);
    delete(update);

  }

  @Override
  public void update(Update update) {
    entityManager.merge(update);
  }
}
