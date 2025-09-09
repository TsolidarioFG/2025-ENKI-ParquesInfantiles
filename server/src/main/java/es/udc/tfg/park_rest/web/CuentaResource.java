package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.exception.UsuarioNombreUsuarioExistsException;
import es.udc.tfg.park_rest.model.service.UsuarioService;
import es.udc.tfg.park_rest.security.JWTToken;
import es.udc.tfg.park_rest.security.TokenProvider;
import es.udc.tfg.park_rest.web.exceptions.RequestBodyNotValidException;
import es.udc.tfg.park_rest.model.service.dto.LoginDTO2;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPublic;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cuenta")

public class CuentaResource {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UsuarioService usuarioService;

  @PostMapping("authenticate")
  public JWTToken authenticate(@Valid @RequestBody LoginDTO2 loginDTO2) {
    UsernamePasswordAuthenticationToken authenticationToken =
      new UsernamePasswordAuthenticationToken(loginDTO2.getNombreUsuario(), loginDTO2.getContraseña());
    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = tokenProvider.createToken(authentication);
    return new JWTToken(jwt);
  }
  @GetMapping("/account")
  public UsuarioDTOPrivate getAccount() {
    return usuarioService.getCurrentUserWithAuthority();
  }

  @PostMapping("/register")
  public void registerUsuario(@Valid @RequestBody UsuarioDTOPrivate usuarioDTO)
    throws UsuarioNombreUsuarioExistsException {
    usuarioService.registerUsuario(
      usuarioDTO.getNombreUsuario(),
      usuarioDTO.getContraseña(),
      usuarioDTO.getNombre(),
      usuarioDTO.getApellido(),
      usuarioDTO.getTelefono(),
      usuarioDTO.getEmail(),
      false,
      usuarioDTO.getUserType());
  }


  @PutMapping("/update")
  public UsuarioDTOPrivate updateAccount(@Valid @RequestBody UsuarioDTOPublic usuarioDTO, Errors errors) throws RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return usuarioService.updateUsuario(
      usuarioDTO.getNombre(),
      usuarioDTO.getApellido(),
      usuarioDTO.getTelefono(),
      usuarioDTO.getNombreUsuario(),
      usuarioDTO.getEmail(),
      usuarioDTO.getUserType()
    );

  }
  @PutMapping("/account")
  public UsuarioDTOPrivate updateAccount(@RequestBody @Valid UsuarioDTOPrivate usuarioDTO) {
    return usuarioService.updateUsuarioWithPassword(usuarioDTO);
  }

}

