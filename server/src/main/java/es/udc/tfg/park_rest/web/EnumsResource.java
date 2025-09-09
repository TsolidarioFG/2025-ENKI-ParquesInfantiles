package es.udc.tfg.park_rest.web;

import es.udc.tfg.park_rest.model.enums.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/enums")
@CrossOrigin(origins = "*")
public class EnumsResource {

  @GetMapping("/areaUbi")
  public List<AreaUbi> getAreaUbiOptions() {
    return Arrays.asList(AreaUbi.values());
  }

  @GetMapping("/cierrmd")
  public List<Cierrmd> getCierrmdOptions() {
    return Arrays.asList(Cierrmd.values());
  }

  @GetMapping("/cierrmt")
  public List<Cierrmt> getCierrmtOptions() {
    return Arrays.asList(Cierrmt.values());
  }

  @GetMapping("/parkType")
  public List<ParkType> getParkTypeOptions() {
    return Arrays.asList(ParkType.values());
  }

  @GetMapping("/pavimtp")
  public List<Pavimtp> getPavimtpOptions() {
    return Arrays.asList(Pavimtp.values());
  }

  @GetMapping("/signMaterial")
  public List<SignMaterial> getSignMaterialOptions() {
    return Arrays.asList(SignMaterial.values());
  }
  @GetMapping("/accessibility")
  public List<Accessibility> getAccessibilityOptions() {
    return Arrays.asList(Accessibility.values());
  }
  @GetMapping("/incidentType")
  public List<IncidentType> getIncidentTypeOptions() {
    return Arrays.asList(IncidentType.values());
  }
}
