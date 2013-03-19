<%-- 
    Document   : index
    Created on : 1-ott-2012, 15.08.43
    Author     : francesco
--%>

<%@page errorPage="errorPage.jsp"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%
    String album_id = request.getParameter("album_id");
    String url = request.getParameter("url");
    String title = request.getParameter("title");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
        <link href="bootstrap/css/bootstrap.css" rel="stylesheet"/>
        <title>Map</title>
        <style type="text/css">

            .hero-unit p{
                font-size: 14px;
                line-height: 20px 
            }
            #address{
                width:170px;
                float: right;
            }
            #right{
                width: 350px;
            }
            #map_canvas { 
                height: 300px;
                width: 500px;
                margin-bottom: 20px;
                margin-top: 30px;
                margin-left: 15px;
            }

            #sfondo { 
                width: 940px;
                padding-bottom: 520px;
                margin-top: 40px;
                margin-left: auto;
                margin-right: auto;
            }
            .modal p{
                text-align: center;
                font-size: 20px
            }
            #mytext{
                width: 335px;
                height: 340px;
                margin-top: 20px;
                margin-bottom: 20px;
                resize: none;
            }
            #image { 
                max-height: 80px; 
            }
            #box{
                background-color: white;
                padding: 20px;
            }
        </style>
        <script src="bootstrap/js/jquery-1.8.2.min.js"></script>
        <script type="text/javascript"
                src="http://maps.googleapis.com/maps/api/js?key=AIzaSyAfhwuXTgopU7b9KHaTB9YDZwQ6LdPbLJ8&sensor=false">
        </script>
        <script src="http://dev.kucherbaev.com/CrowdMemories/cm-func.js"></script>
        <script src="bootstrap/js/bootstrap.js"></script>
        <script src="myMapMarkerCircle.js"></script>
        <script type="text/javascript">
           
            function checkDescription(){
                var text = document.getElementById('mytext').value;
               
                if(text == ""){
                    $("#myModalDescription").modal('show');
                    return false;
                }else{
                    return true;
                }  
            }
            
            function checkLocation(){
                var lat = document.getElementById('lat').value;
                var lng = document.getElementById('lng').value;
                
                if(lat == "" ||lng == ""){
                    $("#myModalLocation").modal('show');
                    return false;
                }else{
                    return true;
                }           
            }
            
        </script>

    </head>
    <body onload="initialize()">
        <div class="container">
            <div id="sfondo" class="hero-unit">
                <div class="row" style="margin-bottom: 20px">
                    <div class="span10">
                        <h1 >Do you remember <br> the location?</h1>   
                    </div>  
                    <div class="span2" style="background: white;padding: 5px;margin-left: 0px;text-align: center;">
                        <form method="get" action="date.jsp">
                            <button class="btn btn-mini pull-right" type="submit" name="submit" value="skip" style="margin-bottom: 5px;"> Skip <i class="icon-forward"></i></button>
                            <div style="display: none;">
                                <input name="album_id" value="<%=album_id%>">
                                <input name="url" value="<%= url%>">
                                <input name="title" value="<%= title%>">
                            </div>
                        </form>
                        <img id="image" src="<%= url%>" class="img-rounded"> 
                        <p style="margin: 0 0 0px;"><%= title%></p>
                    </div>
                </div>

                <div class="span7" >
                    <p>
                        <i class="icon-arrow-right"></i> If you remember exactly the location just drag the marker on the map over the location or select with a rightclick on the map the area in which you think it was.
                    </p>
                    <p style="margin-right: 10px"> 
                        <i class="icon-arrow-right"></i> You can also search a place!  
                        <input type="button" class="btn btn-inverse pull-right" value="Search" onclick="codeAddress()">
                        <input id="address" name="address" class="input-medium search-query" type="text" placeholder="Insert place here" >
                    </p>


                    <div id="map_canvas" ></div>

                    <form method="get" action="date.jsp" >
                        <button class="btn btn-success btn-large btn-block" type="submit" name="submit" value="map" onclick="return checkLocation()"> Add the location on the map</button>

                        <div style="display: none; margin-top: 652px">
                            <input name="album_id" value="<%=album_id%>">
                            <input name="url" value="<%= url%>">
                            <input name="title" value="<%= title%>">
                            <table class="table" style="margin-top: 40px; width: 700px;">
                                <thead>
                                <th>Latitudine</th>
                                <th>Longitudine</th>
                                <th>Radius</th>
                                <th>Indirizzo</th>
                                </thead>
                                <tr>
                                    <td >
                                        <input id="lat" name="lat" type="text" value=""/>
                                    </td>
                                    <td >
                                        <input id="lng" name="lng" type="text" value=""/>
                                    </td>
                                    <td >
                                        <input id="radius" name="radius" type="text" value="0"/>
                                    </td>
                                    <td >
                                        <input id="indirizzo" name="indirizzo" type="text" value=""/>
                                    </td>
                                </tr>
                            </table>
                        </div>

                    </form>
                </div>

                <div id="right" class="span4" >
                    <form method="get" action="date.jsp">
                        <p> <i class="icon-arrow-right"></i> Otherwise you can describe it textually.</p>
                        <textarea id="mytext" name="description" placeholder="Here you can describe textually what you remember of the location!"></textarea>
                        <button class="btn btn-success btn-large btn-block" type="submit" name="submit" value="text" onclick="return checkDescription()"> or add the description </button>

                        <div style="display: none">
                            <input name="album_id" value="<%=album_id%>">
                            <input name="url" value="<%= url%>">
                            <input name="title" value="<%= title%>">
                        </div>           

                    </form>
                </div>

            </div>
        </div>

        <div id="myModalDescription" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">Warning!</h3>
            </div>
            <div class="modal-body">
                <p>The description you entered could not be Empty!</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Close</button>
            </div>
        </div>

        <div id="myModalLocation" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="myModalLabel">Warning!</h3>
            </div>
            <div class="modal-body">
                <p>Please check the location you entered!</p>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" data-dismiss="modal" aria-hidden="true">Close</button>
            </div>
        </div>

    </body>
</html>