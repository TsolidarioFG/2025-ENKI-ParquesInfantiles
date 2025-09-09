package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.service.ReviewService;
import es.udc.tfg.park_rest.model.service.dto.ReviewDTO;
import es.udc.tfg.park_rest.web.exceptions.RequestBodyNotValidException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewResource {

  @Autowired
  private ReviewService reviewService;


  @GetMapping
  public Collection<ReviewDTO> findAll() {
    return reviewService.findAll();
  }

  @GetMapping("/park/{parkId}")
  public List<ReviewDTO> getParkReviews(@PathVariable Long parkId) throws NotFoundException {
    return reviewService.getReviewsByPark(parkId);
  }

  @PostMapping("/park/{parkId}")
  public ReviewDTO creteReview(
    @PathVariable Long parkId,
    @RequestBody @Valid ReviewDTO reviewDTO,
    Errors errors)
    throws RequestBodyNotValidException, NotFoundException {

    if (errors.hasErrors()) {
      throw new RequestBodyNotValidException(errors);
    }

    return reviewService.creteReview(parkId, reviewDTO, reviewDTO.getUsuario());
  }
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) throws NotFoundException, OperationNotAllowed {
    reviewService.deleteById(id);
  }
  @PutMapping("/{id}/accept")
  public void acceptReview(@PathVariable Long id) throws NotFoundException {
    reviewService.acceptReview(id);
  }
  @PutMapping("/{id}/reject")
  public void rejectReview(@PathVariable Long id) throws NotFoundException {
    reviewService.rejectReview(id);
  }

}
