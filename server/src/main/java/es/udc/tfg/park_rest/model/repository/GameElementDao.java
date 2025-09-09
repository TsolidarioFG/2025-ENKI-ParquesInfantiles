package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Review;

import java.util.Collection;
import java.util.List;

public interface GameElementDao {
  void create(GameElement gameElement);
  void delete(GameElement gameElement);
  void deleteGameElements(List<GameElement> gameElements);
  void deleteById(Long id);
  Collection<GameElement> findAll();
  GameElement findById(Long id);
  List<GameElement> findByParkId(Long parkId);
  void update(GameElement gameElement);
}
