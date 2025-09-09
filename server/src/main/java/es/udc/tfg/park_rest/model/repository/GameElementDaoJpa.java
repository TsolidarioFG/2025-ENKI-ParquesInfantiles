package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class GameElementDaoJpa extends GenericDaoJpa implements GameElementDao {

  @Override
  public void create(GameElement gameElement) {
    entityManager.persist(gameElement);
  }
  @Override
  public void delete(GameElement gameElement) {
    entityManager.remove(gameElement);
  }

  @Override
  public void deleteGameElements(List<GameElement> gameElements) {
    for (GameElement gameElement : gameElements) {
      if (entityManager.contains(gameElement)) {
        entityManager.remove(gameElement);
      } else {
        entityManager.remove(entityManager.merge(gameElement));
      }
    }
  }

  @Override
  public void deleteById(Long id) {
    GameElement gameElement = findById(id);
    delete(gameElement);
  }

  @Override
  public Collection<GameElement> findAll() {
    return entityManager.createQuery("from GameElement", GameElement.class).getResultList();
  }

  @Override
  public GameElement findById(Long id) {
    return entityManager.find(GameElement.class, id);
  }

  @Override
  public List<GameElement> findByParkId(Long parkId) {
    TypedQuery<GameElement> query = entityManager.createQuery(
      "SELECT g FROM GameElement g WHERE g.park.id = :parkId", GameElement.class);
    query.setParameter("parkId", parkId);
    return query.getResultList();
  }



  @Override
  public void update(GameElement gameElement) {
    entityManager.merge(gameElement);
  }
}
