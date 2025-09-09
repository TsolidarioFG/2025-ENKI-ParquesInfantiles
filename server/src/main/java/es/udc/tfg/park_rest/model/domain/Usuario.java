package es.udc.tfg.park_rest.model.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import es.udc.tfg.park_rest.model.enums.Cierrmd;
import es.udc.tfg.park_rest.model.enums.UserType;
import jakarta.persistence.*;

@Entity
public class Usuario {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_generator")
  @SequenceGenerator(name = "usuario_generator", sequenceName = "usuario_seq")
  private Long id;

  @Column (unique=true)
  private String nombreUsuario;

  private String nombre;

  private String apellido;

  private String contraseña;

  private String telefono;

  private String email;


  @OneToMany(mappedBy = "usuario")
  private List<Review> reviews = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
    name = "usuario_favoritos",
    joinColumns = @JoinColumn(name = "usuario_id"),
    inverseJoinColumns = @JoinColumn(name = "plato_id")
  )
  private List<Park> favoritos = new ArrayList<>();
    @Enumerated(EnumType.STRING)
  private UserAuthority authority;
  @Enumerated(EnumType.STRING)
  private UserType userType;

  public Usuario() {
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
    Usuario other = (Usuario) obj;
    return Objects.equals(id, other.id);
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombreUsuario(){
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario){
    this.nombreUsuario=nombreUsuario;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }
  public String getApellido() {
    return apellido;
  }
  public void setApellido(String apellido) {
    this.apellido = apellido;
  }


  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getContraseña() {
    return contraseña;
  }
  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  public UserAuthority getAuthority() {
    return authority;
  }

  public void setAuthority(UserAuthority authority) {
    this.authority = authority;
  }

  public List<Park> getFavoritos() {
    return favoritos;
  }

  public void setFavoritos(List<Park> favoritos) {
    this.favoritos = favoritos;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public UserType getUserType() {
    return userType;
  }

  public void setUserType(UserType userType) {
    this.userType = userType;
  }
}
