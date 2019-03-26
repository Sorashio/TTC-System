package TransitSystemClasses;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/** A SystemManager to record system statistics. */
class SystemManager {

  /**
   * Returns the monthly stops reached by all modes of transportation.
   *
   * @param date the month to find the monthly stops reached.
   * @return the monthly stops reached by all modes of transportation.
   */
  static int getMonthlyStopReached(LocalDate date) {
    return busStopsPerDay
            .entrySet()
            .stream()
            .filter(
                x ->
                    x.getKey().getMonthValue() == date.getMonthValue()
                        && x.getKey().getYear() == date.getYear())
            .mapToInt(Map.Entry::getValue)
            .sum()
        + subwayStationsPerDay
            .entrySet()
            .stream()
            .filter(
                x ->
                    x.getKey().getMonthValue() == date.getMonthValue()
                        && x.getKey().getYear() == date.getYear())
            .mapToInt(Map.Entry::getValue)
            .sum();
  }

  /** The total revenue recorded after each day. */
  private static final Map<LocalDate, Double> dailyRevenue = new TreeMap<>();

  /** The total number of bus stops recorded after each day. */
  private static final Map<LocalDate, Integer> busStopsPerDay = new TreeMap<>();

  /** The total number of subway stations recorded after each day. */
  private static final Map<LocalDate, Integer> subwayStationsPerDay = new TreeMap<>();

  /**
   * Adds stopsReached to the Map busStopsPerDay if the key is present, otherwise puts the key/value
   * pair in the Map.
   *
   * @param date the date that bus stops were reached.
   * @param stopsReached the number to add/increase in busStopsPerDay.
   */
  static void recordBusStops(LocalDate date, int stopsReached) {
    if (busStopsPerDay.containsKey(date)) {
      // update
      busStopsPerDay.replace(date, busStopsPerDay.get(date) + stopsReached);
    } else {
      busStopsPerDay.put(date, stopsReached);
    }
  }

  /**
   * Adds stationsReached to the Map subwayStationsPerDay if the key is present, otherwise puts the
   * key/value pair in the Map
   *
   * @param date the date that subway stations were reached.
   * @param stationsReached the number of stations to add/increase in subwayStationsPerDay.
   */
  static void recordSubwayStations(LocalDate date, int stationsReached) {
    if (subwayStationsPerDay.containsKey(date)) {
      subwayStationsPerDay.replace(date, subwayStationsPerDay.get(date) + stationsReached);
    } else {
      subwayStationsPerDay.put(date, stationsReached);
    }
  }

  /**
   * Adds/updates the dailyRevenue by adding revenue.
   *
   * @param date the LocalDate in which to add revenue.
   * @param revenue the revenue to be updated.
   */
  static void addRevenue(LocalDate date, double revenue) {
    if (dailyRevenue.containsKey(date)) {
      dailyRevenue.replace(date, dailyRevenue.get(date) + revenue);
    } else {
      dailyRevenue.put(date, revenue);
    }
  }

  /**
   * Returns the total number of bus stops reached on the given date, otherwise return 0.
   *
   * @param date the date to find bus stops reached.
   * @return the total number of bus stops reached on the given date, otherwise return 0.
   */
  static int getTotalBusStopsReached(LocalDate date) {
    return busStopsPerDay.getOrDefault(date, 0);
  }

  /**
   * Returns the total number of subway stations reached on the given date, otherwise return 0.
   *
   * @param date the date to find subway stations reached.
   * @return the total number of subway stations reached on the given date, otherwise return 0.
   */
  static int getTotalSubwayStationsReached(LocalDate date) {
    return subwayStationsPerDay.getOrDefault(date, 0);
  }

  /**
   * Returns the daily revenue of the given date, or zero otherwise.
   *
   * @param date the date to find revenue.
   * @return the revenue generated on the given date.
   */
  static double getDailyRevenue(LocalDate date) {
    return dailyRevenue.getOrDefault(date, 0.0);
  }

  /**
   * Returns the monthly revenue of the given date.
   *
   * @param date the date of the revenue.
   * @return the monthly revenue of the given date.
   */
  static double getMonthlyRevenue(LocalDate date) {
    return dailyRevenue
        .entrySet()
        .stream()
        .filter(
            x ->
                x.getKey().getMonthValue() == date.getMonthValue()
                    && x.getKey().getYear() == date.getYear())
        .mapToDouble(Map.Entry::getValue)
        .sum();
  }

  /**
   * Returns the average monthly revenue across all months.
   *
   * @return the average monthly revenue.
   */
  static double getAverageMonthlyRevenue() {
    double totalRev = dailyRevenue.values().stream().mapToDouble(Double::doubleValue).sum();
    long numMonths = getNumMonths(dailyRevenue.keySet());

    return totalRev / numMonths;
  }

  /**
   * Returns the number of months of a given set with LocalDate's.
   *
   * @param set the Set of LocalDate's.
   * @return the number of months of a given set.
   */
  private static long getNumMonths(Set<LocalDate> set) {
    return set.stream().mapToInt(d -> d.getMonthValue() + d.getYear()).distinct().count();
  }

  /**
   * Returns the monthly average stops reached by all modes of transportation.
   *
   * @return the monthly average stops reached by all modes of transportation.
   */
  static int getMonthlyAverageStopReached() {
    int res1 = 0;
    int res2 = 0;
    if (busStopsPerDay.keySet().size() != 0) {
      res1 =
          busStopsPerDay.values().stream().mapToInt(Integer::intValue).sum()
              / (int) getNumMonths(busStopsPerDay.keySet());
    }
    if (subwayStationsPerDay.keySet().size() != 0) {
      res2 =
          subwayStationsPerDay.values().stream().mapToInt(Integer::intValue).sum()
              / (int) getNumMonths(subwayStationsPerDay.keySet());
    }
    return res1 + res2;
  }

  /** A Fares enum class to hold the different fare rates of the transit system. */
  public enum Fares {
    FINE(6),
    MAX_FARE(6),
    busFare(2.0),
    subwayFare(0.5);

    private double fare;

    Fares(double fare) {
      this.fare = fare;
    }

    public double getFare() {
      return fare;
    }
  }
}
