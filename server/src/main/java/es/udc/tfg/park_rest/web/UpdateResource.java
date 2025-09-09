package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Update;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.service.UpdateService;
import es.udc.tfg.park_rest.model.service.dto.IncidentDTO;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.dto.UpdateDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import es.udc.tfg.park_rest.web.exceptions.IdAndBodyNotMatchingOnUpdateException;
import es.udc.tfg.park_rest.web.exceptions.RequestBodyNotValidException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/updates")
public class UpdateResource {

  @Autowired
  private UpdateService updateService;

  @GetMapping
  public Collection<UpdateDTO> findAll() {
    return updateService.findAll();
  }

  @GetMapping("/park/{parkId}")
  public List<UpdateDTO> getParkUpdates(@PathVariable Long parkId) throws NotFoundException {
    return updateService.getUpdatesByPark(parkId);
  }

  @PostMapping
  public UpdateDTO create(@RequestBody @Valid UpdateDTO update, Errors errors)
    throws RequestBodyNotValidException, NotFoundException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return updateService.create(update);
  }
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    updateService.deleteById(id);
  }
  @PutMapping("/{id}")
  public UpdateDTO update(@PathVariable Long id, @RequestBody @Valid UpdateDTO update, Errors errors)
    throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException, NotFoundException,
    OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    if (id != update.getId()) {
      throw new IdAndBodyNotMatchingOnUpdateException(Update.class);
    }

    return updateService.update(update);
  }

}
