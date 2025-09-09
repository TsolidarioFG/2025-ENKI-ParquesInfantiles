package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.ModelException;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.service.GameElementService;
import es.udc.tfg.park_rest.model.service.dto.GameElementDTO;
import es.udc.tfg.park_rest.model.service.dto.ImageDTO;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.tfg.park_rest.web.exceptions.RequestBodyNotValidException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/api/gameElements")
public class GameElementResource {

  @Autowired
  private GameElementService gameElementService;

  @GetMapping("/park/{parkId}")
  public Collection<GameElementDTO> findByPark(@PathVariable Long parkId) {
    return gameElementService.findByParkId(parkId);
  }

  @GetMapping("/{id}")
  public GameElementDTO findById(@PathVariable Long id) throws NotFoundException {
    return gameElementService.findById(id);
  }
  @PostMapping
  public GameElementDTO create(@RequestBody @Valid GameElementDTO gameElement, Errors errors) throws RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return gameElementService.create(gameElement);
  }
  @PutMapping("/{id}")
  public GameElementDTO update(@PathVariable Long id, @RequestBody @Valid GameElementDTO gameElement, Errors errors)
    throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException, NotFoundException,
    OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    if (id != gameElement.getId()) {
      throw new IdAndBodyNotMatchingOnUpdateException(GameElement.class);
    }

    return gameElementService.update(gameElement);
  }
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    gameElementService.deleteById(id);
  }
  @GetMapping("/{id}/imagen")
  public void getGameElementImage(@PathVariable Long id, HttpServletResponse response) throws ModelException {
    ImageDTO imagen = gameElementService.getGameElementImage(id);

    try {
      response.setHeader("Content-disposition", "filename=" + imagen.getFilename());
      response.setContentType(imagen.getMimeType());
      IOUtils.copy(imagen.getInputStream(), response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @PostMapping("/{id}/imagen")
  public void saveGameElementImage(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
    gameElementService.saveGameElementImage(id, file);
  }

}
