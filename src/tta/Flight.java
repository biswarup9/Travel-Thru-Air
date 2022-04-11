package tta;

import java.util.HashMap;
import javax.servlet.Servlet;

public class Flight {
   String number;
   String departure;
   String arrival;
   int takeoff;
   int landing;
   int cost;
   public int full_cost = -1;

   public Flight(String number, String departure, int takeoff, String arrival, int landing, int cost) {
      this.number = number.toUpperCase();
      this.departure = departure.toUpperCase();
      this.takeoff = takeoff;
      this.arrival = arrival.toUpperCase();
      this.landing = landing;
      this.cost = cost;
   }

   public String getNumber(){
      return this.number;
   }

   public int getDuration() {
      int landing_min = landing%100;
      int landing_hr = landing/100;

      int takeoff_min = takeoff%100;
      int takeoff_hr = takeoff/100;

      int hr_diff = landing_hr - takeoff_hr;
      int min_diff = landing_min - takeoff_min;
      
      if(min_diff<0){
         min_diff += 60;
         hr_diff -= 1;
      }
      else if(min_diff>59){
         min_diff -= 60;
         hr_diff += 1;
      }
      return hr_diff*100+min_diff;
   }

   public String getDuration(int i){
      int duration = getDuration();
      int duration_min = duration%100;
      int duration_hr = duration/100;
      if(duration_hr == 0)
         return duration_min + " min";
      if(duration_min == 0)
         return duration_hr + " hr";
      return duration_hr + "hr "  + duration_min + "min";
   }

   public int getTakeOff() {
      return takeoff;
   }

   public int getLanding() {
      return landing;
   }

   public int getCost() 
   // Function to return a cost when a deal is present or not present (single routed flight)
   {
      int temp_cost = cost;
      String sector = departure + arrival;
      if(SearchServlet.deals_map.containsKey(sector)){
         HashMap<Integer, Integer> temp = SearchServlet.deals_map.get(sector); // Gets the HashMap which has discount price as value and expire time as a key
         for(int exp_time : temp.keySet()){
            if (exp_time>=takeoff){
               temp_cost = Math.min(temp.get(exp_time), temp_cost);
            }
         }
      }
      if(temp_cost != cost)
         full_cost = cost;
      return temp_cost;
   }

   public String getDep(){
      return departure;
   }

   public String getArr(){
      return arrival;
   }

   public int hashCode() {
      return this.number.hashCode();
   }

   public boolean equals(Object other) {
      if (this == other) {
         return true;
      }
      if (other instanceof Flight) {
         Flight f2 = (Flight) other;
         return this.number.equals(f2.number);
      }
      return false;
   }
}