package TransitSystemClasses;

import TransitSystemExceptions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/** A Transit Card used for travelling a city. */
public class Card {

  private final TransitUser cardHolder;
  private final String cardId;
  private double balance;
  private boolean activated;

  private Trip currentTrip = null;
  private ArrayList<Trip> trips;
  private final Map<LocalDateTime, Double> transactions;

  /**
   * Constructs a new Transit Card that has an owner of this Card called cardHolder and a unique ID
   * called cardId.
   *
   * @param cardHolder the TransitUser that owns this Card.
   * @param id the ID of this Card.
   */
  public Card(TransitUser cardHolder, String id) {
    this.cardHolder = cardHolder;
    this.cardId = id;
    this.balance = 19;
    this.activated = true;
    this.trips = new ArrayList<>();
    this.transactions = new TreeMap<>();
  }

  /**
   * Used for view recent three trips, Return the last three trips on this card, if there are less
   * than three trips, return all trips
   */
  public String viewLastThreeTrips(String email) throws IncorrectOwnerException {
    if (!email.equals(this.cardHolder.getEmail())) {
      throw new IncorrectOwnerException(
          String.format("%s is not the owner of card %s.", email, getCardId()));
    } else {
      StringBuilder result = new StringBuilder();
      int num = this.trips.size();
      if (num == 0) {
        result.append("None. ");
      } else {
        if (num > 3) {
          num = 3;
        }
        for (int i = 0; i < num; i++) {
          result.append(i + 1);
          result.append(".");
          result.append(System.lineSeparator());
          result.append(this.trips.get(i).toString());
          result.append(System.lineSeparator());
        }
      }
      return result.toString();
    }
  }

  /**
   * Pay for transportation.
   *
   * @param fare the amount of money to pay.
   * @param time the time of payment.
   */
  private void pay(double fare, LocalDateTime time) {
    balance -= fare;
    transactions.put(time, fare);
    SystemManager.addRevenue(time.toLocalDate(), fare);
  }

  /**
   * Adds a Trip to the top of the Stack.
   *
   * @param newSubTrip the Trip to be added.
   */
  private void finishTrip(SubTrip newSubTrip) {
    this.trips.add(currentTrip);
    this.cardHolder.addToMyTrips(currentTrip);
    this.currentTrip = new Trip(newSubTrip);
  }

  // finish a trip without starting next trip
  private void finishTrip() {
    this.trips.add(currentTrip);
    this.cardHolder.addToMyTrips(currentTrip);
    this.currentTrip = null;
  }

  /**
   * This TransitUser taps into a bus station or a subway station with their card.
   *
   * @param time the LocalDateTime of tapping in.
   * @param station the departure station.
   * @param route the route of travel.
   * @throws LowBalanceException when card's balance is less than zero.
   */
  public void tapIn(LocalDateTime time, String station, String route)
      throws LowBalanceException, CardSuspendedException {

    if (balance < 0) {
      throw new LowBalanceException("Low balance! Please load money onto this card.");
    } else if (!isActivated()) {
      throw new CardSuspendedException("This Card has been suspended. Please call 555-555-5555");
    }
    if (BusSubTrip.getBusRoutes().containsKey(route)) {
      tapIntoBus(time, station, route);
    } else if (SubwaySubTrip.getSubwayRoutes().containsKey(route)) {
      tapIntoSubway(time, station, route);
    }
  }

  /**
   * Helper method for tapIn. This is called if TransitUser enters bus.
   *
   * @param time the LocalDateTime of tap-in.
   * @param station the departure station.
   * @param route the route of travel.
   */
  private void tapIntoBus(LocalDateTime time, String station, String route) {
    BusSubTrip nextSubTrip = new BusSubTrip(time, station, route);
    float nextFare = nextSubTrip.calculateFare();
    if (this.currentTrip == null) {
      this.currentTrip = new Trip(nextSubTrip);
    } else {
      try {
        this.currentTrip.appendTrip(nextSubTrip);
        float currFare = this.currentTrip.getCurrentTotalFare();
        // deduct up to cappedFare for continuous trip
        if (currFare + nextFare > SystemManager.Fares.MAX_FARE.getFare()) {
          nextFare = (float) SystemManager.Fares.MAX_FARE.getFare() - currFare;
        }
      } catch (TripEnRouteException en) {
        // deducts fine since illegally exit subway without paying fare previously
        pay(SystemManager.Fares.FINE.getFare(), time);
        this.currentTrip.getEnd().setFare((float) SystemManager.Fares.FINE.getFare());
        this.finishTrip(nextSubTrip);

      } catch (TripCanNotContinueException nc) {
        this.finishTrip(nextSubTrip);
      }
    }
    nextSubTrip.setFare(nextFare);
    pay(nextFare, time);
  }

