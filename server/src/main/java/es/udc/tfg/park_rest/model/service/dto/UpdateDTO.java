package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.Update;
import es.udc.tfg.park_rest.model.domain.Usuario;

import java.util.Date;

public class UpdateDTO {
  private Long id;
  private String title;
  private String description;
  private Date creationDate;
  private Long userId;
  private String park;
  private String userName;


  public UpdateDTO() {super();}
  public UpdateDTO(Update update) {
    this.id = update.getId();
    this.title=update.getTitle();
    this.description = update.getDescription();
    this.creationDate = update.getCreationDate();
    this.userId = update.getUser().getId();
    this.park = update.getPark();
    this.userName=update.getUser().getNombreUsuario();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getPark() {
    return park;
  }

  public void setPark(String park) {
    this.park = park;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }
}
