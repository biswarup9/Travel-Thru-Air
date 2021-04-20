<%@ page trimDirectiveWhitespaces="true" %>
<%@ page import="java.util.Set"%> 
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title>Admin | Travel Thru Air</title>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <meta
      name="description"
      content="BCSEIII Internet Technology Lab Assignment 3"
    />
    <meta name="author" content="Biswarup Ray | 82" />
    <link
      rel="stylesheet"
      href="https://stackpath.bootstrapcdn.com/bootswatch/4.5.2/simplex/bootstrap.min.css"
    />
  </head>
  <body class="pt-5">
    <div class="container pt-5 mt-5">
        <p class="display-4 text-center">Deals Management</p>
        <div class="col-6 offset-3 bg-white shadow border rounded">
            <form class="pt-4 mt-3 px-3" method="POST" action="">
                <div class="form-row">
                    <div class="col">
                        <input placeholder="Departure" class="form-control" list="airport_codes" required name="departure" autocomplete="off"/>
                    </div>
                    <div class="col">
                        <input placeholder="Arrival" class="form-control" list="airport_codes" required name="arrival" autocomplete="off"/>
                    </div>
                </div>      
                <div class="form-row mt-2">
                    <div class="col">
                        <input class="form-control" type="number" required name="cost" placeholder="Discounted Price">
                    </div>
                    <div class="col">
                      <div class="form-group row">
                        <label class="col-4 text-right col-form-label">Valid till</label>
                        <div class="col-8">
                            <input class="form-control" type="time" required name="expiry">
                        </div>
                    </div>
               </div>
               </div>
            <button class="btn btn-block btn-primary">Submit</button>
            <p class="w-100 text-success lead text-center py-2 mb-0"><span class=<%=request.getAttribute("message")%>>Success</span></p>
            </form>
        </div>
    </div>
    <datalist id="airport_codes">
    <%
        Set<String> airports = (Set)request.getAttribute("airports");
        if(airports != null)
            for ( String airport: airports){%>
                <option value=<%= airport %>>
    <%}%>
   </datalist>
  </body>
</html>