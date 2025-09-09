package es.udc.tfg.park_rest.security;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;

@Component
public class MyUserDetailsService implements UserDetailsService {
  private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

  @Autowired
  private UsuarioDao usuarioDAO;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
    Usuario user = usuarioDAO.findByNombreUsuario(login);
    if (user == null) {
      throw new UsernameNotFoundException("User " + login + " not found");
    }

    logger.info("Loaded user {} with authority {}", login, user.getAuthority().name());
    GrantedAuthority authority = new SimpleGrantedAuthority(user.getAuthority().name());
    return new org.springframework.security.core.userdetails.User(login, user.getContraseña(),
        Collections.singleton(authority));
  }
}
