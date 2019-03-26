package TransitSystemClasses;

import TransitSystemExceptions.ExitNotSameRouteException;
import TransitSystemExceptions.TripEnRouteException;
import TransitSystemExceptions.TripNotEnRouteException;

import java.time.LocalDateTime;
import java.util.Arrays;

abstract class SubTrip {
  private LocalDateTime timeStart;
  private LocalDateTime timeEnd;
  private String startPoint;
  private String endPoint;
  private float fare;
  private SubTrip connection;
  private boolean enRoute = true;
  private String[] path;
  private String routeName;

  /** number of stops reached */
  private int numOfStops = 0;

  SubTrip(LocalDateTime timeStart, String startPoint, String routeName) {
    this.timeStart = timeStart;
    this.startPoint = startPoint;
    this.routeName = routeName;
  }

  /**
   * Sets the timeEnd and endPoint of this TransitSystemClasses.SubTrip and mark this trip as over
   * by setting enRoute to false.
   *
   * @throws TripNotEnRouteException, throws the Exception when previous record is tapOut
   * @throws ExitNotSameRouteException, throws this Exception when exit from a different route
   */
  private void endTrip(LocalDateTime timeEnd, String endPoint)
      throws TripNotEnRouteException, ExitNotSameRouteException {
    if (!enRoute) {
      throw new TripNotEnRouteException("Exit without entry!");
    }
    if (!Arrays.asList(this.getRoute()).contains(endPoint)) {
      throw new ExitNotSameRouteException("Entry and exit on different route!");
    }
    enRoute = false;
    this.timeEnd = timeEnd;
    this.endPoint = endPoint;
  }

  /**
   * Sets the timeEnd and endPoint of this TransitSystemClasses.SubTrip and mark this trip as over
   * by setting enRoute to false, set the fare of this trip appropriately.
   *
   * @throws TripNotEnRouteException throws the Exception when the trip has already finished
   */
  void finishTrip(LocalDateTime timeEnd, String endPoint)
      throws TripNotEnRouteException, ExitNotSameRouteException {
    endTrip(timeEnd, endPoint);
    this.setPath();
    this.numOfStops = this.path.length - 1;
  }

  private void setPath() {
    String[] routes = getRoute();
    int startIndex = Arrays.asList(routes).indexOf(getStartPoint());
    int endIndex = Arrays.asList(routes).indexOf(getEndPoint());
    if (startIndex < endIndex) {
      this.path = Arrays.copyOfRange(routes, startIndex, endIndex + 1);
    } else {
      String[] result = Arrays.copyOfRange(routes, endIndex, startIndex + 1);
      this.path = SubTrip.reverse(result);
    }
  }

  float getFare() throws TripEnRouteException {
    if (enRoute) {
      throw new TripEnRouteException("This trip is still en Route.");
    }
    return fare;
  }

  void setFare(float fare) {
    this.fare = fare;
  }

  /**
   * Return the fare of this trip according to the charging scheme.
   *
   * @return the fare of this trip.
   * @throws TripEnRouteException if the TransitSystemClasses.Trip is not over yet.
   */
  abstract float calculateFare() throws TripEnRouteException;

  void setConnection(SubTrip connection) throws TripEnRouteException {
    if (isEnRoute()) {
      throw new TripEnRouteException("This trip is still en route.");
    }
    this.connection = connection;
  }

  boolean isEnRoute() {
    return enRoute;
  }

  LocalDateTime getTimeStart() {
    return timeStart;
  }

  private LocalDateTime getTimeEnd() throws TripEnRouteException {
    if (isEnRoute()) {
      throw new TripEnRouteException("This trip is still en route.");
    }
    return timeEnd;
  }

  SubTrip getConnection() {
    return connection;
  }

  String getRouteName() {
    return routeName;
  }

  int getNumOfStops() {
    return numOfStops;
  }

  /**
   * Return the route of this TransitSystemClasses.SubTrip.
   *
   * @return the route of this TransitSystemClasses.SubTrip in the form of an array.
   */
  abstract String[] getRoute();

  // Setters and getters.
  String getStartPoint() {
    return startPoint;
  }

  String getEndPoint() {
    return endPoint;
  }

  /**
   * Return an array which elements are in reverse order as in arrayToReverse.
   *
   * @param arrayToReverse Array whose elements need to be reversed.
   * @return Reversed version of arrayToReverse.
   */
  private static String[] reverse(String[] arrayToReverse) {
    String[] result = new String[arrayToReverse.length];
    int length = arrayToReverse.length;
    for (int i = 0; i < arrayToReverse.length; i++) {
      result[i] = arrayToReverse[length - 1 - i];
    }
    return result;
  }

  @Override
  public String toString() {
    try {
      return "Started at "
          + getTimeStart().toString()
          + " and end at "
          + getTimeEnd().toString()
          + "."
          + System.lineSeparator()
          + "The route of this trip is "
          + getRouteName()
          + ": "
          + String.join("-> ", Arrays.asList(path));
    } catch (TripEnRouteException e) {
      return "Started at "
          + getStartPoint()
          + " at "
          + getTimeStart().toString()
          + "The route of this trip is "
          + getRouteName()
          + ": "
          + " En route.";
    }
  }
}
