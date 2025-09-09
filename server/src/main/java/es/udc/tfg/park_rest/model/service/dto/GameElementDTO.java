package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;

public class GameElementDTO {

  private Long id;
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
  private String image;
  private Long parkId;

  private Boolean hasImage = false;// Represented as a String

  public GameElementDTO() {}

  public GameElementDTO(GameElement gameElement) {
    this.id = gameElement.getId();
    this.parkId = gameElement.getPark() != null ? gameElement.getPark().getId() : null;
    this.objectId = gameElement.getObjectId();
    this.areaCode = gameElement.getAreaCode();
    this.elementName = gameElement.getElementName();
    this.elementType = gameElement.getElementType();
    this.swinging = gameElement.getSwinging();
    this.sliding = gameElement.getSliding();
    this.climbing = gameElement.getClimbing();
    this.balancing = gameElement.getBalancing();
    this.crawling = gameElement.getCrawling();
    this.rocking = gameElement.getRocking();
    this.jumping = gameElement.getJumping();
    this.spinning = gameElement.getSpinning();
    this.upperBodyStrength = gameElement.getUpperBodyStrength();
    this.auditory = gameElement.getAuditory();
    this.visual = gameElement.getVisual();
    this.tactile = gameElement.getTactile();
    this.symbolic = gameElement.getSymbolic();
    this.cognitive = gameElement.getCognitive();
    this.soloPlay = gameElement.getSoloPlay();
    this.cooperativePlay = gameElement.getCooperativePlay();
    this.socialPlay = gameElement.getSocialPlay();
    this.parallelPlay = gameElement.getParallelPlay();
    this.linearPlay = gameElement.getLinearPlay();
    this.accessibilityDegree = gameElement.getAccessibilityDegree();
    this.image=gameElement.getImage();
    if (gameElement.getImage() != null) {
      this.hasImage = true;
    }
  }




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

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Long getParkId() {
    return parkId;
  }

  public void setParkId(Long parkId) {
    this.parkId = parkId;
  }

  public Boolean getHasImage() {
    return hasImage;
  }

  public void setHasImage(Boolean hasImage) {
    this.hasImage = hasImage;
  }
}
