package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.enums.*;

import java.util.Set;
import java.util.stream.Collectors;

public class ParkDTO {

  private Long id;
  private Integer parkId;
  private Integer visitYear;
  private String name;
  private String address;
  private String postalCode;
  private AreaUbi areaUbi;
  private ParkType type;
  private Cierrmt cierrmt;
  private Cierrmd cierrmd;
  private Pavimtp pavimtp;
  private SignMaterial signMaterial;
  private Boolean ageSeparation;
  private String ageRange;
  private Integer numElements;
  private Integer numUsers;
  private Accessibility accessibility;
  private Double width;
  private Double freeTurningSpace;
  private String parking;
  private Boolean plazaPMR;
  private Boolean bus;
  private Boolean pedestrian;
  private Boolean bioArea;
  private Integer degree1;

  private Integer degree2;

  private Integer degree3;

  private Boolean generalAccessibility;

  private Boolean accessibleFountain;
  private Set<GameElementDTO> gameElements;
  private String imageSet;

  private Boolean hasImages = false;// Represented as a String
  private Float latitude;
  private Float longitude;

  public ParkDTO() {}

  public ParkDTO(Park park) {
    this.id = park.getId();
    this.parkId = park.getParkId();
    this.name = park.getName();
    this.address = park.getAddress();
    this.postalCode = park.getPostalCode();
    this.areaUbi = park.getAreaUbi() != null ? park.getAreaUbi(): null;
    this.type = park.getType() != null ? park.getType() : null;
    this.cierrmt = park.getCierrmt() != null ? park.getCierrmt(): null;
    this.cierrmd = park.getCierrmd() != null ? park.getCierrmd() : null;
    this.pavimtp = park.getPavimtp() != null ? park.getPavimtp() : null;
    this.signMaterial = park.getSignMaterial() != null ? park.getSignMaterial() : null;
    this.ageSeparation = park.getAgeSeparation();
    this.ageRange = park.getAgeRange();
    this.numElements = park.getNumElements();
    this.numUsers = park.getNumUsers();
    this.accessibility = park.getAccessibility();
    this.width = park.getWidth();
    this.freeTurningSpace = park.getFreeTurningSpace();
    this.parking = park.getParking();
    this.plazaPMR = park.getPlazaPMR();
    this.bus = park.getBus();
    this.pedestrian = park.getPedestrian();
    this.bioArea = park.getBioArea();
    this.imageSet=park.getImageSet();
    if (park.getImageSet() != null) {
      this.hasImages = true;
    }
    this.visitYear=park.getVisitYear();
    this.degree1=park.getDegree1();
    this.degree2=park.getDegree2();
    this.degree3=park.getDegree3();
    this.generalAccessibility=park.getGeneralAccessibility();
    this.accessibleFountain=park.getAccessibleFountain();
    this.gameElements = park.getGameElements().stream().map(GameElementDTO::new).collect(Collectors.toSet());
    this.latitude=park.getLatitude();
    this.longitude=park.getLongitude();
  }

  public Integer getVisitYear() {
    return visitYear;
  }

  public void setVisitYear(Integer visitYear) {
    this.visitYear = visitYear;
  }

  public Integer getDegree1() {
    return degree1;
  }

  public void setDegree1(Integer degree1) {
    this.degree1 = degree1;
  }

  public Integer getDegree2() {
    return degree2;
  }

  public void setDegree2(Integer degree2) {
    this.degree2 = degree2;
  }

  public Integer getDegree3() {
    return degree3;
  }

  public void setDegree3(Integer degree3) {
    this.degree3 = degree3;
  }

  public Boolean getGeneralAccessibility() {
    return generalAccessibility;
  }

  public void setGeneralAccessibility(Boolean generalAccessibility) {
    this.generalAccessibility = generalAccessibility;
  }

  public Boolean getAccessibleFountain() {
    return accessibleFountain;
  }

