// A word of notice: avoid alerts as much as possible. 
// use console.log instead

function initialize() {
	$( "#loginButton" ).click(function( event ) {
		  event.preventDefault();
		  Login();
	});
	
	$( "#logoutButton1" ).click(function( event ) {
		  event.preventDefault();
		  Logout();
	});
	

	$( "#logoutButton2" ).click(function( event ) {
		  event.preventDefault();
		  Logout();
	});
	
	$( "#postMemento" ).click(function( event ) {
		  event.preventDefault();
		  PostMemento();
	});
	
	$( "#loginButton" ).submit(function( event ) {
		  event.preventDefault();
		  Login();
	});
	
	$( "#logoutButton1" ).submit(function( event ) {
		  event.preventDefault();
		  Logout();
	});
	
	$( "#logoutButton2" ).click(function( event ) {
		  event.preventDefault();
		  Logout();
	});
	
	$( "#postMemento" ).submit(function( event ) {
		  event.preventDefault();
		  PostMemento();
	});
	
	$( "#month").change(function( event ) {
		updateDaySelect();
	});
}


function Logout() {
	SetSessionKey("");
	SetPersonId("");
	SetPersonName("");
	SetUserId("");
	
	$("#loginForm")
			.html("<ul class='nav navbar-nav'></ul>"
			  +"<form class='navbar-form navbar-right'>"
              +"  <div class='form-group'>"
              +"    <input id='email' name='email' type='text' placeholder='Email' class='form-control'>"
              +"  </div>"
              +"  <div class='form-group'>"
              +"    <input id='password' name='password' type='password' placeholder='Password' class='form-control'>"
              +"  </div>"
              +"  <button id='loginButton' type='submit' class='btn btn-success'>Entra</button>"
              +"</form>");
	
	$( "#loginButton" ).click(function( event ) {
		  event.preventDefault();
		  Login();
	});
	$( "#loginButton" ).submit(function( event ) {
		  event.preventDefault();
		  Login();
	});
	return false;
}

// JavaScript Document
function PostMemento(){
	console.log("posting the memento");
	var publicMemento = new Object;
	publicMemento.headline = $("#headline").val();
	
	if (publicMemento.headline==null || publicMemento.headline =="") {
		$("#headlineLabel").css( "class","form-group has-error");
		$("#postMementoResult").html("<div class='alert alert-danger'>Il titolo non puo' essere vuoto</div>");
		return false;
	} else {
		$("#headlineLabel").css( "class","form-group has-success");
	}

	publicMemento.contributorId = GetUserId();
	publicMemento.locale = "it_IT";
	publicMemento.text = $("#text").val();
	publicMemento.author = $("#author").val();
	publicMemento.startLocation = new Object;
	
	var country = $("#country").val();
	var region = $("#region").val();
	var city = $("#city").val();
	publicMemento.startLocation.locale = "it_IT";	
	
	if (country != null && country!="") {
		publicMemento.startLocation.country = country;
	}
		
	if (region != null && region!="") {
		publicMemento.startLocation.region = region;
	}
	
	if (city != null && city!="") {
		publicMemento.startLocation.city = city;
	}
	
	publicMemento.startLocation.locale = "it_IT";	
	
	publicMemento.startDate = new Object;
	
	var year = $("#year").val();
	var month = $("#month").val();
	var day = $("#day").val();
	
	if (year != null && year!="") {
		publicMemento.startDate.year = year;
		publicMemento.startDate.decade = year - year%10;
	}
	if (month != null && month !="") {
		publicMemento.startDate.month = month;
	}
	if (day != null && day!="") {
		publicMemento.startDate.day = $("#day").val();
	}
	publicMemento.startDate.locale = "it_IT";
		
	
	publicMemento.resourceUrl = $("#resourceUrl").val();
	publicMemento.source = $("#source").val();
	publicMemento.sourceUrl = $("#sourceUrl").val();
	publicMemento.category = $("#category").val();
	publicMemento.resourceType = $("#resourceType").val();
	
	var contextId = $("#contextId").val();
	if (contextId !=null && contextId != "") {
		PostContextMemento(publicMemento,contextId);
	} else {	
		SaveStoryWithConnection(publicMemento);
	}
		
	return false;
}

