package es.udc.tfg.park_rest;

import es.udc.tfg.park_rest.model.exception.UsuarioNombreUsuarioExistsException;
import es.udc.tfg.park_rest.config.DatabaseLoader;
import es.udc.tfg.park_rest.model.exception.UserLoginExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class ParkRestApplication {
  private final Logger logger = LoggerFactory.getLogger(ParkRestApplication.class);

  @Autowired
  @Lazy
  private DatabaseLoader databaseLoader;

  public static void main(String[] args) {
    SpringApplication.run(ParkRestApplication.class, args);
  }

  @PostConstruct
  public void init() throws InterruptedException {
    try {
      databaseLoader.loadData();
    } catch (UserLoginExistsException e) {
      logger.error(e.getMessage(), e);
    } catch (UsuarioNombreUsuarioExistsException e) {
        throw new RuntimeException(e);
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }
}
