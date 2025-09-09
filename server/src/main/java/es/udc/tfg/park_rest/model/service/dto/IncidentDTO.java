package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.Incident;
import es.udc.tfg.park_rest.model.enums.IncidentType;

public class IncidentDTO {

  private Long id;
  private Long parkId;
  private String parkName;
  private IncidentType incidentType;
  private String description;

  public IncidentDTO() {}

  public IncidentDTO(Incident incident) {
    this.id = incident.getId();
    this.parkId = incident.getParkId();
    this.parkName = incident.getParkName();
    this.incidentType = incident.getIncidentType();
    this.description = incident.getDescription();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getParkId() {
    return parkId;
  }

  public void setParkId(Long parkId) {
    this.parkId = parkId;
  }

  public String getParkName() {
    return parkName;
  }

  public void setParkName(String parkName) {
    this.parkName = parkName;
  }

  public IncidentType getIncidentType() {
    return incidentType;
  }

  public void setIncidentType(IncidentType incidentType) {
    this.incidentType = incidentType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
