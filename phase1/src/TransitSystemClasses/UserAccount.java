package TransitSystemClasses;

import javax.naming.InvalidNameException;

/** A UserAccount for a User. */
public class UserAccount {

  // *** Instance variables ***
  private TransitUser accountHolder; // The User (owner) of this Account.

  // *** Constructor ***

  /**
   * Constructs a new UserAccount for a User.
   *
   * @param accountHolder the username of the User that owns this Account.
   */
  public UserAccount(TransitUser accountHolder) {
    this.accountHolder = accountHolder;
  }

  /**
   * Returns the TransitUser that owns this Account.
   *
   * @return the TransitUser that owns this Account.
   */
  public TransitUser getAccountHolder() {
    return accountHolder;
  }

  /**
   * Changes the name of the User that owns this Account.
   *
   * @param firstName the first name of the User.
   * @param lastName the last name of the User.
   */
  public void changeName(String firstName, String lastName) throws InvalidNameException {
    String fullName = firstName + " " + lastName;
    if (isValidName(fullName)) {
      accountHolder.setName(firstName, lastName);
    } else {
      throw new InvalidNameException("This is not a valid name.");
    }
  }

  /**
   * Returns true if and only if the given full name matches the regex pattern.
   *
   * @param fullName the full name of a person to check against regex.
   * @return true iff the given full name matches the regex pattern.
   */
  private boolean isValidName(String fullName) {
    return fullName.matches("^[\\p{L} .'-]+$");
  }

  /**
   * Returns the full name of the User that owns this Account.
   *
   * @return the full name of the User that owns this Account.
   */
  public String getName() {
    return accountHolder.getName();
  }

  /**
   * Returns the email of the User that owns this Account.
   *
   * @return the email of the User that owns this Account.
   */
  public String getEmail() {
    return accountHolder.getEmail();
  }

  /**
   * Returns a string representation of the User of this Account's three most recent Trips.
   *
   * @return a string representation of the User of this Account's three most recent Trips.
   */
  public String showThreeRecentTrips() {
    return accountHolder.latestTrips();
  }

  /**
   * Returns the average transit cost per month that the User of this Account is spending.
   *
   * @return the average transit cost per month that the User of this Account is spending.
   */
  public double averageTransitCostPerMonth() {
    // note: calls Card::getAverageMonthlyCost
    return accountHolder.getMyCards().stream().mapToDouble(Card::getAverageMonthlyCost).sum();
  }

  /**
   * Returns a string representation of this User's UserAccount.
   *
   * @return a string representation of this User's UserAccount.
   */
  public String toString() {
    return accountHolder.toString();
  }
}
