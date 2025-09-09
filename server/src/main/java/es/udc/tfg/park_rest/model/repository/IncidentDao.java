package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Incident;

import java.util.Collection;
import java.util.List;

public interface IncidentDao {
  void create(Incident incident);
  void delete(Incident incident);
  void deleteById(Long id);
  Collection<Incident> findAll();
  Incident findById(Long id);
  List<Incident> findByParkId(Long parkId);
  void update(Incident incident);
}
