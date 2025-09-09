package es.udc.tfg.park_rest.model.repository;

import es.udc.tfg.park_rest.model.domain.Incident;
import es.udc.tfg.park_rest.model.repository.util.GenericDaoJpa;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public class IncidentDaoJpa extends GenericDaoJpa implements IncidentDao {

  @Override
  public void create(Incident incident) {
    entityManager.persist(incident);
  }
  @Override
  public void delete(Incident incident) {
    entityManager.remove(incident);
  }

  @Override
  public void deleteById(Long id) {
    Incident incident = findById(id);
    delete(incident);
  }

  @Override
  public Collection<Incident> findAll() {
    return entityManager.createQuery("from Incident", Incident.class).getResultList();
  }

  @Override
  public Incident findById(Long id) {
    return entityManager.find(Incident.class, id);
  }

  @Override
  public List<Incident> findByParkId(Long parkId) {
    TypedQuery<Incident> query = entityManager.createQuery(
        "from Incident i where i.parkId = :parkId", Incident.class)
      .setParameter("parkId", parkId);
    return query.getResultList();
  }

  @Override
  public void update(Incident incident) {
    entityManager.merge(incident);
  }
}
