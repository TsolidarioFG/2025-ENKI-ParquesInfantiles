package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Review;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.ReviewDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ReviewDTO;
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
public class ReviewService {

  @Autowired
  private ReviewDao reviewDao;

  @Autowired
  private ParkDao parkDao;
  @Autowired
  private UsuarioDao usuarioDao;


  public Collection<ReviewDTO> findAll() {
    return reviewDao.findAll().stream()
      .map(ReviewDTO::new)
      .collect(Collectors.toList());
  }

  @Transactional(readOnly = false)
  public ReviewDTO creteReview(Long parkId, ReviewDTO reviewDTO, UsuarioDTOPrivate usuarioDTO) throws NotFoundException {
    // Buscar el park por ID
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException("Park con ID " + parkId + " no encontrado.", Park.class);
    }
    Usuario usuario = usuarioDao.findById(usuarioDTO.getId());
    if (usuario == null) {
      throw new NotFoundException("Usuario con ID " + usuario.getId() + " no encontrado.", Usuario.class);
    }

    // Crear la nueva review
    Review review = new Review();
    review.setPark(park);
    review.setUsuario(usuario);
    review.setTexto(reviewDTO.getTexto());
    review.setCalificacion(reviewDTO.getCalificacion());
    review.setApproved(null);
    review.setFechaCreacion(new Date());

    // Guardar la review
    reviewDao.create(review);

    // Devolver la review como DTO
    return new ReviewDTO(review);
  }

  public List<ReviewDTO> getReviewsByPark(Long parkId) throws NotFoundException {
    Park park = parkDao.findById(parkId);
    if (park == null) {
      throw new NotFoundException("Park con ID " + parkId + " no encontrado.", Park.class);
    }

    List<Review> reviews = reviewDao.findByParkId(parkId);
    return reviews.stream().map(ReviewDTO::new).collect(Collectors.toList());
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void deleteById(Long id) throws NotFoundException, OperationNotAllowed {
    Review review = reviewDao.findById(id);
    if (review == null) {
      throw new NotFoundException(id.toString(), Review.class);
    }
    reviewDao.deleteById(id);
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void acceptReview(Long id) throws NotFoundException {
    Review review = reviewDao.findById(id);
    if (review == null) {
      throw new NotFoundException(id.toString(), Review.class);
    }
    review.setApproved(true);
    reviewDao.update(review);
  }
  @PreAuthorize("hasAuthority('ADMIN')")
  @Transactional(readOnly = false)
  public void rejectReview(Long id) throws NotFoundException {
    Review review = reviewDao.findById(id);
    if (review == null) {
      throw new NotFoundException(id.toString(), Review.class);
    }
    review.setApproved(false);
    reviewDao.update(review);
  }

}
