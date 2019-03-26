package Main;

import TransitSystemClasses.BusSubTrip;
import TransitSystemClasses.SubwaySubTrip;
import TransitSystemExceptions.CriticalFileMissingException;
import TransitSystemExceptions.FileReadException;
import TransitSystemExceptions.IncorrectOwnerException;
import TransitSystemExceptions.UserNotFoundException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
  public static void main(String[] args)
      throws IOException, FileReadException, UserNotFoundException, CriticalFileMissingException,
          IncorrectOwnerException {
    ReadEvents.setUserList(ReadUser.readUser("ExampleUsers.txt"));
    ReadEvents.setCards(ReadCard.readCard("ExampleCards.txt", ReadEvents.getUserList()));
    ArrayList<HashMap<String, String[]>> routes = ReadStations.readStations("ExampleStations.txt");
    SubwaySubTrip.setSubwayRoutes(routes.get(0));
    BusSubTrip.setBusRoutes((routes.get(1)));
    ReadEvents.readEvents("ExampleEvents.txt");
    System.out.println("Processing success! Check \"output.txt\" for output results.");
  }
}
