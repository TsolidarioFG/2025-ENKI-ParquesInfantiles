package es.udc.tfg.park_rest.model.domain;

import es.udc.tfg.park_rest.model.enums.*;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Park {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "park_generator")
  @SequenceGenerator(name = "park_generator", sequenceName = "park_seq")
  private Long id;

  @Column(unique = true)
  private Integer parkId;

  private String name;

  private Integer visitYear;

  private String address;

  private String postalCode;

  @Enumerated(EnumType.STRING)
  private AreaUbi areaUbi;

  @Enumerated(EnumType.STRING)
  private ParkType type;

  @Enumerated(EnumType.STRING)
  private Cierrmt cierrmt;

  @Enumerated(EnumType.STRING)
  private Cierrmd cierrmd;

  @Enumerated(EnumType.STRING)
  private Pavimtp pavimtp;

  @Enumerated(EnumType.STRING)
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
  @OneToMany(mappedBy = "park", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<GameElement> gameElements = new HashSet<>();


  private Float latitude;
  private Float longitude;


  private String imageSet;
public Park(){
    super();
  }



  public Park(Integer parkId, Integer visitYear, String name, String address, String postalCode, AreaUbi areaUbi, ParkType type, Cierrmt cierrmt, Cierrmd cierrmd, Pavimtp pavimtp, SignMaterial signMaterial, Boolean ageSeparation, String ageRange, Integer numElements, Integer numUsers, Accessibility accessibility, Double width, Double freeTurningSpace, String parking, Boolean plazaPMR, Boolean bus, Boolean pedestrian, Boolean bioArea, Integer degree1, Integer degree2, Integer degree3, Boolean generalAccessibility, Boolean accessibleFountain, String imageSet, Float latitude, Float longitude) {
    this.parkId = parkId;
    this.name = name;
    this.visitYear = visitYear;
    this.address = address;
    this.postalCode = postalCode;
    this.areaUbi = areaUbi;
    this.type = type;
    this.cierrmt = cierrmt;
    this.cierrmd = cierrmd;
    this.pavimtp = pavimtp;
    this.signMaterial = signMaterial;
    this.ageSeparation = ageSeparation;
    this.ageRange = ageRange;
    this.numElements = numElements;
    this.numUsers = numUsers;
    this.accessibility = accessibility;
    this.width = width;
    this.freeTurningSpace = freeTurningSpace;
    this.parking = parking;
    this.plazaPMR = plazaPMR;
    this.bus = bus;
    this.pedestrian = pedestrian;
    this.bioArea = bioArea;
    this.degree1 = degree1;
    this.degree2 = degree2;
    this.degree3 = degree3;
    this.generalAccessibility = generalAccessibility;
    this.accessibleFountain = accessibleFountain;
    this.gameElements = gameElements;
    this.imageSet = imageSet;
    this.latitude=latitude;
    this.longitude=longitude;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    Park other = (Park) obj;
    return Objects.equals(id, other.id);
  }

  public void addGameElement(GameElement gameElement) {
    gameElements.add(gameElement);
    gameElement.setPark(this);
  }

  public void removeGameElement(GameElement gameElement) {
    gameElements.remove(gameElement);
    gameElement.setPark(null);
  }

  // Example Getter and Setter
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

  public Integer getVisitYear() {
    return visitYear;
  }

  public void setVisitYear(Integer visitYear) {
    this.visitYear = visitYear;
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

  public Set<GameElement> getGameElements() {
    return gameElements;
  }

  public void setGameElements(Set<GameElement> gameElements) {
    this.gameElements = gameElements;
  }


  public String getImageSet() {
    return imageSet;
  }

  public void setImageSet(String imageSet) {
    this.imageSet = imageSet;
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
