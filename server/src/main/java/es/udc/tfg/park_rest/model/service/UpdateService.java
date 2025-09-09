package es.udc.tfg.park_rest.model.service;
import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Update;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.UpdateDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ParkDTO;
import es.udc.tfg.park_rest.model.service.dto.UpdateDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UpdateService {

  @Autowired
  private UpdateDao updateDao;

  @Autowired
  private ParkDao parkDao;

  @Autowired
  private UsuarioDao usuarioDao;

  public Collection<UpdateDTO> findAll() {
    return updateDao.findAll().stream()
      .map(UpdateDTO::new)
      .collect(Collectors.toList());
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public UpdateDTO create(UpdateDTO updateDTO) throws NotFoundException {
    // Verificar que el usuario existe
    Usuario user = usuarioDao.findById(updateDTO.getUserId());
    if (user == null) {
      throw new NotFoundException("Usuario con ID " + updateDTO.getUserId() + " no encontrado.", Usuario.class);
    }

    // Verificar que el parque existe (si es necesario)
    // Park park = parkDao.findByName(updateDTO.getPark());

    Update update = new Update();
    update.setTitle(updateDTO.getTitle());
    update.setDescription(updateDTO.getDescription());
    update.setPark(updateDTO.getPark());
    update.setUser(user);


    updateDao.create(update);

    return new UpdateDTO(update);
  }

  public List<UpdateDTO> getUpdatesByPark(Long parkId) throws NotFoundException {
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException("Parque con ID " + parkId + " no encontrado.", Park.class);
    }

    return updateDao.findByParkId(parkId).stream()
      .map(UpdateDTO::new)
      .collect(Collectors.toList());
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Update update = updateDao.findById(id);
    if (update == null) {
      throw new NotFoundException(id.toString(), Update.class);
    }
    updateDao.deleteById(id);
  }

  @Transactional
  public UpdateDTO update(UpdateDTO updateDTO) throws NotFoundException {
    Update update = updateDao.findById(updateDTO.getId());
    if (update == null) {
      throw new NotFoundException(updateDTO.getId().toString(), Update.class);
    }
    update.setTitle(updateDTO.getTitle());
    update.setDescription(updateDTO.getDescription());
    update.setPark(updateDTO.getPark());
    update.setCreationDate(new Date());

    updateDao.update(update);
    return new UpdateDTO(update);
  }
}
