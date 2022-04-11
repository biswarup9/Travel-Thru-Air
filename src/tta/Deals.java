package tta;

public class Deals{
   String departure;
   String arrival;
   int expiry;
   int discounted_price;

   public Deals( String departure, String arrival, int expiry, int discounted_price){
      this.departure = departure;
      this.arrival = arrival;
      this.expiry = expiry;
      this.discounted_price = discounted_price;
   }

   public String getSector(){
      return departure + "-" + arrival;
   }

   public int getExpiry(){
      return this.expiry;
   }
   
   public String getstrExpiry(){
	      int a =  this.expiry;
	      int b = a/100;
		  int c = a-b*100;
		  if(c!=0) 
		  {
			  return String.valueOf(b) + ":" + String.valueOf(c);
		  }
		  else
		  {
			  return String.valueOf(b) + ":" + "00";
		  }
	   }

   public int getPrice(){
      return this.discounted_price;
   }

   public String toString(){
      return this.getSector();
   }
}