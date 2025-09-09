package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.service.IncidentService;
import es.udc.tfg.park_rest.model.service.ParkService;
import es.udc.tfg.park_rest.model.service.dto.IncidentDTO;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.tfg.park_rest.web.exceptions.RequestBodyNotValidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentResource {


  @Autowired
  private IncidentService incidentService;

  @Autowired
  private ParkService parkService;

  @GetMapping
  public Collection<IncidentDTO> findAll() {
    return incidentService.findAll();
  }

  @GetMapping("/{id}")
  public IncidentDTO findById(@PathVariable Long id) throws NotFoundException {
    return incidentService.findById(id);
  }
  @GetMapping("/park/{parkId}")
  public List<IncidentDTO> findIncidentsByParkId(@PathVariable Long parkId) throws NotFoundException {
    return incidentService.findIncidentsByParkId(parkId);
  }

  @PostMapping
  public IncidentDTO create(@RequestBody @Valid IncidentDTO incident, Errors errors) throws RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return incidentService.create(incident);
  }

  @PutMapping("/{id}")
  public IncidentDTO update(@PathVariable Long id, @RequestBody @Valid IncidentDTO incident, Errors errors)
    throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException, NotFoundException,
    OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    if (id != incident.getId()) {
      throw new IdAndBodyNotMatchingOnUpdateException(Park.class);
    }

    return incidentService.update(incident);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    incidentService.deleteById(id);
  }
}
