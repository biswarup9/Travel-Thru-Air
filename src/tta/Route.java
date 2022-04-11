package tta;

import java.util.LinkedList;


public class Route {
   int cost;
   int start;
   int end;
   int flyingTime;
   LinkedList<Flight> flights;

   public Route(LinkedList<Flight> flights){
      int cost = 0;
      int start = -1;
      int end = 0;
      // Get a combined total cost for all the flights present in the route
      for( Flight flight : flights){
         if(start<0)
            start = flight.getTakeOff();
         cost += flight.getCost();
         end = flight.getLanding();
         flyingTime += flight.getDuration();
      }
      this.cost = cost;
      this.flights = flights;
   }

   public int getStart() {
      return start;
   }

   public int getEnd() {
      return end;
   }

   public int getCost() {
      return cost;
   }

   public int getDuration() {
      return end - start;
   }

   public LinkedList<Flight> getFlights() {
      return flights;
   }

   public void addFlight(Flight flight) throws Exception {
      if (this.end > 0 && (flight.getTakeOff() < (this.end + 30)))
         throw new Exception("Flights Overlap");

      this.cost += flight.getCost();
      if (this.start < 0)
         this.start = flight.getTakeOff();
      this.end = flight.getLanding();
      this.flights.add(flight);
   }
}