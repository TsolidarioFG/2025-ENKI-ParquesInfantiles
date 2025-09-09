package es.udc.tfg.park_rest.config;


import es.udc.tfg.park_rest.model.domain.*;
import es.udc.tfg.park_rest.model.enums.*;
import es.udc.tfg.park_rest.model.exception.UsuarioNombreUsuarioExistsException;
import es.udc.tfg.park_rest.model.repository.GameElementDao;
import es.udc.tfg.park_rest.model.repository.IncidentDao;
import es.udc.tfg.park_rest.model.repository.ParkDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.udc.tfg.park_rest.model.exception.UserLoginExistsException;

import es.udc.tfg.park_rest.model.service.UsuarioService;
@Configuration
public class DatabaseLoader {


  @Autowired
  private UsuarioService usuarioService;

  @Autowired
  private ParkDao parkDao;

  @Autowired
  private GameElementDao gameElementDao;

  @Autowired
  private IncidentDao incidentDao;


  @Transactional(readOnly = false, rollbackFor = Exception.class)
  public void loadData() throws UserLoginExistsException, InterruptedException, UsuarioNombreUsuarioExistsException{

    // Creating Park Examples
    Park park1 = new Park();
    park1.setParkId(1);
    park1.setName("Plaza Alfonso XIII");
    park1.setVisitYear(2022);
    park1.setAddress("https://goo.gl/maps/tffw1VG4MsbPcUiq5");
    park1.setPostalCode("15008");
    park1.setAreaUbi(AreaUbi.VIA);
    park1.setType(ParkType.PLAZA);
    park1.setCierrmt(Cierrmt.MAD);
    park1.setCierrmd(Cierrmd.PAR);
    park1.setPavimtp(Pavimtp.CAU);
    park1.setSignMaterial(SignMaterial.MADERA);
    park1.setAgeSeparation(false);
    park1.setAgeRange("2 a 8 años");
    park1.setNumElements(2);
    park1.setNumUsers(6);
    park1.setAccessibility(Accessibility.SIN_OBSTÁCULOS);
    park1.setWidth(80.0);
    park1.setFreeTurningSpace(150.0);
    park1.setParking("No");
    park1.setPlazaPMR(true);
    park1.setBus(true);
    park1.setPedestrian(true);
    park1.setBioArea(false);
    park1.setDegree1(7);
    park1.setDegree2(0);
    park1.setDegree3(0);
    park1.setGeneralAccessibility(false);
    park1.setAccessibleFountain(false);
    park1.setImageSet("1.jpg");
    park1.setLatitude(43.3623F);
    park1.setLongitude(-8.4115F);
    parkDao.create(park1);

    Park park2 = new Park();
    park2.setParkId(2);
    park2.setName("Carlos Casares Caucho");
    park2.setVisitYear(2020);
    park2.setAddress("https://goo.gl/maps/u4nLuRCXNVtsDqUXA");
    park2.setPostalCode("15002");
    park2.setAreaUbi(AreaUbi.VIA);
    park2.setType(ParkType.ESPACIO_VERDE);
    park2.setCierrmt(Cierrmt.SIN);
    park2.setCierrmd(Cierrmd.SIN);
    park2.setPavimtp(Pavimtp.CAU);
    park2.setSignMaterial(SignMaterial.METAL);
    park2.setAgeSeparation(false);
    park2.setAgeRange("0 a 6 años");
    park2.setNumElements(2);
    park2.setNumUsers(14);
    park2.setAccessibility(Accessibility.SIN_OBSTÁCULOS);
    park2.setWidth(80.0);
    park2.setFreeTurningSpace(150.0);
    park2.setParking("Aparcamiento público");
    park2.setPlazaPMR(true);
    park2.setBus(true);
    park2.setPedestrian(true);
    park2.setBioArea(false);
    park2.setDegree1(7);
    park2.setDegree2(3);
    park2.setDegree3(0);
    park2.setGeneralAccessibility(false);
    park2.setAccessibleFountain(false);
    park2.setImageSet("2.jpg");
    park2.setLatitude(43.3825185F);
    park2.setLongitude(-8.4041812F);
    parkDao.create(park2);

    Park park3 = new Park();
    park3.setParkId(3);
    park3.setName("Adormideras Cutty Shark");
    park3.setVisitYear(2020);
    park3.setAddress("https://goo.gl/maps/iyD31wGkRbz2W8f8A");
    park3.setPostalCode("15002");
    park3.setAreaUbi(AreaUbi.VIA);
    park3.setType(ParkType.RESIDENCIAL);
    park3.setCierrmt(Cierrmt.MAD);
    park3.setCierrmd(Cierrmd.PAR);
    park3.setPavimtp(Pavimtp.CYG);
    park3.setSignMaterial(SignMaterial.METAL);
    park3.setAgeSeparation(false);
    park3.setAgeRange("Mayores de 6 años");
    park3.setNumElements(7);
    park3.setNumUsers(41);
    park3.setAccessibility(Accessibility.SIN_OBSTÁCULOS);
    park3.setWidth(80.0);
    park3.setFreeTurningSpace(150.0);
    park3.setParking("No");
    park3.setPlazaPMR(true);
    park3.setBus(true);
    park3.setPedestrian(true);
    park3.setBioArea(false);
    park3.setDegree1(7);
    park3.setDegree2(0);
    park3.setDegree3(0);
    park3.setGeneralAccessibility(false);
    park3.setAccessibleFountain(false);
    park3.setImageSet("3.png");
    park3.setLatitude(43.3832F);
    park3.setLongitude(-8.3970519F);
    parkDao.create(park3);

    Park park4 = new Park();
    park4.setParkId(4);
    park4.setName("Alcalde Puga y Parga");
    park4.setVisitYear(2023);
    park4.setAddress("https://goo.gl/maps/YP5yzLhXSEf5J6vM8");
    park4.setPostalCode("15006");
    park4.setAreaUbi(AreaUbi.VIA);
    park4.setType(ParkType.TRÁFICO_CONCURRIDO);
    park4.setCierrmt(Cierrmt.MET);
    park4.setCierrmd(Cierrmd.TOT);
    park4.setPavimtp(Pavimtp.CYS);
    park4.setSignMaterial(SignMaterial.METAL);
    park4.setAgeSeparation(true);
    park4.setAgeRange("0 a 12 años");
    park4.setNumElements(0);
    park4.setNumUsers(14);
    park4.setAccessibility(Accessibility.SIN_OBSTÁCULOS);
    park4.setWidth(80.0);
    park4.setFreeTurningSpace(150.0);
    park4.setParking("No");
    park4.setPlazaPMR(true);
    park4.setBus(true);
    park4.setPedestrian(true);
    park4.setBioArea(false);
    park4.setDegree1(3);
    park4.setDegree2(2);
    park4.setDegree3(0);
    park4.setGeneralAccessibility(false);
    park4.setAccessibleFountain(true);
    park4.setImageSet("4.png");
    park4.setLatitude(43.3566948F);
    park4.setLongitude(-8.4051093F);
    parkDao.create(park4);

    Park park5 = new Park();
    park5.setParkId(5);
    park5.setName("Os Ancares");
    park5.setVisitYear(2023);
    park5.setAddress("https://goo.gl/maps/qPxZgZLDMVcvUC8B9");
    park5.setPostalCode("15190");
    park5.setAreaUbi(AreaUbi.VIA);
    park5.setType(ParkType.RESIDENCIAL);
    park5.setCierrmt(Cierrmt.MAD);
    park5.setCierrmd(Cierrmd.TOT);
    park5.setPavimtp(Pavimtp.CAU);
    park5.setSignMaterial(SignMaterial.METAL);
    park5.setAgeSeparation(true);
    park5.setAgeRange("0 a 5 años; 5 a 12 años");
    park5.setNumElements(0);
    park5.setNumUsers(14);
    park5.setAccessibility(Accessibility.SIN_OBSTÁCULOS);
    park5.setWidth(80.0);
    park5.setFreeTurningSpace(150.0);
    park5.setParking("No");
    park5.setPlazaPMR(true);
    park5.setBus(true);
    park5.setPedestrian(true);
    park5.setBioArea(false);
    park5.setDegree1(8);
    park5.setDegree2(1);
    park5.setDegree3(0);
    park5.setGeneralAccessibility(false);
    park5.setAccessibleFountain(false);
    park5.setImageSet("5.png");
    park5.setLatitude(43.324416F);
    park5.setLongitude(-8.4364619F);
    parkDao.create(park5);


    GameElement element1 = new GameElement();
    element1.setObjectId(1);
    element1.setElementName("Balancín Doble Selva");
    element1.setElementType("Balancín");
    element1.setSwinging(true);
    element1.setCooperativePlay(true);
    element1.setSocialPlay(true);
    element1.setAccessibilityDegree(1);
    element1.setPark(park1);
    element1.setImage("6.jpg");
    gameElementDao.create(element1);

    GameElement element2 = new GameElement();
    element2.setObjectId(2);
    element2.setElementName("Balancín Moto");
    element2.setElementType("Juego de muelles");
    element2.setSwinging(true);
    element2.setSoloPlay(true);
    element2.setAccessibilityDegree(1);
    element2.setPark(park1);
    element2.setImage("2.webp");
    gameElementDao.create(element2);

    GameElement element3 = new GameElement();
    element3.setObjectId(3);
    element3.setElementName("Balancín Ponys");
    element3.setElementType("Juego de muelles");
    element3.setSwinging(true);
    element3.setSoloPlay(true);
    element3.setAccessibilityDegree(1);
    element3.setPark(park1);
    element3.setImage("3.webp");
    gameElementDao.create(element3);

    GameElement element4 = new GameElement();
    element4.setObjectId(4);
    element4.setElementName("Balancín Pato");
    element4.setElementType("Juego de muelles");
    element4.setSwinging(true);
    element4.setSoloPlay(true);
    element4.setAccessibilityDegree(1);
    element4.setPark(park1);
    element4.setImage("4.webp");
    gameElementDao.create(element4);


    GameElement element7 = new GameElement();
    element7.setObjectId(7);
    element7.setElementName("Tobogán La Guarida del Castor");
    element7.setElementType("Tobogán");
    element7.setSliding(true);
    element7.setParallelPlay(true);
    element7.setAccessibilityDegree(1);
    element7.setPark(park1);
    element7.setImage("5.webp");
    gameElementDao.create(element7);

    // Game Elements for park2
    GameElement element8 = new GameElement();
    element8.setObjectId(8);
    element8.setElementName("Balancín Los Perritos");
    element8.setElementType("Balancín");
    element8.setSwinging(true);
    element8.setCooperativePlay(true);
    element8.setSocialPlay(true);
    element8.setAccessibilityDegree(1);
    element8.setPark(park2);
    element7.setImage("6.webp");
    gameElementDao.create(element8);

    GameElement element10 = new GameElement();
    element10.setObjectId(10);
    element10.setElementName("Carrousel Plutón");
    element10.setElementType("Rotación");
    element10.setSocialPlay(true);
    element10.setAccessibilityDegree(1);
    element10.setPark(park2);
    element10.setImage("7.webp");
    gameElementDao.create(element10);

    GameElement element11 = new GameElement();
    element11.setObjectId(11);
    element11.setElementName("Columpio Cesta");
    element11.setElementType("Columpio");
    element11.setSwinging(true);
    element11.setSoloPlay(true);
    element11.setAccessibilityDegree(2);
    element11.setPark(park2);
    element11.setImage("8.webp");
    gameElementDao.create(element11);

    GameElement element12 = new GameElement();
    element12.setObjectId(12);
    element12.setElementName("Columpio Doble Mixto Tabla Bebé");
    element12.setElementType("Columpio");
    element12.setSwinging(true);
    element12.setCooperativePlay(true);
    element12.setSocialPlay(true);
    element12.setAccessibilityDegree(2);
    element12.setPark(park2);
    element11.setImage("9.webp");
    gameElementDao.create(element12);


    GameElement element15 = new GameElement();
    element15.setObjectId(15);
    element15.setElementName("Juego Flores Parlantes");
    element15.setElementType("Juego sensorial");
    element15.setAuditory(true);
    element15.setSymbolic(true);
    element15.setCognitive(true);
    element15.setCooperativePlay(true);
    element15.setSocialPlay(true);
    element15.setParallelPlay(true);
    element15.setAccessibilityDegree(2);
    element15.setPark(park2);
    element15.setImage("10.jpeg");
    gameElementDao.create(element15);

    GameElement element17 = new GameElement();
    element17.setObjectId(209);
    element17.setElementName("Balancín Coche Veloz");
    element17.setElementType("Juego de muelles");
    element17.setSwinging(true);
    element17.setCooperativePlay(true);
    element17.setAccessibilityDegree(1);
    element17.setPark(park2);
    element17.setImage("11.webp");
    gameElementDao.create(element17);

    // GameElement 18 - RED TRIDIMENSIONAL
    GameElement element18 = new GameElement();
    element18.setObjectId(20);
    element18.setAreaCode(3);
    element18.setElementName("RED TRIDIMENSIONAL");
    element18.setElementType("Escalada");
    element18.setClimbing(true);
    element18.setBalancing(true);
    element18.setUpperBodyStrength(true);
    element18.setSocialPlay(true);
    element18.setAccessibilityDegree(1);
    element18.setPark(park3);
    gameElementDao.create(element18);

// GameElement 19 - MULTIJUEGO_2 TORRES CON TOBOGÁN PASARELA Y BARRAS
    GameElement element19 = new GameElement();
    element19.setObjectId(21);
    element19.setAreaCode(3);
    element19.setElementName("MULTIJUEGO 2 TORRES CON TOBOGÁN PASARELA Y BARRAS");
    element19.setElementType("Juego combinado");
    element19.setSliding(true);
    element19.setClimbing(true);
    element19.setUpperBodyStrength(true);
    element19.setCooperativePlay(true);
    element19.setParallelPlay(true);
    element19.setAccessibilityDegree(1);
    element19.setPark(park3);
    gameElementDao.create(element19);

// GameElement 20 - CASETA CON PANELES LÚDICOS
    GameElement element20 = new GameElement();
    element20.setObjectId(22);
    element20.setAreaCode(3);
    element20.setElementName("CASETA CON PANELES LÚDICOS");
    element20.setElementType("Juego simbólico");
    element20.setSymbolic(true);
    element20.setCognitive(true);
    element20.setSocialPlay(true);
    element20.setAccessibilityDegree(1);
    element20.setPark(park3);
    gameElementDao.create(element20);

// GameElement 21 - MULTIJUEGO_HELICÓPTERO
    GameElement element21 = new GameElement();
    element21.setObjectId(23);
    element21.setAreaCode(3);
    element21.setElementName("MULTIJUEGO HELICÓPTERO");
    element21.setElementType("Otros");
    element21.setBalancing(true);
    element21.setLinearPlay(true);
    element21.setAccessibilityDegree(1);
    element21.setPark(park3);
    gameElementDao.create(element21);

// GameElement 22 - BALANCÍN_LORITO
    GameElement element22 = new GameElement();
    element22.setObjectId(24);
    element22.setAreaCode(3);
    element22.setElementName("BALANCÍN LORITO");
    element22.setElementType("Juego de muelles");
    element22.setRocking(true);
    element22.setLinearPlay(true);
    element22.setAccessibilityDegree(1);
    element22.setPark(park3);
    gameElementDao.create(element22);

// GameElement 23 - BALANCÍN_ALCE
    GameElement element23 = new GameElement();
    element23.setObjectId(25);
    element23.setAreaCode(3);
    element23.setElementName("BALANCÍN ALCE");
    element23.setElementType("Juego de muelles");
    element23.setRocking(true);
    element23.setLinearPlay(true);
    element23.setAccessibilityDegree(1);
    element23.setPark(park3);
    gameElementDao.create(element23);

// GameElement 24 - TOBOGÁN_LA GUARIDA DEL CASTOR
    GameElement element24 = new GameElement();
    element24.setObjectId(26);
    element24.setAreaCode(3);
    element24.setElementName("TOBOGÁN LA GUARIDA DEL CASTOR");
    element24.setElementType("Tobogán");
    element24.setSliding(true);
    element24.setLinearPlay(true);
    element24.setAccessibilityDegree(1);
    element24.setPark(park3);
    gameElementDao.create(element24);

    // GameElement 30 - BALANCÍN DOBLE CABALLITOS
    GameElement element30 = new GameElement();
    element30.setObjectId(33);
    element30.setAreaCode(5);
    element30.setElementName("BALANCÍN DOBLE CABALLITOS");
    element30.setElementType("Juego de muelles");
    element30.setRocking(true);
    element30.setAccessibilityDegree(1);
    element30.setPark(park5);
    gameElementDao.create(element30);

    // GameElement 25 - CARRUSEL_GIROSPEED
    GameElement element25 = new GameElement();
    element25.setObjectId(27);
    element25.setAreaCode(4);
    element25.setElementName("CARRUSEL GIROSPEED");
    element25.setElementType("Rotación");
    element25.setSpinning(true);
    element25.setLinearPlay(true);
    element25.setAccessibilityDegree(1);
    element25.setPark(park4);
    gameElementDao.create(element25);

// GameElement 26 - COLUMPIO_CESTA
    GameElement element26 = new GameElement();
    element26.setObjectId(28);
    element26.setAreaCode(4);
    element26.setElementName("COLUMPIO CESTA");
    element26.setElementType("Columpio");
    element26.setSwinging(true);
    element26.setSocialPlay(true);
    element26.setAccessibilityDegree(2);
    element26.setPark(park4);
    gameElementDao.create(element26);

// GameElement 27 - COLUMPIO_DOBLE BEBE
    GameElement element27 = new GameElement();
    element27.setObjectId(29);
    element27.setAreaCode(4);
    element27.setElementName("COLUMPIO DOBLE BEBE");
    element27.setElementType("Columpio");
    element27.setSwinging(true);
    element27.setParallelPlay(true);
    element27.setAccessibilityDegree(2);
    element27.setPark(park4);
    gameElementDao.create(element27);

// GameElement 28 - MULTIJUEGO_MUNDOS FANTÁSTICOS
    GameElement element28 = new GameElement();
    element28.setObjectId(30);
    element28.setAreaCode(4);
    element28.setElementName("MULTIJUEGO MUNDOS FANTÁSTICOS");
    element28.setElementType("Juego combinado");
    element28.setClimbing(true);
    element28.setBalancing(true);
    element28.setRocking(true);
    element28.setUpperBodyStrength(true);
    element28.setLinearPlay(true);
    element28.setAccessibilityDegree(1);
    element28.setPark(park4);
    gameElementDao.create(element28);

// GameElement 29 - MULTIJUEGO_TIBOO
    GameElement element29 = new GameElement();
    element29.setObjectId(32);
    element29.setAreaCode(4);
    element29.setElementName("MULTIJUEGO TIBOO");
    element29.setElementType("Juego combinado");
    element29.setSliding(true);
    element29.setBalancing(true);
    element29.setLinearPlay(true);
    element29.setAccessibilityDegree(1);
    element29.setPark(park4);
    gameElementDao.create(element29);

// GameElement 31 - BALANCÍN JIRAFA
    GameElement element31 = new GameElement();
    element31.setObjectId(34);
    element31.setAreaCode(5);
    element31.setElementName("BALANCÍN JIRAFA");
    element31.setElementType("Juego de muelles");
    element31.setRocking(true);
    element31.setSocialPlay(true);
    element31.setAccessibilityDegree(1);
    element31.setPark(park5);
    gameElementDao.create(element31);

// GameElement 32 - MULTIJUEGO TEIDE
    GameElement element32 = new GameElement();
    element32.setObjectId(35);
    element32.setAreaCode(5);
    element32.setElementName("MULTIJUEGO TEIDE");
    element32.setElementType("Juego combinado");
    element32.setSliding(true);
    element32.setClimbing(true);
    element32.setUpperBodyStrength(true);
    element32.setSocialPlay(true);
    element32.setLinearPlay(true);
    element32.setAccessibilityDegree(1);
    element32.setPark(park5);
    gameElementDao.create(element32);

// GameElement 33 - COLUMPIO DOBLE TABLA MAXI
    GameElement element33 = new GameElement();
    element33.setObjectId(36);
    element33.setAreaCode(5);
    element33.setElementName("COLUMPIO DOBLE TABLA MAXI");
    element33.setElementType("Columpio");
    element33.setSwinging(true);
    element33.setAccessibilityDegree(1);
    element33.setPark(park5);
    gameElementDao.create(element33);

// GameElement 34 - MULTIJUEGO PESQUERITO
    GameElement element34 = new GameElement();
    element34.setObjectId(37);
    element34.setAreaCode(5);
    element34.setElementName("MULTIJUEGO PESQUERITO");
    element34.setElementType("Juego combinado");
    element34.setSliding(true);
    element34.setClimbing(true);
    element34.setUpperBodyStrength(true);
    element34.setSymbolic(true);
    element34.setSocialPlay(true);
    element34.setLinearPlay(true);
    element34.setAccessibilityDegree(1);
    element34.setPark(park5);
    gameElementDao.create(element34);

// GameElement 35 - COLUMPIO DOBLE BEBÉ
    GameElement element35 = new GameElement();
    element35.setObjectId(38);
    element35.setAreaCode(5);
    element35.setElementName("COLUMPIO DOBLE BEBÉ");
    element35.setElementType("Columpio");
    element35.setSwinging(true);
    element35.setParallelPlay(true);
    element35.setAccessibilityDegree(2);
    element35.setPark(park5);
    gameElementDao.create(element35);

// GameElement 36 - TOBOGÁN ELEFANTE
    GameElement element36 = new GameElement();
    element36.setObjectId(39);
    element36.setAreaCode(5);
    element36.setElementName("TOBOGÁN ELEFANTE");
    element36.setElementType("Tobogán");
    element36.setSliding(true);
    element36.setLinearPlay(true);
    element36.setAccessibilityDegree(1);
    element36.setPark(park5);
    gameElementDao.create(element36);

// GameElement 37 - MULTIJUEGO CAPRI
    GameElement element37 = new GameElement();
    element37.setObjectId(40);
    element37.setAreaCode(5);
    element37.setElementName("MULTIJUEGO CAPRI");
    element37.setElementType("Juego combinado");
    element37.setSliding(true);
    element37.setClimbing(true);
    element37.setUpperBodyStrength(true);
    element37.setSocialPlay(true);
    element37.setLinearPlay(true);
    element37.setAccessibilityDegree(1);
    element37.setPark(park5);
    gameElementDao.create(element37);

// GameElement 38 - BALANCÍN PARCHÍS
    GameElement element38 = new GameElement();
    element38.setObjectId(41);
    element38.setAreaCode(5);
    element38.setElementName("BALANCÍN PARCHÍS");
    element38.setElementType("Balancín");
    element38.setRocking(true);
    element38.setAccessibilityDegree(1);
    element38.setPark(park5);
    gameElementDao.create(element38);


    //Usuarios
    usuarioService.registerUsuario("pepeminmin", "pepeminmin", "Pepe","Min","911252879", "pepe@gmail.com",true, UserType.PROFESIONAL_DEL_ÁMBITO_SOCIAL);
    usuarioService.registerUsuario("lauralaura", "lauralaura", "Laura","Max","123456789", "laura@gmail.com", false, UserType.EDUCATIVO_O_SANITARIO);
    //Incidents
    Incident incident1= new Incident();
    incident1.setIncidentType(IncidentType.CARTELERÍA);
    incident1.setParkId(1L);
    incident1.setParkName("Plaza Alfonso XIII");
    incident1.setDescription("La edad máxima de la cartelería no corresponde al excel facilitado.");
    incidentDao.create(incident1);

    Incident incident2 = new Incident();
    incident2.setIncidentType(IncidentType.ELEMENTOS_DE_JUEGO);
    incident2.setParkId(2L);
    incident2.setParkName("Alcaldesa Berta Tapia");
    incident2.setDescription("176_Panel ludico no figura, en su lugar columpio");
    incidentDao.create(incident2);

    Incident incident3 = new Incident();
    incident3.setIncidentType(IncidentType.PAVIMENTO);
    incident3.setParkId(2L);
    incident3.setParkName("Alcalde Puga y Parga");
    incident3.setDescription("Caucho con.+Césped sin (CYS) y caucho continuo.");
    incidentDao.create(incident3);
  }
}

