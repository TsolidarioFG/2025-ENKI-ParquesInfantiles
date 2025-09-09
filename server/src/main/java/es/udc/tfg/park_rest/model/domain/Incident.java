package es.udc.tfg.park_rest.model.domain;

import es.udc.tfg.park_rest.model.enums.IncidentType;
import es.udc.tfg.park_rest.model.enums.Pavimtp;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Incident {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "incident_generator")
  @SequenceGenerator(name = "incident_generator", sequenceName = "incident_seq")
  private Long id;

  @Column(nullable = false)
  private Long parkId;

  @Column(nullable = false)
  private String parkName;

  @Enumerated(EnumType.STRING)
  private IncidentType incidentType; // E.g., Signage, Game Elements, Pavement, Age

  @Column(nullable = false, length = 1000)
  private String description;

  public Incident() {
    super();
  }

  // Getters and Setters

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

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Incident other = (Incident) obj;
    return Objects.equals(id, other.id);
  }
}
