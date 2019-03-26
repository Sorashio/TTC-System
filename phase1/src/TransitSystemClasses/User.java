package TransitSystemClasses;

/** A TransitSystemClasses.User that has private information. */
public abstract class User {

  // *** Instance variables ***
  private String firstName; // the first name of this user.
  private String lastName; // the last name of this user.
  private String email; // the email of this user.

  // *** Constructor ***

  /**
   * Construct a new TransitSystemClasses.User with a name and email address.
   *
   * @param firstName the first name of this TransitSystemClasses.User.
   * @param lastName the last name of this TransitSystemClasses.User.
   * @param email the email of this TransitSystemClasses.User.
   */
  public User(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  /**
   * Returns the name of this TransitSystemClasses.User.
   *
   * @return the name of this TransitSystemClasses.User.
   */
  String getName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the email of this TransitSystemClasses.User.
   *
   * @return the email of this TransitSystemClasses.User.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the name of this TransitSystemClasses.User.
   *
   * @param firstName the first name of this user.
   * @param lastName the last name of this user.
   */
  void setName(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  /**
   * Returns a string representation of this TransitSystemClasses.User.
   *
   * @return a string representation of this TransitSystemClasses.User.
   */
  @Override
  public String toString() {
    return String.format("Name: %s%nEmail: %s%n", getName(), email);
  }
}