  /**
   * Helper method for tapIn. This is called if TransitUser enters subway.
   *
   * @param time the time this TransitUser enters subway.
   * @param station the name of the departure station.
   * @param route the route of the subway's Trip.
   */
  private void tapIntoSubway(LocalDateTime time, String station, String route) {
    SubwaySubTrip nextSubTrip = new SubwaySubTrip(time, station, route);
    if (this.currentTrip == null) {
      this.currentTrip = new Trip(nextSubTrip);
    } else {
      try {
        this.currentTrip.appendTrip(nextSubTrip);
      } catch (TripEnRouteException en) {
        // deducts fine since illegally exit subway without paying fare previously
        pay(SystemManager.Fares.FINE.getFare(), time);
        this.currentTrip.getEnd().setFare((float) SystemManager.Fares.FINE.getFare());
        this.finishTrip(nextSubTrip);
      } catch (TripCanNotContinueException nc) {
        this.finishTrip(nextSubTrip);
      }
    }
  }

  /**
   * This TransitUser taps out of a bus or a subway station with their card.
   *
   * @param time the current LocalDateTime of check-in.
   * @param station the station checked into.
   * @param route the route to travel.
   * @throws TripEnRouteException when the Trip is currently en-route.
   */
  public void tapOut(LocalDateTime time, String station, String route) throws TripEnRouteException {
    if (BusSubTrip.getBusRoutes().containsKey(route)) {
      tapOutOfBus(time, station);
    } else if (SubwaySubTrip.getSubwayRoutes().containsKey(route)) {
      tapOutOfSubway(time, station);
    }
  }

  // helper method for tapOut
  private void tapOutOfBus(LocalDateTime time, String station) {
    LocalDate date = time.toLocalDate();
    if (this.currentTrip == null) {
      // NOTICE: THIS REDUCTION OF BALANCE IS NOT RECORDED IN TRIPS
      pay(SystemManager.Fares.FINE.getFare(), time);
      SystemManager.recordBusStops(date, 1);

    } else {
      SubTrip endingTrip = this.currentTrip.getEnd();
      try {
        endingTrip.finishTrip(time, station);
        int busStopNum = endingTrip.getNumOfStops();
        SystemManager.recordBusStops(date, busStopNum);

      } catch (TripNotEnRouteException | NullPointerException a) {
        // NOTICE: THIS REDUCTION OF BALANCE IS NOT RECORDED IN TRIPS
        pay(SystemManager.Fares.FINE.getFare(), time);
        SystemManager.recordBusStops(date, 1);

      } catch (ExitNotSameRouteException b) {
        endingTrip.setFare(
            (float) SystemManager.Fares.FINE.getFare()); // setting the fare for this sub trip
        pay(SystemManager.Fares.FINE.getFare(), time);
        SystemManager.recordBusStops(date, 1);

        // avoid recalculating fine upon the next entry
        this.finishTrip();
      }
    }
  }