  public void setAccessibleFountain(Boolean accessibleFountain) {
    this.accessibleFountain = accessibleFountain;
  }

  public String getImageSet() {
    return imageSet;
  }

  public void setImageSet(String imageSet) {
    this.imageSet = imageSet;
  }

  public Boolean getHasImages() {
    return hasImages;
  }

  public void setHasImages(Boolean hasImages) {
    this.hasImages = hasImages;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getParkId() {
    return parkId;
  }

  public void setParkId(Integer parkId) {
    this.parkId = parkId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public AreaUbi getAreaUbi() {
    return areaUbi;
  }

  public void setAreaUbi(AreaUbi areaUbi) {
    this.areaUbi = areaUbi;
  }

  public ParkType getType() {
    return type;
  }

  public void setType(ParkType type) {
    this.type = type;
  }

  public Cierrmt getCierrmt() {
    return cierrmt;
  }

  public void setCierrmt(Cierrmt cierrmt) {
    this.cierrmt = cierrmt;
  }

  public Cierrmd getCierrmd() {
    return cierrmd;
  }

  public void setCierrmd(Cierrmd cierrmd) {
    this.cierrmd = cierrmd;
  }

  public Pavimtp getPavimtp() {
    return pavimtp;
  }

  public void setPavimtp(Pavimtp pavimtp) {
    this.pavimtp = pavimtp;
  }

  public SignMaterial getSignMaterial() {
    return signMaterial;
  }

  public void setSignMaterial(SignMaterial signMaterial) {
    this.signMaterial = signMaterial;
  }

  public Boolean getAgeSeparation() {
    return ageSeparation;
  }

  public void setAgeSeparation(Boolean ageSeparation) {
    this.ageSeparation = ageSeparation;
  }

  public String getAgeRange() {
    return ageRange;
  }

  public void setAgeRange(String ageRange) {
    this.ageRange = ageRange;
  }

  public Integer getNumElements() {
    return numElements;
  }

  public void setNumElements(Integer numElements) {
    this.numElements = numElements;
  }

  public Integer getNumUsers() {
    return numUsers;
  }

  public void setNumUsers(Integer numUsers) {
    this.numUsers = numUsers;
  }

  public Accessibility getAccessibility() {
    return accessibility;
  }

  public void setAccessibility(Accessibility accessibility) {
    this.accessibility = accessibility;
  }

  public Double getWidth() {
    return width;
  }

  public void setWidth(Double width) {
    this.width = width;
  }

  public Double getFreeTurningSpace() {
    return freeTurningSpace;
  }

  public void setFreeTurningSpace(Double freeTurningSpace) {
    this.freeTurningSpace = freeTurningSpace;
  }

  public String getParking() {
    return parking;
  }

  public void setParking(String parking) {
    this.parking = parking;
  }

  public Boolean getPlazaPMR() {
    return plazaPMR;
  }

  public void setPlazaPMR(Boolean plazaPMR) {
    this.plazaPMR = plazaPMR;
  }

  public Boolean getBus() {
    return bus;
  }

  public void setBus(Boolean bus) {
    this.bus = bus;
  }

  public Boolean getPedestrian() {
    return pedestrian;
  }

  public void setPedestrian(Boolean pedestrian) {
    this.pedestrian = pedestrian;
  }

  public Boolean getBioArea() {
    return bioArea;
  }

  public void setBioArea(Boolean bioArea) {
    this.bioArea = bioArea;
  }

  public Set<GameElementDTO> getGameElements() {
    return gameElements;
  }

  public void setGameElements(Set<GameElementDTO> gameElements) {
    this.gameElements = gameElements;
  }

  public Float getLatitude() {
    return latitude;
  }

  public void setLatitude(Float latitude) {
    this.latitude = latitude;
  }

  public Float getLongitude() {
    return longitude;
  }

  public void setLongitude(Float longitude) {
    this.longitude = longitude;
  }
}
