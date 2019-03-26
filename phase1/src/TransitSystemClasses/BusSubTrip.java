package TransitSystemClasses;

import java.time.LocalDateTime;
import java.util.HashMap;

public class BusSubTrip extends SubTrip {

  private static final float FARE_PER_TRIP = (float) SystemManager.Fares.busFare.getFare();
  private static HashMap<String, String[]> busRoutes;

  BusSubTrip(LocalDateTime timeStart, String startPoint, String route) {
    super(timeStart, startPoint, route);
  }

  @Override
  float calculateFare() {
    return FARE_PER_TRIP;
  }

  @Override
  String[] getRoute() {
    return busRoutes.get(getRouteName());
  }

  public static void setBusRoutes(HashMap<String, String[]> busRoutes) {
    BusSubTrip.busRoutes = busRoutes;
  }

  @Override
  public String toString() {
    return "BusTrip " + super.toString();
  }

  static HashMap getBusRoutes() {
    return BusSubTrip.busRoutes;
  }
}
