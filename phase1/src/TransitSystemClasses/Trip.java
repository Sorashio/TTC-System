package TransitSystemClasses;

import TransitSystemExceptions.TripCanNotContinueException;
import TransitSystemExceptions.TripEnRouteException;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

class Trip {
  private SubTrip start;
  private SubTrip end;

  Trip(SubTrip start) {
    this.start = this.end = start;
  }

  void appendTrip(SubTrip connection) throws TripCanNotContinueException, TripEnRouteException {
    if (!canContinue(connection.getTimeStart(), connection.getStartPoint())) {
      throw new TripCanNotContinueException("TransitSystemClasses.Trip can't continue.");
    }
    this.end.setConnection(connection);
    this.end = this.end.getConnection();
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    SubTrip curNode = this.start;
    while (curNode != null) {
      result.append(curNode.toString());
      result.append(System.lineSeparator());
      curNode = curNode.getConnection();
    }
    float thisFare = getCurrentTotalFare();
    result.append("The current total fare of this trip is ");
    result.append(thisFare);
    return result.toString();
  }

  private boolean canContinue(LocalDateTime time, String station) throws TripEnRouteException {
    if (this.end.isEnRoute()) {
      throw new TripEnRouteException("Last trip has not ended yet!");
    }
    if (!station.equals(end.getEndPoint())) {
      return false;
    }
    long timeDuration = this.start.getTimeStart().until(time, ChronoUnit.MINUTES);
    return timeDuration <= 120;
  }

  SubTrip getEnd() {
    return end;
  }

  float getCurrentTotalFare() {
    SubTrip curNode = start;
    float sum = 0;
    while (curNode != null) {
      try {
        sum += curNode.getFare();
      } catch (TripEnRouteException e) {
        sum += 0;
      }
      curNode = curNode.getConnection();
    }
    return sum;
  }
}
