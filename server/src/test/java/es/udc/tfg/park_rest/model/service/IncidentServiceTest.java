package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Incident;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.IncidentDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.service.dto.IncidentDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IncidentServiceTest {

  @Mock
  private IncidentDao incidentDao;

  @Mock
  private ParkDao parkDao;

  @InjectMocks
  private IncidentService incidentService;

  @Test
  public void testFindAll() {
    Incident incident = new Incident();
    incident.setId(1L);
    when(incidentDao.findAll()).thenReturn(List.of(incident));

    assertFalse(incidentService.findAll().isEmpty());  // First call
    assertEquals(1, incidentService.findAll().size()); // Second call
    verify(incidentDao, times(2)).findAll(); // Expects exactly 2 calls
  }

  @Test
  public void testFindById() throws NotFoundException {
    Incident incident = new Incident();
    incident.setId(1L);
    when(incidentDao.findById(1L)).thenReturn(incident);

    IncidentDTO result = incidentService.findById(1L);
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(incidentDao).findById(1L);
  }

  @Test
  public void testCreate() {
    IncidentDTO dto = new IncidentDTO();
    dto.setDescription("Test Incident");

    doAnswer(invocation -> {
      Incident incident = invocation.getArgument(0);
      incident.setId(1L);
      return null;
    }).when(incidentDao).create(any(Incident.class));

    IncidentDTO result = incidentService.create(dto);
    assertNotNull(result);
    assertEquals("Test Incident", result.getDescription());
    verify(incidentDao).create(any(Incident.class));
  }

  @Test
  public void testUpdate() throws NotFoundException {
    Incident existingIncident = new Incident();
    existingIncident.setId(1L);
    existingIncident.setDescription("Old Description");

    IncidentDTO dto = new IncidentDTO();
    dto.setId(1L);
    dto.setDescription("New Description");

    when(incidentDao.findById(1L)).thenReturn(existingIncident);
    doNothing().when(incidentDao).update(any(Incident.class));

    IncidentDTO result = incidentService.update(dto);
    assertNotNull(result);
    assertEquals("New Description", result.getDescription());
    verify(incidentDao).findById(1L);
    verify(incidentDao).update(any(Incident.class));
  }

  @Test
  public void testDeleteById() throws NotFoundException, OperationNotAllowed {
    Incident incident = new Incident();
    incident.setId(1L);

    when(incidentDao.findById(1L)).thenReturn(incident);
    doNothing().when(incidentDao).deleteById(1L);

    assertDoesNotThrow(() -> incidentService.deleteById(1L));
    verify(incidentDao).findById(1L);
    verify(incidentDao).deleteById(1L);
  }

  @Test
  public void testFindIncidentsByParkId() throws NotFoundException {
    Park park = new Park();
    park.setId(1L);
    Incident incident = new Incident();
    incident.setId(1L);

    when(parkDao.findById(1L)).thenReturn(park);
    when(incidentDao.findByParkId(1L)).thenReturn(List.of(incident));

    List<IncidentDTO> results = incidentService.findIncidentsByParkId(1L);
    assertFalse(results.isEmpty());
    assertEquals(1, results.size());
    verify(parkDao).findById(1L);
    verify(incidentDao).findByParkId(1L);
  }
}
