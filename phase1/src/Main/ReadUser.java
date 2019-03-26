package Main;

import TransitSystemClasses.TransitUser;
import TransitSystemClasses.UserAccount;
import TransitSystemExceptions.CriticalFileMissingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/** A class of read user from user.txt */
class ReadUser {

  /**
   * Read user information from a file.
   *
   * @param filePath the path of the file contains the user information.
   * @return an ArrayList of all the user in the user information file.
   * @throws CriticalFileMissingException when the user information file is missing.
   */
  static ArrayList<UserAccount> readUser(String filePath) throws CriticalFileMissingException {
    File userFile = new File(filePath);
    Scanner content;
    ArrayList<TransitUser> result = new ArrayList<>();
    try {
      content = new Scanner(userFile);
    } catch (IOException e) {
      throw new CriticalFileMissingException("User file missing!");
    }
    while (content.hasNextLine()) {
      String thisLine = content.nextLine();
      String firstLine = thisLine.substring(0, thisLine.indexOf(","));
      String lastLine = thisLine.substring(thisLine.indexOf(",") + 2, thisLine.indexOf("("));
      String email = thisLine.substring(thisLine.indexOf("(") + 1, thisLine.indexOf(")"));
      TransitUser thisTransitUser = new TransitUser(firstLine, lastLine, email);
      result.add(thisTransitUser);
    }
    ArrayList<UserAccount> finalResult = new ArrayList<>();
    for (TransitUser thisUser : result) {
      // UserAccount thisAccount = new UserAccount(thisUser);
      finalResult.add(new UserAccount(thisUser));
    }
    return finalResult;
  }
}
