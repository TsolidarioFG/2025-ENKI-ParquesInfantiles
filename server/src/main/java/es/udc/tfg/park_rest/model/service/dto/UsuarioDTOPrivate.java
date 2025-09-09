package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.enums.UserType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDTOPrivate {
  private Long id;

  @NotEmpty
  @Size(min = 4)
  private String nombreUsuario;

  @NotEmpty
  private String nombre;

  @NotEmpty
  private String apellido;

  @NotEmpty
  @Size(min = 8)
  private String contraseña;

  private String telefono;

  private String authority;
  private String email;
  private List<Long> favoritos;
  private UserType userType;

  public UsuarioDTOPrivate() {
  }

  public UsuarioDTOPrivate(Usuario usuario) {
    this.id = usuario.getId();
    this.nombreUsuario = usuario.getNombreUsuario();
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.telefono = usuario.getTelefono();
    this.authority = usuario.getAuthority().name();
    this.email=usuario.getEmail();
    this.userType=usuario.getUserType();
    this.favoritos = usuario.getFavoritos().stream()
      .map(Park::getId)
      .collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombreUsuario() {
    return nombreUsuario;
  }

  public void setNombreUsuario(String nombreUsuario) {
    this.nombreUsuario = nombreUsuario;
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

  public String getContraseña() {
    return contraseña;
  }

  public void setContraseña(String contraseña) {
    this.contraseña = contraseña;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getAuthority() {
    return authority;
  }

  public void setAuthority(String authority) {
    this.authority = authority;
  }

  public List<Long> getFavoritos() {
    return favoritos;
  }

  public void setFavoritos(List<Long> favoritos) {
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
