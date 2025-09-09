package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.*;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.IncidentDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.service.dto.IncidentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class IncidentService {

  @Autowired
  private IncidentDao incidentDao;
  @Autowired
  private ParkDao parkDao;

  public Collection<IncidentDTO> findAll() {
    return incidentDao.findAll().stream()
      .map(IncidentDTO::new)
      .collect(Collectors.toList());
  }

  public IncidentDTO findById(Long id) throws NotFoundException {
    Incident incident = incidentDao.findById(id);
    if (incident == null) {
      throw new NotFoundException(id.toString(), Incident.class);
    }
    return new IncidentDTO(incident);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public IncidentDTO create(IncidentDTO incidentDTO) {
    Incident incident = new Incident();

    incident.setParkId(incidentDTO.getParkId());
    incident.setParkName(incidentDTO.getParkName());
    incident.setIncidentType(incidentDTO.getIncidentType());
    incident.setDescription(incidentDTO.getDescription());

    incidentDao.create(incident);

    return new IncidentDTO(incident);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public IncidentDTO update(IncidentDTO incidentDTO) throws NotFoundException {
    Incident incident = incidentDao.findById(incidentDTO.getId());
    if (incident == null) {
      throw new NotFoundException(incidentDTO.getId().toString(), Incident.class);
    }

    incident.setParkId(incidentDTO.getParkId());
    incident.setParkName(incidentDTO.getParkName());
    incident.setIncidentType(incidentDTO.getIncidentType());
    incident.setDescription(incidentDTO.getDescription());

    incidentDao.update(incident);

    return new IncidentDTO(incident);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Incident incident = incidentDao.findById(id);
    if (incident == null) {
      throw new NotFoundException(id.toString(), Park.class);
    }

    incidentDao.deleteById(id);
  }
  @Transactional
  public List<IncidentDTO> findIncidentsByParkId(Long parkId) throws NotFoundException {
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException("Park con ID " + parkId + " no encontrado.", Park.class);
    }

    List<Incident> incidents = incidentDao.findByParkId(parkId);
    return incidents.stream().map(IncidentDTO::new).collect(Collectors.toList());
  }
}