// JavaScript Document
function PostContextMemento(publicMemento,contextId){
	console.log("posting the memento to a context with Id "+contextId);
	var contextMemento = new Object;
	contextMemento.publicMemento = publicMemento;
	contextMemento.level = "WORLD";
	if (publicMemento.startLocation.country != null && publicMemento.startLocation.country.toLowerCase()=="italia") {
		if (publicMemento.startLocation.region !=null && publicMemento.startLocation.region!="") {
			if (publicMemento.startLocation.city !=null && publicMemento.startLocation.city!="") {
				contextMemento.level="CITY";
			} else {
				contextMemento.level="REGION";
			}
		} else {
			contextMemento.level="COUNTRY";
		}
	} 

	contextMemento.decade = publicMemento.startDate.decade;
	contextMemento.type = publicMemento.resourceType;
	contextMemento.category= publicMemento.category;	

	var sessionKey = GetSessionKey();
	$.ajax({
        	type: "POST",			
			beforeSend: function (request)
            {
				$("#postMementoResult").html("");
    			$("#postMementoResult").html("<div class='alert alert-warning'>Salvando...</div>");
                request.setRequestHeader("PLAY_SESSION", sessionKey);
            },
			url:GetBaseUrl() + "/lifeapi/context/"+contextId+"/memento",
        	data: JSON.stringify(contextMemento),
        	async: false,
        	success: function (data) {
				$("#postMementoResult").html("<div class='alert alert-success'>Salvato!</div>");
				headline = $("#headline").val("");
				$("#text").val("");
				$("#country").val("");
				$("#region").val("");
				$("#city").val("");
				$("#year").val("");
				$("#month").val("");
				$("#day").val("");
				$("#resourceUrl").val("");
				$("#author").val("");
				$("#category").val("SONG");
				$("#sourceUrl").val("");
				$("#resourceType").val("IMAGE");
        	},
        	error: function (data) {
				$("#postMementoResult").html("<div class='alert alert-danger'>E' successo un errore "
				+data+"</div>");
        	},
        	dataType: "json",
			contentType: "application/json"
        });


	return false;
}

function SaveStoryWithConnection(publicMemento)
{
	var sessionKey = GetSessionKey();
	$.ajax({
        	type: "POST",			
			beforeSend: function (request)
            {
				$("#postMementoResult").html("<div class='alert alert-warning'>Salvando...</div>");
                request.setRequestHeader("PLAY_SESSION", sessionKey);
            },
			url:GetBaseUrl() + "/lifeapi/context/memento",
        	data: JSON.stringify(publicMemento),
        	async: false,
        	success: function (data) {
				$("#postMementoResult").html("<div class='alert alert-success'>Salvato!</div>");
				headline = $("#headline").val("");
				$("#text").val("");
				$("#country").val("");
				$("#region").val("");
				$("#city").val("");
				$("#year").val("");
				$("#month").val("");
				$("#day").val("");
				$("#resourceUrl").val("");
        	},
        	error: function (data) {
				$("#postMementoResult").html("<div class='alert alert-danger'>E' successo un errore "
				+data+"</div>");
        	},
        	dataType: "json",
			contentType: "application/json"
        });
}

function Login() {
	var sessionData = new Object;
	sessionData.email = $("#email").val();
	sessionData.password = $("#password").val();
	var localBaseUrl = GetBaseUrl();
	console.log("Using this API URL: " + localBaseUrl);
	if (localBaseUrl == null || localBaseUrl == "") {
		if (window.location.hostname == "http://base.reminiscens.me") {
			localBaseUrl = "http://base.reminiscens.me";
		} else if (window.location.hostname == "localhost"){
			localBaseUrl = "http://localhost";
		} else {
			localBaseUrl = "http://test.reminiscens.me";
		}
		console.log("Setting API URL to: " + localBaseUrl);
		SetBaseUrl(localBaseUrl);
	}
	console.log("Login with: " + localBaseUrl + "/lifeapi/user/login");
	$.ajax({
		type : "POST",
		//url: "http://test.reminiscens.me/lifeapi/user/login",
		//url : "http://base.reminiscens.me/lifeapi/user/login",
		url : localBaseUrl + "/lifeapi/user/login",
		data : JSON.stringify(sessionData),
		async : false,
		success : function(data) {
				SetSessionKey(data.sessionKey);
				SetPersonId(data.person.personId);
				SetPersonName(data.person.firstname+" "+data.person.lastname);
				SetUserId(data.userId);
				var username = GetPersonName();
				$("#loginForm").html(
					"<ul class='nav navbar-nav'><li class='active'><a href='#'>Benvenuto "
							+ username
							+ "!</a></li></ul>"
							+ "<form class='navbar-form navbar-right'><button id='logoutButton2' type='submit' class='btn btn-danger'>Logout</button></form>");
				
				
				 $( "#logoutButton2" ).click(function( event ) {
			          event.preventDefault();
			          Logout();
			    });

			    $( "#logoutButton2" ).submit(function( event ) {
			          event.preventDefault();
			          Logout();
			    });
				
				return false;
			},
			error : function(data) {
				alert("L'email o la password inseriti sono sbagliati");
				return false;
			},
			dataType : "json",
			contentType : "application/json"
		});
	return false;
}

function updateDaySelect() { 
	var month = $("#month").val();
    if (month != 0) {
        var monthInt = parseInt(month);
        var j = 1;
        var selectHTML = "<select id='day' name='day' class='form-control'><option value='" + 0 + "'>" + "giorno" + "</option>";
        switch (monthInt) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                for (var i = 1; i <= 31; i++) {
                   selectHTML += "<option value='" + i + "'>" + i + "</option>";
                   j++;
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                for (var i = 1; i <= 30; i++) {
                	selectHTML += "<option value='" + i + "'>" + i + "</option>";
                    j++;
                }
                break;
            case 2:
                for (var i = 1; i <= 28; i++) {
                	selectHTML += "<option value='" + i + "'>" + i + "</option>";
                    j++;
                }
                break;
        }

        selectHTML += "</select>";
        $("#day").html(selectHTML);
        $('#day').fadeIn('slow');
    }
}

