package es.udc.tfg.park_rest.model.service.dto;


import es.udc.tfg.park_rest.model.domain.Review;

import java.util.Date;

public class ReviewDTO {
  private Long id;
  private Float calificacion;
  private String texto;
  private UsuarioDTOPrivate usuario;
  private String park;
  private Boolean approved;
  private Date fechaCreacion;

  public ReviewDTO() {
    super();
  }

  public ReviewDTO(Review review) {
    this.id = review.getId();
    this.calificacion = review.getCalificacion();
    this.texto = review.getTexto();
    this.usuario =new UsuarioDTOPrivate(review.getUsuario());
    this.park = review.getPark().getName();
    this.approved = review.getApproved();
    this.fechaCreacion = review.getFechaCreacion();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getCalificacion() {
    return calificacion;
  }

  public void setCalificacion(Float calificacion) {
    this.calificacion = calificacion;
  }

  public String getTexto() {
    return texto;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }

  public UsuarioDTOPrivate getUsuario() {
    return usuario;
  }

  public void setUsuario(UsuarioDTOPrivate usuario) {
    this.usuario = usuario;
  }

  public String getPark() {
    return park;
  }

  public void setPark(String park) {
    this.park = park;
  }

  public Boolean getApproved() {
    return approved;
  }

  public void setApproved(Boolean approved) {
    this.approved = approved;
  }

  public Date getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(Date fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }
}
