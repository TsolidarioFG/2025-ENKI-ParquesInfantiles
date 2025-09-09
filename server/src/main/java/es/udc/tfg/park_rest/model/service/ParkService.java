package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.*;
import es.udc.tfg.park_rest.model.enums.*;
import es.udc.tfg.park_rest.model.exception.ModelException;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.GameElementDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.GameElementDTO;
import es.udc.tfg.park_rest.model.service.dto.ImageDTO;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.util.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class ParkService {

  @Autowired
  private ParkDao parkDao;
  @Autowired
  private UsuarioDao usuarioDao;

  @Autowired
  private GameElementDao gameElementDao;
  @Autowired
  private ImageService imageService;


  public Collection<ParkDTO> findAll() {
    return parkDao.findAll().stream()
      .map(ParkDTO::new)
      .collect(Collectors.toList());
  }

  public ParkDTO findById(Long id) throws NotFoundException {
    Park park = parkDao.findById(id);
    if (park == null) {
      throw new NotFoundException(id.toString(), Park.class);
    }
    return new ParkDTO(park);
  }
  public Collection<ParkDTO> findByName(String name) {
    return parkDao.findByNameContaining(name).stream()
      .map(ParkDTO::new)
      .collect(Collectors.toList());
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public ParkDTO create(ParkDTO parkDTO) {
    Park park = new Park(
      parkDTO.getParkId(),
      parkDTO.getVisitYear(),
      parkDTO.getName(),
      parkDTO.getAddress(),
      parkDTO.getPostalCode(),
      parkDTO.getAreaUbi(),
      parkDTO.getType(),
      parkDTO.getCierrmt(),
      parkDTO.getCierrmd(),
      parkDTO.getPavimtp(),
      parkDTO.getSignMaterial(),
      parkDTO.getAgeSeparation(),
      parkDTO.getAgeRange(),
      parkDTO.getNumElements(),
      parkDTO.getNumUsers(),
      parkDTO.getAccessibility(),
      parkDTO.getWidth(),
      parkDTO.getFreeTurningSpace(),
      parkDTO.getParking(),
      parkDTO.getPlazaPMR(),
      parkDTO.getBus(),
      parkDTO.getPedestrian(),
      parkDTO.getBioArea(),
      parkDTO.getDegree1(),
      parkDTO.getDegree2(),
      parkDTO.getDegree3(),
      parkDTO.getGeneralAccessibility(),
      parkDTO.getAccessibleFountain(),
      parkDTO.getImageSet(),
      parkDTO.getLatitude(),
      parkDTO.getLongitude()
    );

    // Asignar elementos de juego si existen
    if (parkDTO.getGameElements() != null) {
      parkDTO.setGameElements( parkDTO.getGameElements());
    }

    parkDao.create(park);
    return parkDTO;  // Retornamos el mismo DTO, o el DTO de la entidad si es necesario.
  }

  @Transactional
  public ParkDTO update(ParkDTO parkDTO) throws NotFoundException {
    Park park = parkDao.findById(parkDTO.getId());
    if (park == null) {
      throw new NotFoundException(parkDTO.getId().toString(), Park.class);
    }
    park.setParkId(parkDTO.getParkId());
    park.setName(parkDTO.getName());
    park.setVisitYear(parkDTO.getVisitYear());
    park.setAddress(parkDTO.getAddress());
    park.setPostalCode(parkDTO.getPostalCode());
    park.setAreaUbi(parkDTO.getAreaUbi());
    park.setType(parkDTO.getType());
    park.setCierrmt(parkDTO.getCierrmt());
    park.setCierrmd(parkDTO.getCierrmd());
    park.setPavimtp(parkDTO.getPavimtp());
    park.setSignMaterial(parkDTO.getSignMaterial());
    park.setAgeSeparation(parkDTO.getAgeSeparation());
    park.setAgeRange(parkDTO.getAgeRange());
    park.setNumElements(parkDTO.getNumElements());
    park.setNumUsers(parkDTO.getNumUsers());
    park.setAccessibility(parkDTO.getAccessibility());
    park.setWidth(parkDTO.getWidth());
    park.setFreeTurningSpace(parkDTO.getFreeTurningSpace());
    park.setParking(parkDTO.getParking());
    park.setPlazaPMR(parkDTO.getPlazaPMR());
    park.setBus(parkDTO.getBus());
    park.setPedestrian(parkDTO.getPedestrian());
    park.setBioArea(parkDTO.getBioArea());
    park.setDegree1(parkDTO.getDegree1());
    park.setDegree2(parkDTO.getDegree2());
    park.setDegree3(parkDTO.getDegree3());
    park.setGeneralAccessibility(parkDTO.getGeneralAccessibility());
    park.setAccessibleFountain(parkDTO.getAccessibleFountain());
    park.setImageSet(parkDTO.getImageSet());
    park.setLatitude(parkDTO.getLatitude());
    park.setLongitude(parkDTO.getLongitude());

    parkDao.update(park);
    return new ParkDTO(park);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Park park = parkDao.findById(id);
    if (park == null) {
      throw new NotFoundException(id.toString(), Park.class);
    }

    // First, remove the park from all users' favorites
    List<Usuario> usersWithParkInFavorites = usuarioDao.findUsuariosByFavoritoId(id);
    for (Usuario user : usersWithParkInFavorites) {
      user.getFavoritos().removeIf(p -> p.getId().equals(id));
      usuarioDao.update(user);
    }

    // Then delete associated game elements
    List<GameElement> gameElements = gameElementDao.findByParkId(id);
    gameElementDao.deleteGameElements(gameElements);

    // Finally, delete the park
    parkDao.deleteById(id);
  }

  @Transactional(readOnly = false)
  public void saveParkImage(Long id, MultipartFile file) throws ModelException {
    Park park = parkDao.findById(id);
    if (park == null) {
      throw new NotFoundException(id.toString(), Park.class);
    }

    if (file.isEmpty()) {
      throw new ModelException("No se ha enviado ninguna imagen");
    }

    String nombreFichero = imageService.saveImage(file, id);
    park.setImageSet(nombreFichero);
    parkDao.update(park);
  }

  public ImageDTO getParkImage(Long id) throws ModelException {
    Park park = parkDao.findById(id);
    if (park == null) {
      throw new NotFoundException(id.toString(), Park.class);
    }

    return imageService.getImage(id, park.getImageSet());
  }

}
