package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.ModelException;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.GameElementDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.service.dto.GameElementDTO;
import es.udc.tfg.park_rest.model.service.dto.ImageDTO;
import es.udc.tfg.park_rest.model.service.util.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class GameElementService {

  @Autowired
  private GameElementDao gameElementDao;

  @Autowired
  private ParkDao parkDao;
  @Autowired
  private ImageService imageService;

  public Collection<GameElementDTO> findAll() {
    return gameElementDao.findAll().stream()
      .map(GameElementDTO::new)
      .collect(Collectors.toList());
  }

  public GameElementDTO findById(Long id) throws NotFoundException {
    GameElement gameElement = gameElementDao.findById(id);
    if (gameElement == null) {
      throw new NotFoundException(id.toString(), GameElement.class);
    }
    return new GameElementDTO(gameElement);
  }

  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public GameElementDTO create(GameElementDTO gameElementDTO) throws NotFoundException {
    GameElement gameElement = new GameElement(
      gameElementDTO.getObjectId(),
      null,
      gameElementDTO.getAreaCode(),
      gameElementDTO.getElementName(),
      gameElementDTO.getElementType(),
      gameElementDTO.getSwinging(),
      gameElementDTO.getSliding(),
      gameElementDTO.getClimbing(),
      gameElementDTO.getBalancing(),
      gameElementDTO.getCrawling(),
      gameElementDTO.getRocking(),
      gameElementDTO.getJumping(),
      gameElementDTO.getSpinning(),
      gameElementDTO.getUpperBodyStrength(),
      gameElementDTO.getAuditory(),
      gameElementDTO.getVisual(),
      gameElementDTO.getTactile(),
      gameElementDTO.getSymbolic(),
      gameElementDTO.getCooperativePlay(),
      gameElementDTO.getCognitive(),
      gameElementDTO.getSoloPlay(),
      gameElementDTO.getSocialPlay(),
      gameElementDTO.getParallelPlay(),
      gameElementDTO.getLinearPlay(),
      gameElementDTO.getAccessibilityDegree(),
      gameElementDTO.getImage()
    );

    if (gameElementDTO.getParkId() != null && gameElementDTO.getParkId() != null) {
      Park park = parkDao.findById(gameElementDTO.getParkId());
      if (park == null) {
        throw new NotFoundException("Parque con ID " + gameElementDTO.getParkId() + " no encontrado", Park.class);
      }
      gameElement.setPark(park);
    } else {
      throw new IllegalArgumentException("Debe asignarse un parque existente al crear un elemento de juego.");
    }

    gameElementDao.create(gameElement);
    return new GameElementDTO(gameElement);
  }


  @Transactional
  public GameElementDTO update(GameElementDTO gameElementDTO) throws NotFoundException {
    GameElement gameElement = gameElementDao.findById(gameElementDTO.getId());
    if (gameElement == null) {
      throw new NotFoundException(gameElementDTO.getId().toString(), GameElement.class);
    }
    gameElement.setObjectId(gameElementDTO.getObjectId());
    gameElement.setAreaCode(gameElementDTO.getAreaCode());
    gameElement.setElementName(gameElementDTO.getElementName());
    gameElement.setElementType(gameElementDTO.getElementType());
    gameElement.setSwinging(gameElementDTO.getSwinging());
    gameElement.setSliding(gameElementDTO.getSliding());
    gameElement.setClimbing(gameElementDTO.getClimbing());
    gameElement.setBalancing(gameElementDTO.getBalancing());
    gameElement.setCrawling(gameElementDTO.getCrawling());
    gameElement.setRocking(gameElementDTO.getRocking());
    gameElement.setJumping(gameElementDTO.getJumping());
    gameElement.setSpinning(gameElementDTO.getSpinning());
    gameElement.setUpperBodyStrength(gameElementDTO.getUpperBodyStrength());
    gameElement.setAuditory(gameElementDTO.getAuditory());
    gameElement.setVisual(gameElementDTO.getVisual());
    gameElement.setTactile(gameElementDTO.getTactile());
    gameElement.setSymbolic(gameElementDTO.getSymbolic());
    gameElement.setCooperativePlay(gameElementDTO.getCooperativePlay());
    gameElement.setCognitive(gameElementDTO.getCognitive());
    gameElement.setSoloPlay(gameElementDTO.getSoloPlay());
    gameElement.setSocialPlay(gameElementDTO.getSocialPlay());
    gameElement.setParallelPlay(gameElementDTO.getParallelPlay());
    gameElement.setLinearPlay(gameElementDTO.getLinearPlay());
    gameElement.setAccessibilityDegree(gameElementDTO.getAccessibilityDegree());
    gameElement.setImage(gameElementDTO.getImage());

    gameElementDao.update(gameElement);
    return new GameElementDTO(gameElement);
  }
  @Transactional
  public Collection<GameElementDTO> findByParkId(Long parkId) {
    return gameElementDao.findByParkId(parkId)
      .stream()
      .map(GameElementDTO::new)
      .collect(Collectors.toList());
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    GameElement gameElement = gameElementDao.findById(id);
    if (gameElement == null) {
      throw new NotFoundException(id.toString(), GameElement.class);
    }

    gameElementDao.deleteById(id);
  }

  @Transactional(readOnly = false)
  public void saveGameElementImage(Long id, MultipartFile file) throws ModelException {
    GameElement gameElement = gameElementDao.findById(id);
    if (gameElement == null) {
      throw new NotFoundException(id.toString(), GameElement.class);
    }

    if (file.isEmpty()) {
      throw new ModelException("No se ha enviado ninguna imagen");
    }

    String nombreFichero = imageService.saveImage(file, id);
    gameElement.setImage(nombreFichero);
    gameElementDao.update(gameElement);
  }

  public ImageDTO getGameElementImage(Long id) throws ModelException {
    GameElement gameElement = gameElementDao.findById(id);
    if (gameElement == null) {
      throw new NotFoundException(id.toString(), GameElement.class);
    }

    return imageService.getImage(id, gameElement.getImage());
  }

}
