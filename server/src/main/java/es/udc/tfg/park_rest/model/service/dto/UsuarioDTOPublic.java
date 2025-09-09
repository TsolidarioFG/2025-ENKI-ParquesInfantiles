package es.udc.tfg.park_rest.model.service.dto;

import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.enums.UserType;

public class UsuarioDTOPublic {
  private Long id;
  private String nombreUsuario;
  private String nombre;
  private String apellido;
  private String telefono;
  private String email;
  private UserType userType;

  public UsuarioDTOPublic() {
  }

  public UsuarioDTOPublic(Usuario usuario) {
    this.id = usuario.getId();
    this.nombreUsuario = usuario.getNombreUsuario();
    this.nombre = usuario.getNombre();
    this.apellido = usuario.getApellido();
    this.telefono = usuario.getTelefono();
    this.email=usuario.getEmail();
    this.userType=usuario.getUserType();
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

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
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
