package es.udc.tfg.park_rest.model.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class GameElement {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_element_generator")
  @SequenceGenerator(name = "game_element_generator", sequenceName = "game_element_seq")
  private Long id;

  @Column(nullable = false, unique = true)
  private Integer objectId;

  private Integer areaCode;

  private String elementName;

  private String elementType;

  private Boolean swinging;
  private Boolean sliding;
  private Boolean climbing;
  private Boolean balancing;
  private Boolean crawling;
  private Boolean rocking;
  private Boolean jumping;
  private Boolean spinning;
  private Boolean upperBodyStrength;
  private Boolean auditory;
  private Boolean visual;
  private Boolean tactile;
  private Boolean symbolic;
  private Boolean cognitive;
  private Boolean soloPlay;
  private Boolean cooperativePlay;
  private Boolean socialPlay;
  private Boolean parallelPlay;
  private Boolean linearPlay;

  private Integer accessibilityDegree;

  @ManyToOne
  @JoinColumn(name = "park_id")
  private Park park;

  private String image;

  public GameElement() {
    super();
  }

  public GameElement( Integer objectId, Park park,Integer areaCode, String elementName, String elementType, Boolean swinging, Boolean sliding, Boolean climbing, Boolean balancing, Boolean crawling, Boolean rocking, Boolean jumping, Boolean spinning, Boolean upperBodyStrength, Boolean auditory, Boolean visual, Boolean tactile, Boolean symbolic, Boolean cognitive, Boolean soloPlay, Boolean cooperativePlay, Boolean socialPlay, Boolean parallelPlay, Boolean linearPlay, Integer accessibilityDegree, String image) {

    this.objectId = objectId;
    this.park=park;
    this.areaCode = areaCode;
    this.elementName = elementName;
    this.elementType = elementType;
    this.swinging = swinging;
    this.sliding = sliding;
    this.climbing = climbing;
    this.balancing = balancing;
    this.crawling = crawling;
    this.rocking = rocking;
    this.jumping = jumping;
    this.spinning = spinning;
    this.upperBodyStrength = upperBodyStrength;
    this.auditory = auditory;
    this.visual = visual;
    this.tactile = tactile;
    this.symbolic = symbolic;
    this.cognitive = cognitive;
    this.soloPlay = soloPlay;
    this.cooperativePlay = cooperativePlay;
    this.socialPlay = socialPlay;
    this.parallelPlay = parallelPlay;
    this.linearPlay = linearPlay;
    this.accessibilityDegree = accessibilityDegree;
    this.image = image;
  }

// Getters and Setters for all fields

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getObjectId() {
    return objectId;
  }

  public void setObjectId(Integer objectId) {
    this.objectId = objectId;
  }

  public Integer getAreaCode() {
    return areaCode;
  }

  public void setAreaCode(Integer areaCode) {
    this.areaCode = areaCode;
  }

  public String getElementName() {
    return elementName;
  }

  public void setElementName(String elementName) {
    this.elementName = elementName;
  }

  public String getElementType() {
    return elementType;
  }

  public void setElementType(String elementType) {
    this.elementType = elementType;
  }

  public Boolean getSwinging() {
    return swinging;
  }

  public void setSwinging(Boolean swinging) {
    this.swinging = swinging;
  }

  public Boolean getSliding() {
    return sliding;
  }

  public void setSliding(Boolean sliding) {
    this.sliding = sliding;
  }

  public Boolean getClimbing() {
    return climbing;
  }

  public void setClimbing(Boolean climbing) {
    this.climbing = climbing;
  }

  public Boolean getBalancing() {
    return balancing;
  }

  public void setBalancing(Boolean balancing) {
    this.balancing = balancing;
  }

  public Boolean getCrawling() {
    return crawling;
  }

  public void setCrawling(Boolean crawling) {
    this.crawling = crawling;
  }

  public Boolean getRocking() {
    return rocking;
  }

  public void setRocking(Boolean rocking) {
    this.rocking = rocking;
  }

  public Boolean getJumping() {
    return jumping;
  }

  public void setJumping(Boolean jumping) {
    this.jumping = jumping;
  }

  public Boolean getSpinning() {
    return spinning;
  }

  public void setSpinning(Boolean spinning) {
    this.spinning = spinning;
  }

  public Boolean getUpperBodyStrength() {
    return upperBodyStrength;
  }

  public void setUpperBodyStrength(Boolean upperBodyStrength) {
    this.upperBodyStrength = upperBodyStrength;
  }

  public Boolean getAuditory() {
    return auditory;
  }

  public void setAuditory(Boolean auditory) {
    this.auditory = auditory;
  }

  public Boolean getVisual() {
    return visual;
  }

  public void setVisual(Boolean visual) {
    this.visual = visual;
  }

  public Boolean getTactile() {
    return tactile;
  }

  public void setTactile(Boolean tactile) {
    this.tactile = tactile;
  }

  public Boolean getSymbolic() {
    return symbolic;
  }

  public void setSymbolic(Boolean symbolic) {
    this.symbolic = symbolic;
  }

  public Boolean getCognitive() {
    return cognitive;
  }

  public void setCognitive(Boolean cognitive) {
    this.cognitive = cognitive;
  }

  public Boolean getSoloPlay() {
    return soloPlay;
  }

  public void setSoloPlay(Boolean soloPlay) {
    this.soloPlay = soloPlay;
  }

  public Boolean getCooperativePlay() {
    return cooperativePlay;
  }

  public void setCooperativePlay(Boolean cooperativePlay) {
    this.cooperativePlay = cooperativePlay;
  }

  public Boolean getSocialPlay() {
    return socialPlay;
  }

  public void setSocialPlay(Boolean socialPlay) {
    this.socialPlay = socialPlay;
  }

  public Boolean getParallelPlay() {
    return parallelPlay;
  }

  public void setParallelPlay(Boolean parallelPlay) {
    this.parallelPlay = parallelPlay;
  }

  public Boolean getLinearPlay() {
    return linearPlay;
  }

  public void setLinearPlay(Boolean linearPlay) {
    this.linearPlay = linearPlay;
  }

  public Integer getAccessibilityDegree() {
    return accessibilityDegree;
  }

  public void setAccessibilityDegree(Integer accessibilityDegree) {
    this.accessibilityDegree = accessibilityDegree;
  }

  public Park getPark() {
    return park;
  }

  public void setPark(Park park) {
    this.park = park;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }



  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    GameElement other = (GameElement) obj;
    return Objects.equals(id, other.id);
  }
}
