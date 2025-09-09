package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Review;

import java.util.List;

public interface ReviewDao {

  void create(Review review);
  void delete(Review review);
  void deleteReviews(List<Review> reviews);

  void deleteById(Long id);
  List<Review> findAll();

  Review findById(Long id);
  List<Review> findByParkId(Long parkId);

  void update(Review review);

}
