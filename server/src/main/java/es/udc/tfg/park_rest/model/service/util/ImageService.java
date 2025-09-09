package es.udc.tfg.park_rest.model.service.util;

import es.udc.tfg.park_rest.model.exception.ModelException;
import es.udc.tfg.park_rest.model.service.dto.ImageDTO;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

  String saveImage(MultipartFile file, Long id) throws ModelException;

  ImageDTO getImage(Long id, String nombreImagen) throws ModelException;

}
