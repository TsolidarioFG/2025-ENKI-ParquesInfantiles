package es.udc.tfg.park_rest.config;

import es.udc.tfg.park_rest.model.domain.GameElement;
import es.udc.tfg.park_rest.model.domain.Park;
import es.udc.tfg.park_rest.model.enums.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelLoader {

  public static List<Park> loadParks(InputStream inputStream) throws Exception {
    List<Park> parks = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet sheet = workbook.getSheetAt(0); // Primera hoja

    for (Row row : sheet) {
      if (row.getRowNum() == 0) continue; // Saltar cabecera

      Park park = new Park();
      park.setParkId((int) row.getCell(0).getNumericCellValue());
      park.setName(row.getCell(1).getStringCellValue());
      park.setVisitYear((int) row.getCell(2).getNumericCellValue());
      park.setAddress(row.getCell(3).getStringCellValue());
      park.setPostalCode(String.valueOf((int)row.getCell(4).getNumericCellValue()));
      park.setAreaUbi(AreaUbi.valueOf(row.getCell(5).getStringCellValue()));
      park.setType(ParkType.valueOf(row.getCell(6).getStringCellValue()));
      park.setCierrmt(Cierrmt.valueOf(row.getCell(7).getStringCellValue()));
      park.setCierrmd(Cierrmd.valueOf(row.getCell(8).getStringCellValue()));
      park.setPavimtp(Pavimtp.valueOf(row.getCell(9).getStringCellValue()));
      park.setSignMaterial(SignMaterial.valueOf(row.getCell(10).getStringCellValue()));
      park.setAgeSeparation(row.getCell(11).getBooleanCellValue());
      park.setAgeRange(row.getCell(12).getStringCellValue());
      park.setNumElements((int) row.getCell(13).getNumericCellValue());
      park.setNumUsers((int) row.getCell(14).getNumericCellValue());
      park.setAccessibility(Accessibility.valueOf(row.getCell(15).getStringCellValue()));
      park.setWidth(row.getCell(16).getNumericCellValue());
      park.setFreeTurningSpace(row.getCell(17).getNumericCellValue());
      park.setParking(row.getCell(18).getStringCellValue());
      park.setPlazaPMR(row.getCell(19).getBooleanCellValue());
      park.setBus(row.getCell(20).getBooleanCellValue());
      park.setPedestrian(row.getCell(21).getBooleanCellValue());
      park.setBioArea(row.getCell(22).getBooleanCellValue());
      park.setDegree1((int) row.getCell(23).getNumericCellValue());
      park.setDegree2((int) row.getCell(24).getNumericCellValue());
      park.setDegree3((int) row.getCell(25).getNumericCellValue());
      park.setGeneralAccessibility(row.getCell(26).getBooleanCellValue());
      park.setAccessibleFountain(row.getCell(27).getBooleanCellValue());
      //park.setImageSet(row.getCell(28).getStringCellValue());
      //park.setQualityStarImage(row.getCell(29).getStringCellValue());
      park.setLatitude((float) row.getCell(28).getNumericCellValue());
      park.setLongitude((float) row.getCell(29).getNumericCellValue());

      parks.add(park);
    }

    workbook.close();
    return parks;
  }

  public static List<GameElement> loadGameElements(InputStream inputStream, List<Park> parks) throws Exception {
    List<GameElement> elements = new ArrayList<>();
    Workbook workbook = new XSSFWorkbook(inputStream);
    Sheet sheet = workbook.getSheetAt(1); // Segunda hoja

    for (Row row : sheet) {
      if (row.getRowNum() == 0) continue;

      GameElement element = new GameElement();
      element.setObjectId((int) row.getCell(0).getNumericCellValue());
      element.setElementName(row.getCell(1).getStringCellValue());
      element.setElementType(row.getCell(2).getStringCellValue());
      element.setSwinging(getBoolean(row, 3));
      element.setCooperativePlay(getBoolean(row, 4));
      element.setSocialPlay(getBoolean(row, 5));
      element.setSoloPlay(getBoolean(row, 6));
      element.setSliding(getBoolean(row, 7));
      element.setClimbing(getBoolean(row, 8));
      element.setUpperBodyStrength(getBoolean(row, 9));
      element.setLinearPlay(getBoolean(row, 10));
      element.setSymbolic(getBoolean(row, 11));
      element.setAuditory(getBoolean(row, 12));
      element.setCognitive(getBoolean(row, 13));
      element.setAccessibilityDegree((int) row.getCell(14).getNumericCellValue());
      element.setImage(row.getCell(15).getStringCellValue());

      int parkId = (int) row.getCell(16).getNumericCellValue();
      element.setPark(parks.stream().filter(p -> p.getParkId() == parkId).findFirst().orElse(null));

      elements.add(element);
    }

    workbook.close();
    return elements;
  }

  private static boolean getBoolean(Row row, int index) {
    try {
      return row.getCell(index).getBooleanCellValue();
    } catch (Exception e) {
      return false;
    }
  }
}
