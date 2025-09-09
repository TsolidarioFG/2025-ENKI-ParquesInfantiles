package es.udc.tfg.park_rest.model.domain;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Entity
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_generator")
  @SequenceGenerator(name = "review_generator", sequenceName = "review_seq")
  private Long id;
  @Min(1)
  @Max(5)

  private Float calificacion;
  private Boolean approved;
  @Temporal(TemporalType.TIMESTAMP)
  private Date fechaCreacion = new Date();
  @Column(length = 500)
  private String texto;

  @ManyToOne(fetch = FetchType.LAZY)
  private Park park;

  @ManyToOne(fetch = FetchType.LAZY)
  private Usuario usuario;

  public Review() {
    super();
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Review other = (Review) obj;
    return Objects.equals(id, other.id);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Float getCalificacion(){
    return this.calificacion;
  }
  public void setCalificacion(Float calificacion){
    this.calificacion = calificacion;
  }
  public String getTexto(){
    return this.texto;
  }
  public void setTexto(String texto){
    this.texto = texto;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }

  public Park getPark() {
    return park;
  }

  public void setPark(Park park) {
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
