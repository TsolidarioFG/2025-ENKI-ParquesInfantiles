package es.udc.tfg.park_rest.model.service;

import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.domain.Review;
import es.udc.tfg.park_rest.model.domain.UserAuthority;
import es.udc.tfg.park_rest.model.domain.Usuario;
import es.udc.tfg.park_rest.model.exception.NotFoundException;
import es.udc.tfg.park_rest.model.exception.OperationNotAllowed;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import es.udc.tfg.park_rest.model.repository.ReviewDao;
import es.udc.tfg.park_rest.model.repository.UsuarioDao;
import es.udc.tfg.park_rest.model.service.dto.ReviewDTO;
import es.udc.tfg.park_rest.model.service.dto.UsuarioDTOPrivate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

  @Mock
  private ReviewDao reviewDao;

  @Mock
  private ParkDao parkDao;

  @Mock
  private UsuarioDao usuarioDao;

  @InjectMocks
  private ReviewService reviewService;

  @Test
  public void testFindAll() {
    // Create park
    Park park = new Park();
    park.setId(1L);
    park.setName("Test Park");  // Set park name

    // Create user
    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER);

    // Create review
    Review review = new Review();
    review.setId(1L);
    review.setUsuario(user);
    review.setPark(park);  // Set the park

    when(reviewDao.findAll()).thenReturn(List.of(review));

    // Call once and store result
    Collection<ReviewDTO> result = reviewService.findAll();

    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    verify(reviewDao, times(1)).findAll();
  }
  @Test
  public void testCreateReview() throws NotFoundException {
    Park park = new Park();
    park.setId(1L);

    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER); // Add authority

    ReviewDTO dto = new ReviewDTO();
    dto.setTexto("Great park!");

    UsuarioDTOPrivate userDto = new UsuarioDTOPrivate(user);

    when(parkDao.findById(1L)).thenReturn(park);
    when(usuarioDao.findById(1L)).thenReturn(user);
    doAnswer(invocation -> {
      Review review = invocation.getArgument(0);
      review.setId(1L);
      review.setUsuario(user); // Set the user
      return null;
    }).when(reviewDao).create(any(Review.class));

    ReviewDTO result = reviewService.creteReview(1L, dto, userDto);
    assertNotNull(result);
    assertEquals("Great park!", result.getTexto());
    verify(parkDao).findById(1L);
    verify(usuarioDao).findById(1L);
    verify(reviewDao).create(any(Review.class));
  }@Test
  public void testGetReviewsByPark() throws NotFoundException {
    // Create park
    Park park = new Park();
    park.setId(1L);
    park.setName("Test Park");  // Set park name

    // Create user
    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER);

    // Create review
    Review review = new Review();
    review.setId(1L);
    review.setUsuario(user);
    review.setPark(park);  // Set the park

    when(parkDao.findById(1L)).thenReturn(park);
    when(reviewDao.findByParkId(1L)).thenReturn(List.of(review));

    List<ReviewDTO> results = reviewService.getReviewsByPark(1L);
    assertFalse(results.isEmpty());
    assertEquals(1, results.size());
    verify(parkDao).findById(1L);
    verify(reviewDao).findByParkId(1L);
  }
  @Test
  public void testDeleteById() throws NotFoundException, OperationNotAllowed {
    Review review = new Review();
    review.setId(1L);
    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER); // Add authority
    review.setUsuario(user); // Set the user

    when(reviewDao.findById(1L)).thenReturn(review);
    doNothing().when(reviewDao).deleteById(1L);

    assertDoesNotThrow(() -> reviewService.deleteById(1L));
    verify(reviewDao).findById(1L);
    verify(reviewDao).deleteById(1L);
  }
  @Test
  public void testAcceptReview() throws NotFoundException {
    Review review = new Review();
    review.setId(1L);
    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER); // Add authority
    review.setUsuario(user); // Set the user

    when(reviewDao.findById(1L)).thenReturn(review);
    doNothing().when(reviewDao).update(any(Review.class));

    assertDoesNotThrow(() -> reviewService.acceptReview(1L));
    assertTrue(review.getApproved());
    verify(reviewDao).findById(1L);
    verify(reviewDao).update(any(Review.class));
  }

  @Test
  public void testRejectReview() throws NotFoundException {
    Review review = new Review();
    review.setId(1L);
    Usuario user = new Usuario();
    user.setId(1L);
    user.setAuthority(UserAuthority.USER); // Add authority
    review.setUsuario(user); // Set the user

    when(reviewDao.findById(1L)).thenReturn(review);
    doNothing().when(reviewDao).update(any(Review.class));

    assertDoesNotThrow(() -> reviewService.rejectReview(1L));
    assertFalse(review.getApproved());
    verify(reviewDao).findById(1L);
    verify(reviewDao).update(any(Review.class));
  }
}
