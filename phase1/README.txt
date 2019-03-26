#########################################################################
Input Requirements:
1. We have four input files in total:
   ExampleCards.txt, ExampleEvents.txt, ExampleStations.txt,ExampleUsers.txt.

2. Please refer to Format.txt for the formats of each file. There is a ExampleEvents.txt which
   provides an example of a correct-formatted event.txt.

3. Please notice that our program is strict to the format of input file and
   make sure you follow the "N.B." instructions in TentativeFormat.txt.

   Please also notice that the number of whitespaces also matters in input files and make sure
   that there is only one whitespace between each word. Even slightest disobey of the format will
   result in program crash and abnormal activity.

4. ExampleCards.txt, ExampleStations.txt, ExampleUsers.txt are setup files for the transit system
   and must be processed by Main.java before ExampleEvents.txt is processed.

5. Incorrectly-formatted line in the event.txt will also be included in the output.txt with a note
   says "format incorrect. Please check format carefully".

6. Incorrectly-formatted line in ExampleCards.txt, ExampleStations.txt, ExampleUsers.txt will
   interrupt the execution of program.

############################################################################
Output:
1. The output of our transit system is stored in the file "output.txt".

############################################################################
Exceptions/Special cases handle:
1. Illegal enter/exit
   A card will be deducted a fine(= 6) at each time of illegal enter/exit.
   We decide to handle special cases (e.g.power outage) the same as other illegal enter/exit.
   We may add a rebate method for these cases in Phase2.

    If a card has "TapOut" consecutively the second and later trips will not be stored in Card
    and user will not able to look at them by viewing recent trips.
    However, we store every transactions in Card instead, this is an extendable part and
    users will be able to look at all of the transactions on their Card in Phase2.

2. Incorrect cardholder
    If a user does some operations to a card that is not their own, the program will reject the
    request.
    These operations include: check the balance / view recent 3 trips / add value /
    activate/deactivate card / remove a card from the card list in one's account
    These operations do not include tapIn and tapOut


3. Activate/Deactivate/Remove card
   i. An activated card can be removed directly from the card list of one's account without being
   deactivated, which means the cardholder will not be able to do operations on this card except
   tapIn and tapOut.

   ii. The calculation of user's monthly average will not include removed cards;
       The calculation of total revenue is independent of the states of cards. It is recorded at
       each tapIn/Out.

   iii. The cardholder of this card can still add it to the card list in user account.

   iv. A deactivated card is still included in the calculation of user's monthly average
       if not removed from card list.

4. Transfer
   No matter how long a passenger stays at one station, as long as the time doesn't exceed 2 hours
   from his/her last tapIn, it will count as one trip and the 6 dollars limit is still applied.
   If a passenger enters a station within 2 hours time limit, but s/he taps out after 2 hours limit,
   it still counts as one single trip and the 6 dollars limit is applied.



