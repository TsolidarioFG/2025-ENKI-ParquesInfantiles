package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.exception.ModelException;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.service.ParkService;
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
@RequestMapping("/api/parks")
public class ParkResource {



    @Autowired
    private ParkService parkService;

    @GetMapping
    public Collection<ParkDTO> findAll() {
      return parkService.findAll();
    }
  @GetMapping("/{id}")
  public ParkDTO findById(@PathVariable Long id) throws NotFoundException {
    return parkService.findById(id);
  }

  @GetMapping("/search")
  public Collection<ParkDTO> findByName(@RequestParam String name) {
    return parkService.findByName(name);
  }
  @PostMapping
  public ParkDTO create(@RequestBody @Valid ParkDTO park, Errors errors) throws RequestBodyNotValidException {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return parkService.create(park);
  }
  @PutMapping("/{id}")
  public ParkDTO update(@PathVariable Long id, @RequestBody @Valid ParkDTO park, Errors errors)
    throws RequestBodyNotValidException, IdAndBodyNotMatchingOnUpdateException, NotFoundException,
    OperationNotAllowed {
    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    if (id != park.getId()) {
      throw new IdAndBodyNotMatchingOnUpdateException(Park.class);
    }

    return parkService.update(park);
  }
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    parkService.deleteById(id);
  }
  @GetMapping("/{id}/imagen")
  public void getParkImage(@PathVariable Long id, HttpServletResponse response) throws ModelException {
    ImageDTO imagen = parkService.getParkImage(id);

    try {
      response.setHeader("Content-disposition", "filename=" + imagen.getFilename());
      response.setContentType(imagen.getMimeType());
      IOUtils.copy(imagen.getInputStream(), response.getOutputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @PostMapping("/{id}/imagen")
  public void saveParkImage(@PathVariable Long id, @RequestParam MultipartFile file) throws ModelException {
    parkService.saveParkImage(id, file);
  }
}
