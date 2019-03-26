package Main;

import TransitSystemExceptions.CriticalFileMissingException;
import TransitSystemExceptions.FileReadException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

class ReadStations {

  /**
   * Read bus and subway station information from filePath and return information.
   *
   * @param filePath the path of the file that stores the routes information.
   * @return ArrayList of two hashmap, the first hashmap stores all the subway info, the second one
   *     stroe the bus info.
   * @throws CriticalFileMissingException when the station information file is missing.
   * @author Qingyuan Qie
   */
  static ArrayList<HashMap<String, String[]>> readStations(String filePath)
      throws CriticalFileMissingException, FileReadException {
    File stationInfo = new File(filePath);
    HashMap<String, String[]> subwayStation = new HashMap<>();
    HashMap<String, String[]> busStation = new HashMap<>();
    Scanner content;
    ArrayList<HashMap<String, String[]>> result = new ArrayList<>();
    try {
      content = new Scanner(stationInfo);

    } catch (IOException e) {
      throw new CriticalFileMissingException("Station File missing.");
    }
    while (content.hasNextLine()) {
      String thisLine = content.nextLine();
      int nameEnd = thisLine.indexOf("(");
      int columnIndex = thisLine.indexOf(":");
      String routeName = thisLine.substring(0, nameEnd);
      // +1, -1 is to get rid of the bracket.
      String transitType = thisLine.substring(nameEnd + 1, columnIndex - 1);
      ArrayList<String> thisRoute = new ArrayList<>();
      // Read until the end of the line.
      String restInfo = thisLine.substring(columnIndex + 2, thisLine.indexOf(";"));
      String[] stationArray = restInfo.split("-");
      for (String station : stationArray) {
        String trimStation = station.trim();
        // get rid of the double quote around the station name.
        thisRoute.add(trimStation.substring(1, trimStation.length() - 1));
      }
      if (transitType.equalsIgnoreCase("subway")) {
        subwayStation.put(routeName, thisRoute.toArray(new String[0]));
      } else if (transitType.equalsIgnoreCase("bus")) {
        busStation.put(routeName, thisRoute.toArray(new String[0]));
      } else {
        throw new FileReadException(
            String.format("%s: Designated transit type does not exist!", transitType));
      }
    }
    result.add(subwayStation);
    result.add(busStation);
    return result;
  }
}