  // helper method for tapOut
  private void tapOutOfSubway(LocalDateTime time, String station) throws TripEnRouteException {
    LocalDate date = time.toLocalDate();
    if (this.currentTrip == null) {
      // NOTICE: THIS REDUCTION OF BALANCE IS NOT RECORDED IN TRIPS
      pay(SystemManager.Fares.FINE.getFare(), time);
      SystemManager.recordSubwayStations(date, 1);

    } else {
      SubTrip endingTrip = this.currentTrip.getEnd();
      try {
        endingTrip.finishTrip(time, station);
        // set the fare for subway trip when exiting a station
        float nextFare = endingTrip.calculateFare();
        float currTotal = this.currentTrip.getCurrentTotalFare();
        if (currTotal + nextFare > SystemManager.Fares.MAX_FARE.getFare()) {
          nextFare = (float) SystemManager.Fares.MAX_FARE.getFare() - currTotal;
        }
        endingTrip.setFare(nextFare);
        pay(nextFare, time);
        int SubwayStationNum = endingTrip.getNumOfStops();
        SystemManager.recordSubwayStations(date, SubwayStationNum);

      } catch (TripNotEnRouteException | NullPointerException a) {
        // NOTICE: THIS REDUCTION OF BALANCE IS NOT RECORDED IN TRIPS
        pay(SystemManager.Fares.FINE.getFare(), time);
        SystemManager.recordSubwayStations(date, 1);
      } catch (ExitNotSameRouteException b) {
        endingTrip.setFare(
            (float) SystemManager.Fares.FINE.getFare()); // setting the fare for this sub trip
        pay(SystemManager.Fares.FINE.getFare(), time);
        SystemManager.recordSubwayStations(date, 1);

        // avoid recalculating fine upon the next entry
        this.finishTrip();
      }
    }
  }

  /**
   * Returns a string representation of information about this Card.
   *
   * @return a string representation of information about this Card.
   */
  @Override
  public String toString() {
    return String.format("%-8s %-8s%n%-8s %-8s%n", "Card ID", "Balance", cardId, balance);
  }

  /**
   * Returns the number of months this card has been in use.
   *
   * @return the number of months this Card has been in use.
   */
  private long getMonthsActive() {
    return transactions
        .keySet()
        .stream()
        .mapToInt(x -> x.getMonthValue() + x.getYear())
        .distinct()
        .count();
  }

  /**
   * Returns the total amount of money used by this Card.
   *
   * @return the total amount of monty used by this Card.
   */
  private double getTotalCosts() {
    // get the total amount of money this Card has spent.
    return transactions.values().stream().mapToDouble(Double::doubleValue).sum();
  }

  /**
   * Returns the average monthly cost of this Card.
   *
   * @return the average monthly cost of this Card.
   */
  double getAverageMonthlyCost() {
    if (transactions.size() == 0) {
      return 0;
    }
    return getTotalCosts() / getMonthsActive();
  }

  /**
   * Sets the balance of this Card.
   *
   * @param balance the balance to set.
   */
  public void setBalance(double balance) {
    this.balance = balance;
  }

  /**
   * Adds money onto this card by increasing this card's balance. Warning: proper amounts must be
   * checked in UserAccount.
   *
   * @param value the amount of money to add.
   */
  public void addValue(float value, String email) throws IncorrectOwnerException {
    if (email.equals(this.cardHolder.getEmail())) {
      throw new IncorrectOwnerException(
          String.format("%s is not the owner of card %s.", email, getCardId()));
    } else {
      this.balance += value;
    }
  }

  /** Suspends this card by deactivation. This card may no longer be used. */
  public void deactivateCard(String email) throws IncorrectOwnerException {
    if (!email.equals(this.cardHolder.getEmail())) {
      throw new IncorrectOwnerException(
          String.format("%s is not the owner of card %s.", email, getCardId()));
    } else {
      this.activated = false;
    }
  }

  /**
   * Returns the owner of this Card (the TransitUser).
   *
   * @return the TransitUser that owns this Card.
   */
  public TransitUser getCardHolder() {
    return cardHolder;
  }

  /**
   * Activates this Card, or throws IncorrectOwnerException.
   *
   * @param email the email of the cardHolder that owns this Card.
   * @throws IncorrectOwnerException when the email doesn't match the cardHolder's email.
   */
  public void activateCard(String email) throws IncorrectOwnerException {
    if (!email.equals(this.cardHolder.getEmail())) {
      throw new IncorrectOwnerException(
          String.format("%s is not the owner of card %s.", email, getCardId()));
    } else {
      this.activated = true;
    }
  }

  private boolean isActivated() {
    return activated;
  }

  /**
   * Returns the balance on this card. Double is used for money to hold more digits and better
   * accuracy.
   *
   * @return the balance on this card.
   */
  public double viewBalance(String email) throws IncorrectOwnerException {
    if (email.equals(this.cardHolder.getEmail())) {
      return this.balance;
    } else {
      throw new IncorrectOwnerException(
          String.format("You(%s) are not the owner of card %s.", email, getCardId()));
    }
  }

  public String getCardId() {
    return cardId;
  }
}
