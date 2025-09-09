package es.udc.tfg.park_rest.model.domain;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
public class Update {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "update_generator")
  @SequenceGenerator(name = "update_generator", sequenceName = "update_seq")
  private Long id;;
  @Column(nullable = false, length = 50)
  private String title;
  @Column(nullable = false, length = 1000)
  private String description;
  @Temporal(TemporalType.TIMESTAMP)
  private Date creationDate = new Date();
  @ManyToOne(fetch = FetchType.LAZY)
  private Usuario user;
  @Column(nullable = false)
  private String park;

  public Update() {
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
    Update other = (Update) obj;
    return Objects.equals(id, other.id);
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

  public Usuario getUser() {
    return user;
  }

  public void setUser(Usuario user) {
    this.user = user;
  }

  public String getPark() {
    return park;
  }

  public void setPark(String park) {
    this.park = park;
  }
}
