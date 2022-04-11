package tta;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

// Extend HttpServlet class
public class SearchServlet extends HttpServlet {

   private static final long serialVersionUID = 1L;
   public static ArrayList<Deals> deals = new ArrayList<>(); // An ArrayList to store the objects from Deals.java
   
   public static HashMap<String, HashMap<Integer, Integer>> deals_map = new HashMap<>(); // A HashMap to store the deals with the sectors as keys
   // A HashMap which has discount price as value and expire time as a key is the value of the HashMap
   
   public static ArrayList<Flight> flights = new ArrayList<>();
   
   public static HashMap<String, HashSet<Flight>> airport_flights = new HashMap<>(); // string contains the departure (source) airport as the key
   // The value of the HashMap is a HashSet of all the flights.
   private String message = "~";

   private void getFlights(String path) 
   // Function to get all the flight paths from the flights.csv file
   {
      try {
         Scanner sc = new Scanner(new File(path));
         while (sc.hasNext()) {
            String[] row = sc.next().split(",");

            String number = row[0]; // Plane number
            String dep = row[1]; // Departure Airport abbreviation
            int takeoff = Integer.parseInt(row[2]); // Take-off time
            String arr = row[3]; // Landing Airport abbreviation
            int landing = Integer.parseInt(row[4]); // Landing time
            int cost = Integer.parseInt(row[5]); // Cost for the flight
            Flight new_flight = new Flight(number, dep, takeoff, arr, landing, cost); // object new_flight from class Flight.java
            flights.add(new_flight); // Store the object in the ArrayList
            
            HashSet<Flight> airport_flights_map; // HashSet to map out the flight to store as values in airport_flights HashMap

            if (!airport_flights.containsKey(dep))
               airport_flights_map = new HashSet<Flight>(); // New HashSet created as the departure key was not present  
            else
               airport_flights_map = airport_flights.get(dep); // Get the previous HashSet values from the present departure key.

            airport_flights_map.add(new_flight); //Add the new flights from the flights.csv to the HashSet

            if (!airport_flights.containsKey(arr))
               airport_flights.put(arr, new HashSet<Flight>()); // Create a HashSet for all the landing flights not already present (for multi-route)

            airport_flights.put(dep, airport_flights_map); // Put all the HashSet values created into the HashMap
         }
         sc.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private static void addNewDeal(String departure, String arrival, int exp, int discount_price) 
   // Function to store a new deal if not present or already present deal into a hash map
   {
      deals.add(new Deals(departure, arrival, exp, discount_price));
      String sector = departure + arrival;
      HashMap<Integer, Integer> temp; // A HashMap to store the discount price of a deal with its expire time as a key

      if (deals_map.containsKey(sector))
         temp = deals_map.get(sector);
      else
         temp = new HashMap<Integer, Integer>();

      temp.put(exp, discount_price);
      deals_map.put(sector, temp);
   }

   public static void addDeal(String departure, String arrival, int exp, int discount_price, String path)
   // Function to add a new deal to the deals.csv and add deal to the deals_map using the addNewDeal declared above
   {
      try {
         FileWriter csvWriter = new FileWriter(path, true);
         csvWriter.write(
               '\n' + departure + "," + arrival + "," + Integer.toString(exp) + "," + Integer.toString(discount_price));
         csvWriter.close();
         addNewDeal(departure, arrival, exp, discount_price);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private void getDeals(String path) 
   // Function to get the values of the deals from the deals.csv file and add add deal to the deals_map using the addNewDeal declared above
   {
      try {
         Scanner sc = new Scanner(new File(path));
         while (sc.hasNext()) {
            String[] row = sc.next().split(",");
            addNewDeal(row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]));
         }
         sc.close();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   private ArrayList<Deals> getActiveDeals() 
   // Function that stores the active deals (after current time) in a sorted order in an ArrayList and returns it
   {
      Calendar now = Calendar.getInstance();
      int time_now = 100 * now.get(Calendar.HOUR_OF_DAY) + now.get(Calendar.MINUTE); // Get the current time
      ArrayList<Deals> activeDeals = new ArrayList<Deals>(deals.stream() 
                                    .filter(deal -> deal.getExpiry() > time_now)
                                    .sorted(Comparator.comparing(Deals::getExpiry))
                                    .collect(Collectors.toList())); // Use the stream function of java taught in PPL
      return activeDeals;
   }

   private LinkedList<Route> getRoutes(String src, String dest, int time) 
   // Function that stores all the possible routes in a LinkedList and returns it
   {
      src = src.toUpperCase();
      dest = dest.toUpperCase();
      LinkedList<Route> routes = new LinkedList<>();
      LinkedList<Flight> flights = new LinkedList<>();
      this.getRoutes(src, dest, time, 0, "", flights, routes); // Calling the overloaded getRoutes present below
      routes.sort((r1, r2) -> {
         int res = r1.cost - r2.cost;
         if (res == 0) {
            res = r1.flights.size() - r2.flights.size();
            if (res == 0)
               return r1.flyingTime - r2.flyingTime;
            return res;
         }
         return res;
      });
      return routes;
   }

   private void getRoutes(String src, String dest, int time, int cost, String path, LinkedList<Flight> flights,
         LinkedList<Route> routes) 
   // Overloaded Function that uses DFS to search possible the routes 
   {
      if (!airport_flights.containsKey(dest) || !airport_flights.containsKey(src))
         return;
      if (src.equals(dest)) {
         if (flights.size() > 0)
            routes.add(new Route(flights));
         return;
      }
      for (Flight flight : airport_flights.get(src)) {
         if (flight.getTakeOff() < time)
            continue;
         String next_airport = flight.getArr(); 
         if (!path.contains(next_airport)) {
            LinkedList<Flight> flights_copy = new LinkedList<Flight>(flights);
            flights_copy.add(flight);
            getRoutes(next_airport, dest, flight.getLanding() + 30, cost + flight.getCost(), path + src + "~",
                  flights_copy, routes);
         }
      }
   }

   public void init() throws ServletException {
      try {
         ServletContext servletContext = getServletContext();
         String csvPath = servletContext.getRealPath("/WEB-INF/resources/deals.csv");
         String flightsPath = servletContext.getRealPath("/WEB-INF/resources/flights.csv");

         getDeals(csvPath);
         getFlights(flightsPath);

      } catch (Exception e) {
         StringWriter sw = new StringWriter();
         e.printStackTrace(new PrintWriter(sw));
         message += sw.toString();
      }
   }

   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   // Gets the information from the server
   {
      // Set response content type
      try {
         HttpSession session = request.getSession();
         request.setAttribute("showResults", false);
         session.setAttribute("deals", getActiveDeals()); // Binds all the sorted active deals using previous function to deals
         session.setAttribute("airports", airport_flights.keySet()); // Binds all the departure (source) airport keys to airports
         request.setCharacterEncoding("UTF-8");
         request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
      } catch (Exception e) {
         StringWriter sw = new StringWriter();
         e.printStackTrace(new PrintWriter(sw));
         PrintWriter out = response.getWriter();
         out.println(sw);
      }
   }

   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
   // Sends the information to the server
   {
      try {
         response.setContentType("text/html");
         String source = request.getParameter("source").toUpperCase();
         String destination = request.getParameter("destination").toUpperCase();
         int time = Integer.parseInt(request.getParameter("time").replace(":", ""));

         long startTime = System.nanoTime();
         LinkedList<Route> results = getRoutes(source, destination, time); // Linked list of routes and search result is created
         long endTime = System.nanoTime();

         request.setAttribute("execution_time", endTime - startTime);  // Sends/Posts the Time taken for the search 
         HttpSession session = request.getSession();
         session.setAttribute("source", source);
         session.setAttribute("destination", destination);
         session.setAttribute("time", request.getParameter("time"));
         request.setAttribute("showResults", true);
         session.setAttribute("deals", getActiveDeals());
         session.setAttribute("airports", airport_flights.keySet()); 
         request.setAttribute("results", results); // Sends/Posts the Linked list of routes and search result
         request.setCharacterEncoding("UTF-8");
         request.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(request, response);
      } catch (Exception e) {
         StringWriter sw = new StringWriter();
         e.printStackTrace(new PrintWriter(sw));
         PrintWriter out = response.getWriter();
         out.println(sw);
      }
   }

   public void destroy() {
      // do nothing.
   }
}