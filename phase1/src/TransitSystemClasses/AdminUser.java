package TransitSystemClasses;

import java.time.LocalDate;

/** An AdminUser has access to all the statistical data of the whole transit system. */
public class AdminUser {

  /**
   * Returns the daily revenue of the system on the date given.
   *
   * @param date the date to check the revenue
   * @return the daily revenue of the system on the date given.
   */
  private static double getDailyRevenue(LocalDate date) {
    return SystemManager.getDailyRevenue(date);
  }

  /**
   * Return the revenue of a particular month.
   *
   * @param date the date whose data want to be retrieved.
   * @return the monthly revenue of that month
   */
  private static double getMonthlyRevenue(LocalDate date) {
    return SystemManager.getMonthlyRevenue(date);
  }

  /**
   * Returns the total number of stops reached by all buses and subways.
   *
   * @return the total number of stops reached by all buses and subways.
   */
  private static int getTotalStopReached(LocalDate date) {
    return SystemManager.getTotalBusStopsReached(date)
        + SystemManager.getTotalSubwayStationsReached(date);
  }

  /**
   * Return the average month revenue of all months.
   *
   * @return monthly average of all month.
   */
  private static double getMonthlyAverage() {
    return SystemManager.getAverageMonthlyRevenue();
  }

  /**
   * Prints a daily report of the system statistics at the end of the day.
   *
   * @param date the date of the daily report.
   */
  public static String getDailyReport(LocalDate date) {
    return String.format(
        "The revenue generated this \"%s\" is: %.3f%n"
            + "The total number of stops/stations reached by all buses and subways is: %d",
        date.toString(), getDailyRevenue(date), getTotalStopReached(date));
  }

  /**
   * Returns a string of statistics of the month given by the LocalDate.
   *
   * @param date the month date of which to report.
   * @return a monthly report of revenue, and all stops/stations reached.
   */
  public static String getMonthlyReport(LocalDate date) {
    return String.format(
        "The revenue generated this \"%s\" is: %.3f%n"
            + "The total number of stops/stations reached by all buses and subways is: %d",
        date.toString(), getMonthlyRevenue(date), SystemManager.getMonthlyStopReached(date));
  }

  /**
   * Returns a string containing the system averages per month, across all months. Notice this
   * method is different from getMonthlyReport because the former reports the specific monthly data,
   * whereas this method averages all monthly data.
   *
   * @return the system averages per month.
   */
  public static String getAverageReport() {
    return String.format(
        "The average revenue generated per month is: %.3f%n "
            + "The average number of stops/stations per month reached by all buses and subways is: %d.",
        getMonthlyAverage(), SystemManager.getMonthlyAverageStopReached());
  }
}
