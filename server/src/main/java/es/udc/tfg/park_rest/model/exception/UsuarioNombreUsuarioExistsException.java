package es.udc.tfg.park_rest.model.exception;

public class UsuarioNombreUsuarioExistsException extends ModelException{
  public UsuarioNombreUsuarioExistsException(String nombreUsuario) {
    super("The user name " + nombreUsuario + " already exists");
  }
}
