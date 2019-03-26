package Main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

import TransitSystemClasses.*;
import TransitSystemExceptions.*;

import java.io.IOException;

class ReadEvents {

  private static ArrayList<UserAccount> userList;
  private static ArrayList<Card> cards;

  static void readEvents(String filePath) throws CriticalFileMissingException, IOException {
    FileWriter outputFIle = new FileWriter("output.txt");
    BufferedWriter output = new BufferedWriter(outputFIle);
    File eventFile = new File(filePath);
    Scanner content;
    try {
      content = new Scanner(eventFile);
    } catch (IOException e) {
      throw new CriticalFileMissingException("events.txt file missing.");
    }
    while (content.hasNextLine()) {
      String thisLine = content.nextLine();
      if (thisLine.contains("enters")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String stationName = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String route = rest.substring(rest.indexOf("(") + 1, rest.indexOf(")"));
        handleTapIn(time, id, stationName, route, output);
        continue;
      }
      if (thisLine.contains("exits")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String stationName = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String route = rest.substring(rest.indexOf("(") + 1, rest.indexOf(")"));
        handleTapOut(time, id, stationName, route, output);
        continue;
      }
      if (thisLine.contains("deactivate")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleDeactivateCard(time, id, email, output);
        continue;
      }
      if (thisLine.contains("activate")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleActivateCard(time, id, email, output);
        continue;
      }
      if (thisLine.contains("adds card")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String userEmail = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 2);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleAddCard(time, userEmail, id, output);
        continue;
      }
      if (thisLine.contains("removes")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String userEmail = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 2);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleRemoveCard(time, userEmail, id, output);
        continue;
      }
      if (thisLine.contains("recent trips")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String userEmail = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleShowThreeRecentTrips(time, userEmail, output);
        continue;
      }
      if (thisLine.contains("average costs")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String userEmail = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleAverageTransitCostPerMonth(time, userEmail, output);
        continue;
      }
      if (thisLine.contains("changes name")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String userEmail = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(thisLine.indexOf("]") + 1);
        String newName = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleChangeName(time, userEmail, newName, output);
        continue;
      }
      if (thisLine.contains("New card")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String id = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleNewCard(time, id, email, output);
        continue;
      }
      if (thisLine.contains("adds value")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String cardId = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        double value = Double.valueOf(rest.substring(rest.indexOf("[") + 1, rest.indexOf("]")));
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleAddValue(time, cardId, value, email, output);
        continue;
      }
      if (thisLine.contains("views balance")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String cardId = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleViewBalance(time, cardId, email, output);
        continue;
      }
      if (thisLine.contains("daily report")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        if (!thisLine.contains("AdminUser")) {
          output.write(
              String.format(
                  "%s: You don't have Administrative access!" + System.lineSeparator(),
                  time.toString()));
          continue;
        }
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        LocalDate requestDate =
            handleDateStamp(rest.substring(rest.indexOf("[") + 1, rest.indexOf("]")));
        handleViewDailyRevenue(time, requestDate, output);
        continue;
      }
      if (thisLine.contains("monthly report")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        if (!thisLine.contains("AdminUser")) {
          output.write(
              String.format(
                  "%s: You don't have Administrative access!" + System.lineSeparator(),
                  time.toString()));
          continue;
        }
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        LocalDate requestDate =
            handleDateStamp(rest.substring(rest.indexOf("[") + 1, rest.indexOf("]")));
        handleViewMonthlyRevenue(time, requestDate, output);
        continue;
      }
      if (thisLine.contains("average report")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        if (!thisLine.contains("AdminUser")) {
          output.write(
              String.format(
                  "%s: You don't have Administrative access!" + System.lineSeparator(),
                  time.toString()));
          continue;
        }
        handleViewAverageStatistic(time, output);
        continue;
      }
      if (thisLine.contains("New transitUser")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String name = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        String firstName = name.substring(0, name.indexOf(","));
        String lastName = name.substring(name.indexOf(",") + 2);
        rest = rest.substring(rest.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleNewUser(time, firstName, lastName, email, output);
        continue;
      }
      if (thisLine.contains("views last 3")) {
        LocalDateTime time = handleTimeStamp(thisLine.substring(1, thisLine.indexOf("]")));
        String rest = thisLine.substring(thisLine.indexOf("]") + 1);
        String email = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        rest = rest.substring(rest.indexOf("]") + 1);
        String cardId = rest.substring(rest.indexOf("[") + 1, rest.indexOf("]"));
        handleViewCardTrips(time, cardId, email, output);
        continue;
      }
      output.write(
          thisLine
              + " - format incorrect, please check format carefully!"
              + System.lineSeparator());
    }
    output.flush();
    output.close();
  }

  private static void handleTapIn(
      LocalDateTime time,
      String cardId,
      String stationName,
      String routeName,
      BufferedWriter output)
      throws IOException {
    try {
      Card cardToTap = searchCard(time, cardId);
      cardToTap.tapIn(time, stationName, routeName);
      output.write(
          String.format(
              "%s: Card %s Tap in successfully at %s" + System.lineSeparator(),
              time.toString(),
              cardId,
              stationName));
    } catch (Exception e) {
      output.write(
          String.format("%s: " + e.getMessage() + System.lineSeparator(), time.toString()));
    }
  }

  private static void handleTapOut(
      LocalDateTime time,
      String cardId,
      String stationName,
      String routeName,
      BufferedWriter output)
      throws IOException {
    try {
      Card cardToTap = searchCard(time, cardId);
      cardToTap.tapOut(time, stationName, routeName);
      output.write(
          String.format(
              "%s: Card %s Tap out successfully at %s" + System.lineSeparator(),
              time.toString(),
              cardId,
              stationName));
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  private static Card searchCard(LocalDateTime time, String cardId) throws CardNotFoundException {
    for (Card thisCard : cards) {
      if (thisCard.getCardId().equals(cardId)) {
        return thisCard;
      }
    }
    throw new CardNotFoundException(
        String.format(
            "%s: Card %s is not found in the system." + System.lineSeparator(),
            time.toString(),
            cardId));
  }

  private static void handleActivateCard(LocalDateTime time, String cardId, String email, BufferedWriter output)
      throws IOException {
    try {
      Card cardToHandle = searchCard(time, cardId);
      cardToHandle.activateCard(email);
      output.write(
          String.format(
              "%s: Card %s is successfully activated." + System.lineSeparator(),
              time.toString(),
              cardId));
    } catch (CardNotFoundException|IncorrectOwnerException e) {
      output.write(
          String.format("%s: " + e.getMessage() + System.lineSeparator(), time.toString()));
    }
  }

  private static void handleDeactivateCard(LocalDateTime time, String cardId, String email, BufferedWriter output)
      throws IOException {
    try {
      Card cardToHandle = searchCard(time, cardId);
      cardToHandle.deactivateCard(email);
      output.write(
          String.format(
              "%s: Card %s is successfully deactivated." + System.lineSeparator(),
              time.toString(),
              cardId));
    } catch (CardNotFoundException|IncorrectOwnerException e) {
      output.write(e.getMessage());
    }
  }

  private static UserAccount searchUser(LocalDateTime time, String email)
      throws UserNotFoundException {
    for (UserAccount thisUser : userList) {
      if (thisUser.getAccountHolder().getEmail().equalsIgnoreCase(email)) {
        return thisUser;
      }
    }
    throw new UserNotFoundException(
        String.format(
            "%s: This user is not registered in the transit system" + System.lineSeparator(),
            time));
  }

  private static void handleAddCard(
      LocalDateTime time, String email, String cardId, BufferedWriter output) throws IOException {
    try {
      searchUser(time, email).getAccountHolder().addCard(searchCard(time, cardId));
      output.write(
          String.format(
              "%s: Card %s successfully added to user %s" + System.lineSeparator(),
              time.toString(),
              cardId,
              email));
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  private static void handleRemoveCard(
      LocalDateTime time, String email, String cardId, BufferedWriter output) throws IOException {
    try {
      UserAccount user = searchUser(time, email);
      Card thisCard = searchCard(time, cardId);
      if (thisCard.getCardHolder().getEmail().equalsIgnoreCase(email)) {
        user.getAccountHolder().removeCard(thisCard);
        output.write(
            String.format(
                "%s: Card %s successfully removed from %s" + System.lineSeparator(),
                time.toString(),
                cardId,
                email));
      }
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  private static void handleAverageTransitCostPerMonth(
      LocalDateTime time, String email, BufferedWriter output) throws IOException {
    try {
      UserAccount user = searchUser(time, email);
      double avgCost = user.averageTransitCostPerMonth();
      output.write(
          String.format(
              "%s: User %s average transit cost per month is %s" + System.lineSeparator(),
              time.toString(),
              email,
              avgCost));
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  private static void handleShowThreeRecentTrips(
      LocalDateTime time, String email, BufferedWriter output) throws IOException {
    try {
      UserAccount user = searchUser(time, email);
      String latestTrips = user.showThreeRecentTrips();
      output.write(
          String.format(
              "%s: User %s latest trips are %s" + System.lineSeparator(),
              time.toString(),
              email,
              latestTrips));
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  private static void handleChangeName(
      LocalDateTime time, String email, String name, BufferedWriter output) throws IOException {
    try {
      UserAccount user = searchUser(time, email);
      String firstName = name.substring(0, name.indexOf(","));
      String lastName = name.substring(name.indexOf(",") + 1, name.length());
      user.changeName(firstName, lastName);
      output.write(
          String.format(
              "%s: User %s changes name to %s" + System.lineSeparator(),
              time.toString(),
              email,
              name));
    } catch (Exception e) {
      output.write(e.getMessage());
    }
  }

  /**
   * Handle the time stamp. The string is in yyyy/mm/dd/hh/mm, for example 2018/07/01/14/34.
   *
   * @return a LocalDateTIme object that represent the string.
   */
  private static LocalDateTime handleTimeStamp(String time) {
    String[] timeArray = time.split("/");
    int[] times = new int[5];
    for (int i = 0; i < timeArray.length; i++) {
      times[i] = Integer.valueOf(timeArray[i]);
    }
    return LocalDateTime.of(times[0], times[1], times[2], times[3], times[4]);
  }

  private static LocalDate handleDateStamp(String time) {
    String[] timeArray = time.split("/");
    int[] times = new int[3];
    for (int i = 0; i < timeArray.length; i++) {
      times[i] = Integer.valueOf(timeArray[i]);
    }
    return LocalDate.of(times[0], times[1], times[2]);
  }

  private static void handleNewCard(
      LocalDateTime time, String cardId, String email, BufferedWriter output) throws IOException {
    try {
      UserAccount user = searchUser(time, email);
      for (Card card : cards) {
        if (card.getCardId().equals(cardId)) {
          output.write(
              String.format(
                  "Card number must be unique. There is already %s in the system!", cardId));
          return;
        }
      }
      Card newCard = new Card(user.getAccountHolder(), cardId);
      user.getAccountHolder().addCard(newCard);
      cards.add(newCard);
      output.write(
          String.format(
              "%s: New Card %s successfully added for user %s" + System.lineSeparator(),
              time.toString(),
              cardId,
              email));
    } catch (Exception e) {
      output.write(String.format("%s: " + e.getMessage(), time.toString()));
    }
  }

  private static void handleAddValue(
      LocalDateTime time, String cardId, double value, String email, BufferedWriter output) throws IOException {
    try {
      Card thisCard = searchCard(time, cardId);
      if (value != 10 & value != 20 & value != 50) {
        output.write(
            String.format(
                "%s: value added must be 10, 20, 50, and %s is not one of the values"
                    + System.lineSeparator(),
                time.toString(),
                String.valueOf(value)));
        return;
      }
      thisCard.addValue((float) value, email);
    } catch (Exception e) {
      output.write(
          String.format("%s: " + e.getMessage() + System.lineSeparator(), time.toString()));
    }
  }

  private static void handleViewBalance(
      LocalDateTime time, String cardId, String email, BufferedWriter output) throws IOException {
    try {
      Card thisCard = searchCard(time, cardId);
      double balance = thisCard.viewBalance(email);
      output.write(
          String.format(
              "%s: Card %s has balance %s" + System.lineSeparator(),
              time.toString(),
              cardId,
              balance));
    } catch (Exception e) {
      output.write(
          String.format("%s: " + e.getMessage() + System.lineSeparator(), time.toString()));
    }
  }

  private static void handleViewDailyRevenue(
      LocalDateTime time, LocalDate date, BufferedWriter output) throws IOException {
    output.write(
        String.format(
            "%s: " + AdminUser.getDailyReport(date) + System.lineSeparator(), time.toString()));
  }

  private static void handleViewMonthlyRevenue(
      LocalDateTime time, LocalDate month, BufferedWriter output) throws IOException {
    output.write(
        String.format(
            "%s: " + AdminUser.getMonthlyReport(month) + System.lineSeparator(), time.toString()));
  }

  private static void handleViewAverageStatistic(LocalDateTime time, BufferedWriter output)
      throws IOException {
    output.write(
        String.format(
            "%s: " + AdminUser.getAverageReport() + System.lineSeparator(), time.toString()));
  }

  private static void handleNewUser(
      LocalDateTime time, String firstName, String lastName, String email, BufferedWriter output)
      throws IOException {
    if (userList.stream().anyMatch(x -> x.getAccountHolder().getEmail().equals(email))) {
      output.write(
          String.format(
              "%s: %s has already been registered!" + System.lineSeparator(),
              time.toString(),
              email));
      return;
    }
    TransitUser newUser = new TransitUser(firstName, lastName, email);
    UserAccount newAccount = new UserAccount(newUser);
    userList.add(newAccount);
    output.write(
        String.format(
            "%s: User %s successfully registered!" + System.lineSeparator(),
            time.toString(),
            email));
  }

  private static void handleViewCardTrips(
      LocalDateTime time, String cardId, String email, BufferedWriter output) throws IOException {
    try {
      Card thisCard = searchCard(time, cardId);
      String trips = thisCard.viewLastThreeTrips(email);
      output.write(
          String.format(
              "%s: Card %s has recent trips:"
                  + System.lineSeparator()
                  + " %s"
                  + System.lineSeparator(),
              time.toString(),
              cardId,
              trips));
    } catch (Exception e) {
      output.write(
          String.format("%s: " + e.getMessage() + System.lineSeparator(), time.toString()));
    }
  }

  static void setUserList(ArrayList<UserAccount> userList) {
    ReadEvents.userList = userList;
  }

  static void setCards(ArrayList<Card> cards) {
    ReadEvents.cards = cards;
  }

  static ArrayList<Card> getCards() {
    return cards;
  }

  static ArrayList<UserAccount> getUserList() {
    return userList;
  }
}
