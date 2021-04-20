<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.Set"%> 
<%@ page import="java.util.ArrayList"%> 
<%@ page import="java.util.LinkedList"%> 
<%@ page import="java.text.NumberFormat"%> 
<%@ page import="java.util.Locale"%> 
<%@ page import="tta.Deals"%> 
<%@ page import="tta.Route"%> 
<%@ page import="tta.Flight"%> 
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Travel Thru Air</title>    
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta name="description" content="BCSEIII Internet Technology Lab Assignment 3">
    <meta name="author" content="Biswarup Ray | 82">
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootswatch/4.5.2/simplex/bootstrap.min.css"
    />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
      .search-bar input {
        font-weight: bolder;
        font-size: x-large;
        text-transform:uppercase;
      }
      .search-bar label {
        font-weight: bolder;
      }
      .w-10{
        width: 24% !important;
      }
      .w-45{
        width: 33% !important;
      }
      .discounted{
        text-decoration: line-through;       
      }
    </style>
    <script>
      function scroller(){
        document.getElementById("results").scrollIntoView({ behavior: "smooth" });
      }
    </script>
  </head>
  <body>
    <nav class="navbar navbar-light bg-light">
      <span class="navbar-brand">
        <img src="images/logoreal.png" style="width: 10em" />
      </span>
    </nav>
    <div
      class="jumbotron jumbotron-fluid rounded-0 text-light"
      style="
        background-image: url(./images/airbackbig.png);
      "
    >
      <div id="dealContainer" class="carousel slide" data-ride="carousel">
        <div class="carousel-inner">
          <div class="carousel-item active">
            <div class="container">
              <div class="row px-5">
                <div class="col-md-12 text-center">
                  <p class="display-4 mb-0">Get Best Deals here</p>
                  <p class="lead">Search Domestic And International Flights</p>
                </div>
              </div>
            </div>
          </div>

          <%-- Deals --%>
          <%
          ArrayList<Deals> deals = (ArrayList)session.getAttribute("deals");
          for ( int idx = 0; idx < deals.size(); idx++){ %>          
            <div class="carousel-item ">
              <div class="container">
                <div class="row px-5">
                  <div class="col-md-6 text-sm-right text-center">
                    <p class="display-4 mb-0"><%= deals.get(idx).getSector() %></p>
                    <p class="lead">Valid till <%= deals.get(idx).getstrExpiry() %></p>
                  </div>
                  <div class="col-md-6 pt-3 text-sm-left text-center">
                    <p class="display-4"><small class="font-weight-light">&#8377;</small><%= deals.get(idx).getPrice() %></p>
                  </div>
                </div>
              </div>
            </div>
          <%}%>

        </div>
        <a
          class="carousel-control-prev"
          role="button"
          data-slide="prev"
          onclick="$('#dealContainer').carousel('prev')"
        >
          <span class="carousel-control-prev-icon" aria-hidden="true"></span>
          <span class="sr-only">Previous</span>
        </a>
        <a
          class="carousel-control-next"
          role="button"
          data-slide="next"
          onclick="$('#dealContainer').carousel('next')"
        >
          <span class="carousel-control-next-icon" aria-hidden="true"></span>
          <span class="sr-only">Next</span>
        </a>
      </div>
    </div>
  <form method="POST" action="" onsubmit=onclick="document.getElementById('form-submit').style.display='none';document.getElementById('form-spinner').style.display='inline-block'">
    <div class="container">
      <div class="row shadow-lg py-3 px-1 rounded border search-bar bg-light">
        <div class="col-md-5 col-6 pr-0">
          <div class="form-group border shadow-sm py-2 px-2 rounded-left bg-white">
            <label for="source" class="control-label">From</label>
            <input
              placeholder="Select Departure Airport"
              class="border-0 form-control py-0 my-0"
              id="source"
              name="source"
              list="airport_codes"
              autocomplete="off"
              required              
              <%
                if (session.getAttribute("source")!= null) {
                  out.println("value = " + session.getAttribute("source")); 
               }
              %>
            />
          </div>
        </div>
        <div class="col-md-5 col-6 pl-0 pr-md-0">
          <div class="form-group shadow-sm border py-2 px-2  bg-white">
            <label for="destination" class="control-label">To</label>
            <input
              placeholder="Select Arrival Airport"
              class="border-0 form-control py-0 my-0"
              id="destination"
              name="destination"
              list="airport_codes"
              autocomplete="off"
              required
              <%
                if (session.getAttribute("destination")!= null) {
                  out.println("value = " + session.getAttribute("destination")); 
               }
              %>
            />
          </div>
        </div>
        <div class="col-md-2 col-12 pl-md-0">
          <div class="form-group shadow-sm border rounded-right py-2 px-2  bg-white">
            <label for="time" class="control-label">Departure Time</label>
            <input
              placeholder="Select Airport"
              class="border-0 form-control py-0 my-0"
              id="time"
              name="time"
              type="time"
              step="900"
              required
              <%
                if (session.getAttribute("time")!= null) {
                  out.println("value = " + session.getAttribute("time")); 
               }
              %>
            />
          </div>
        </div>
        <div class="col-12 text-center">
          <button class="btn btn-primary w-25 font-weight-bold shadow" type="submit" id="form-button">Search</button>          
          <button class="btn btn-primary w-25 font-weight-bold shadow" style="display:none" id="form-spinner" disabled><span class="spinner-grow spinner-grow-sm"></span></button>
        </div>
      </div>
    </div>
  </form>
  <div class="container mb-5" id="results">
    <div class="row">
      <%
        if (request.getAttribute("showResults") != null && Boolean.parseBoolean(request.getAttribute("showResults").toString())){
          %>
            <script>
              window.onload = scroller;
            </script>
          <%
          LinkedList<Route> search_results = (LinkedList)request.getAttribute("results");
          %>
            <div class="col-12 mt-5 text-center">
              <p class="display-4 mb-1">
                Results
              </p>
              <% if(session.getAttribute("destination").equals(session.getAttribute("source"))){%><p class="lead">Please enter different Departure and Arrival airport</p><%}%>
              <% else { if(request.getAttribute("execution_time")!=null){%><p class="mb-0">Search took <%=((Long)request.getAttribute("execution_time")/10000000.0)%>ms </p><%}%>              
              <% if(search_results.size()==0){%><p class="lead">No Flights Found</p><%}}%>
            </div>
          <%
          for( Route route : search_results ){%>
            <div class="col-12">
              <div class="card my-3 bg-light shadow">
              <div class="row">
                <div class="col">
                  <ul class="list-group list-group-flush">
                  <% for(Flight flight : route.getFlights()){%>
                    <div class="list-group-item bg-light">
                      <div class="row">
                        <div class="col-sm-3">
                          <p class="display-4 mb-0"><%=flight.getNumber()%></p>
                        </div>
                        <div class="col-sm-5">
                          <table class="w-100 text-center">
                            <tr>
                              <th class="text-right h3 w-45 pr-2"><%=flight.getDep()%></th>
                              <td class="w-10"><img src="images/planes.png" style="width:7em"/></td>
                              <th class="text-left h3 w-45 pl-2"><%=flight.getArr()%></th>
                            </tr>
                            <tr>
                              <td class="text-right pr-2"><%=String.format("%04d", flight.getTakeOff())%></td>                            
                              <td class="text-muted text-sm"><small>&mdash; <%=flight.getDuration(0)%> &mdash;</small></td>
                              <td class="text-left pl-2"><%=String.format("%04d", flight.getLanding())%></td>
                            </tr>
                          </table>
                        </div>
                        <div class="col-sm-2">
                        <%if(flight.full_cost>-1){%>
                          <p class="discounted lead pt-2 mb-0 text-right">&#8377;<%=NumberFormat.getNumberInstance(Locale.US).format(flight.full_cost)%></p>
                        <%}%>
                        </div>
                        <%if(route.getFlights().size()>1){%>
                        <div class="col-sm-2">
                          <p class="h2 pt-2 mb-0 text-right">                            
                            &#8377;<%=NumberFormat.getNumberInstance(Locale.US).format(flight.getCost())%>
                          </p>
                        </div>
                        <%}%>         
                      </div>
                    </div>
                  <%}%>
                  </ul>
                </div>
                  <div class="col-2">
                  <table class="w-100 h-100">
                          <tr>
                          <td class="mb-0 text-right pr-3">                          
                            <span class="h1">&#8377;<%=NumberFormat.getNumberInstance(Locale.US).format(route.getCost())%></span>
                          </td>
                          </tr>
                          </table>
                  </div>
                </div>
              </div>
            </div>
          <%}      
        }
      %>
    </div>
  </div>
    <datalist id="airport_codes">
    <%
      Set<String> airports = (Set)session.getAttribute("airports");
      if(airports != null)
        for ( String airport: airports){%>
          <option value=<%= airport %>>
          <%}%>
   </datalist>
  </body>
</html>