package Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import TransitSystemClasses.Card;
import TransitSystemClasses.UserAccount;
import TransitSystemExceptions.CriticalFileMissingException;
import TransitSystemExceptions.FileReadException;
import TransitSystemExceptions.IncorrectOwnerException;
import TransitSystemExceptions.UserNotFoundException;

/** A class of reading Card information from file. */
class ReadCard {

  /**
   * Attach card information to the users in the user list.
   *
   * @param filePath the path of the card.txt file
   * @param userList the list of users whose card is going to be attached.
   * @throws UserNotFoundException when user is not found in the userList
   * @throws CriticalFileMissingException if the file is missing
   * @throws FileReadException if file format is not valid.
   */
  static ArrayList<Card> readCard(String filePath, ArrayList<UserAccount> userList)
      throws CriticalFileMissingException, FileReadException, UserNotFoundException, IncorrectOwnerException {
    ArrayList<Card> result = new ArrayList<>();
    File cardInfo = new File(filePath);
    Scanner content;
    try {
      content = new Scanner(cardInfo);
    } catch (IOException e) {
      throw new CriticalFileMissingException("Card.txt file missing!");
    }
    loop:
    while (content.hasNextLine()) {
      String thisLine = content.nextLine();
      int frontIndex = thisLine.indexOf("(");
      int endIndex = thisLine.indexOf(")");
      String cardId = thisLine.substring(0, frontIndex);
      String email = thisLine.substring(frontIndex + 1, endIndex);
      Double cardBalance = Double.valueOf(thisLine.substring(endIndex + 3, thisLine.indexOf(",")));
      String status = thisLine.substring(thisLine.indexOf(",") + 2, thisLine.indexOf(";"));
      boolean isActivate;
      if (status.trim().equalsIgnoreCase("yes")) {
        isActivate = true;
      } else if (status.trim().equalsIgnoreCase("no")) {
        isActivate = false;
      } else {
        throw new FileReadException(
            String.format("Activation status %s is not a valid format.", status));
      }
      for (UserAccount user : userList) {
        if (user.getEmail().equalsIgnoreCase(email)) {
          Card newCard = new Card(user.getAccountHolder(), cardId);
          newCard.setBalance(cardBalance);
          if (!isActivate) {
            try{
              newCard.deactivateCard(email);
            } catch (Exception e){
              throw new IncorrectOwnerException(String.format("%s is not the owner of this card.", email));
            }
          }
          user.getAccountHolder().addCard(newCard);
          result.add(newCard);
          continue loop;
        }
      }
      throw new UserNotFoundException(
          String.format("The user %s is not found in the system.", email));
    }
    return result;
  }
}
