package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Park;


import java.util.Collection;
import java.util.List;

public interface ParkDao {

  void create(Park park);
  void delete(Park park);
  void deleteById(Long id);
  Collection<Park> findAll();
  Park findById(Long id);
  List<Park> findByNameContaining(String name);
  void update(Park park);
}
