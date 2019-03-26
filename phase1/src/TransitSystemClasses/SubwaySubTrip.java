package TransitSystemClasses;

import TransitSystemExceptions.TripEnRouteException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;

public class SubwaySubTrip extends SubTrip {

  private static final float FARE_PER_STATION = (float) SystemManager.Fares.subwayFare.getFare();
  private static HashMap<String, String[]> subwayRoutes;

  SubwaySubTrip(LocalDateTime timeStart, String startPoint, String route) {
    super(timeStart, startPoint, route);
  }

  @Override
  float calculateFare() throws TripEnRouteException {
    if (isEnRoute()) {
      throw new TripEnRouteException("This TransitSystemClasses.Trip is still en route.");
    }
    return FARE_PER_STATION
        * Math.abs(
            Arrays.asList(subwayRoutes.get(getRouteName())).indexOf(getStartPoint())
                - Arrays.asList(subwayRoutes.get(getRouteName())).indexOf(getEndPoint()));
  }

  @Override
  public String toString() {
    return "SubwayTrip " + System.lineSeparator() + super.toString();
  }

  @Override
  String[] getRoute() {
    return subwayRoutes.get(getRouteName());
  }

  public static void setSubwayRoutes(HashMap<String, String[]> subwayRoutes) {

    SubwaySubTrip.subwayRoutes = subwayRoutes;
  }

  static HashMap getSubwayRoutes() {
    return SubwaySubTrip.subwayRoutes;
  }
}
