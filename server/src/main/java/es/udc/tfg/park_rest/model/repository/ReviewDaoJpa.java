package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Review;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDaoJpa extends GenericDaoJpa implements ReviewDao {

  @Override
  public void create(Review review) {
    entityManager.persist(review);
  }
  @Override
  public void delete(Review review) {
    entityManager.remove(review);
  }
  @Override
  public void deleteReviews(List<Review> resenas) {
    for (Review resena : resenas) {
      if (entityManager.contains(resena)) {
        entityManager.remove(resena);
      } else {
        entityManager.remove(entityManager.merge(resena));
      }
    }
  }

  @Override
  public List<Review> findAll() {
    return entityManager.createQuery("from Review", Review.class).getResultList();
  }

  @Override
  public Review findById(Long id) {
    return entityManager.find(Review.class, id);
  }

  @Override
  public List<Review> findByParkId(Long parkId) {
    return entityManager.createQuery(
        "SELECT r FROM Review r WHERE r.park.id = :parkId", Review.class)
      .setParameter("parkId", parkId)
      .getResultList();
  }
  @Override
  public void deleteById(Long id) {
    Review review = findById(id);
    delete(review);

  }

  @Override
  public void update(Review review) {
    entityManager.merge(review);
  }
}
