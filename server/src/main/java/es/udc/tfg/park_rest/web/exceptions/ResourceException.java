package es.udc.tfg.park_rest.web.exceptions;

public class ResourceException extends Exception {
  public ResourceException(String errorMsg) {
    super(errorMsg);
  }
}
