package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Update;

import java.util.List;

public interface UpdateDao {

  void create(Update update);
  void delete(Update update);
  void deleteupdates(List<Update> updates);

  void deleteById(Long id);
  List<Update> findAll();

  Update findById(Long id);
  List<Update> findByParkId(Long parkId);

  void update(Update update);
}
