package TransitSystemClasses;

import TransitSystemExceptions.IncorrectOwnerException;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

/** A TransitUser that contains personal information about this User. */
public class TransitUser extends User {

  // *** Instance variables ***
  private final List<Card> myCards; // A Stack of all this User's Card's. The
  // top is most recent
  private final List<Trip> myTrips; // A Stack of all this User's Trip's. The

  // *** Constructor ***

  /**
   * Constructs a new TransitUser with a name and email address.
   *
   * @param firstName the name of this User.
   * @param lastName the last name of this User.
   * @param email the email of this User.
   */
  public TransitUser(String firstName, String lastName, String email) {
    super(firstName, lastName, email);
    this.myCards = new Stack<>();
    this.myTrips = new Stack<>();
  }

  /**
   * Returns a list of Card's that this TransitUser owns.
   *
   * @return a list of Card's belonging to this TransitUser.
   */
  List<Card> getMyCards() {
    return myCards;
  }

  /**
   * Adds a new Trip to this TransitUser's list of Trips.
   *
   * @param newTrip the new Trip to add.
   */
  void addToMyTrips(Trip newTrip) {
    this.myTrips.add(newTrip);
  }

  /**
   * Returns a list of the three most recent Trip's or less that this TransitUser has taken.
   *
   * @return a list of the three most recent Trip's or less that this TransitUser has taken.
   */
  private List<Trip> getThreeRecentTrips() {
    return myTrips.stream().limit(3).collect(Collectors.toList());
  }

  /**
   * Adds a Card to the top of the Stack.
   *
   * @param c the Card to be added.
   */
  public void addCard(Card c) {
    myCards.add(c);
  }

  /**
   * Removes the given Card from the Stack.
   *
   * @param c the Card to remove.
   */
  public void removeCard(Card c) throws IncorrectOwnerException {
    if (this.getEmail().equals(c.getCardHolder().getEmail())) {
      myCards.remove(c);
    } else {
      throw new IncorrectOwnerException(
          String.format("%s is not the owner of card %s.", this.getEmail(), c.getCardId()));
    }
  }

  /**
   * Returns a readable string representation of this TransitUser. Note: this method relies on
   * properly implemented toString methods from classes User, Card, and Trip.
   *
   * @return a string representation of this TransitUser.
   */
  @Override
  public String toString() {
    StringBuilder s = new StringBuilder(super.toString());
    s.append(String.format("%nMy Transit cards:%n"));
    for (Card c : myCards) {
      s.append(c.toString());
    }
    s.append(String.format("%nMy recent trips:%n"));
    for (Trip t : myTrips) {
      s.append(t.toString());
    }
    return s.toString();
  }

  /**
   * Returns a string of the three most recent trips this TransitUser has taken.
   *
   * @return a string of the three most recent trips this TransitUser has taken.
   */
  String latestTrips() {
    StringBuilder s = new StringBuilder();
    if (myTrips.isEmpty()) {
      s.append("No trips are on this card.");
    } else {
      int i = 1;
      for (Trip trip : getThreeRecentTrips()) {
        s.append(String.format("%d) %s%n", i++, trip.toString()));
      }
    }
    return s.toString();
  }
}
