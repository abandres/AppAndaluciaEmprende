// JavaScript Document
//VARIABLES GLOBALES

var TITLE = "Andalucía Emprende";
//RSS url
//var RSS = "http://192.168.0.185/comunes/xml/";
//var RSS = "http://192.168.0.185:8080/S09138Java/servlets/app/RSS/";
//var URL_FICHAS = "http://192.168.0.185/ajax/carga_vista/";

var RSS = "http://10.244.75.35/S09138Java/servlets/app/RSS/";
var URL_FICHAS = "http://10.244.75.35/ajax/carga_vista/";

// cantidad de registros por página
var numero = 10;
//Stores entries
var entries = [];
var selectedEntry = "";
var appReady = false;
var url2="";
var userPrefs="";

var jsonIni = "0";

// EVENTOS DE OBJETOS DEL INDEX.HTML
/**
 * Control del load y deviceready 
 */
//Call onDeviceReady when PhoneGap is loaded.
//
// At this point, the document has loaded but phonegap.js has not.
// When PhoneGap is loaded and talking with the native device,
// it will call the event `deviceready`.
//
function onLoad() {
	appReady = false;
    document.addEventListener("deviceready", onDeviceReady, false);
}

// PhoneGap is loaded and it is now safe to make calls PhoneGap methods
//
function onDeviceReady() {
    // Now safe to use the PhoneGap API
	appReady = true;
	//Leemos las preferencias del usuario
	if(localStorage["userPrefs"]){
		userPrefs = JSON.parse(localStorage["userPrefs"]);
		//recupera y pinta las variables que haya seleccionado el usuario en su móvil
		pintaConf(userPrefs);
	} else {
		userPrefs = JSON.parse("{\"push\": \"on\"}");
	}

	PhoneGap.exec(
            null,           // called when signature capture is successful
            null,           // called when signature capture encounters an error
            'PushNotificationPlugin',  // Tell PhoneGap that we want to run "PushNotificationPlugin"
            'appReady',        		   // Tell the plugin the action we want to perform
            []);              // List of arguments to the plugin
	
	PhoneGap.exec(
            null,           // called when signature capture is successful
            null,           // called when signature capture encounters an error
            'PushNotificationPlugin',  // Tell PhoneGap that we want to run "PushNotificationPlugin"
            'pushConf',    		   // Tell the plugin the action we want to perform
            [userPrefs]);                       // List of arguments to the plugin
}

         


//selecciona todas las provincias el checkbox de configuración de notificaciones
//$('#noticiaAll').checkAll('#noticiasForm input:checkbox:not(#noticiaAll)');

// al cargarse el panel listado* limpiamos el título y la ficha de visualización
$("#listadoNoticias").live("pagebeforeshow", function(event,data) {
	if(data.prevPage.length) {
		//$("h1", data.prevPage).text("");
		$("#entryText", data.prevPage).html("");
	};
});
$("#listadoFormacion").live("pagebeforeshow", function(event,data) {
	if(data.prevPage.length) {
		//$("h1", data.prevPage).text("");
		$("#entryText", data.prevPage).html("");
	};
});
$("#listadoEncuentros").live("pagebeforeshow", function(event,data) {
	if(data.prevPage.length) {
		//$("h1", data.prevPage).text("");
		$("#entryText", data.prevPage).html("");
	};
});

// Al hacer clic en un elemento de las listas, se llama a cargar la ficha
$(".contentLink").live("click", function() {
	selectedEntry = $(this).data("entryid");
	// preparar las variables para descargar el contenido correcto.
	var id2 = entries[selectedEntry].link.slice(entries[selectedEntry].link.lastIndexOf("/")+1);
	abreFichaNotificacion($(this).attr("data-entryTipo"),id2);
});

// Al entrar en un listado cargamos con ajax el RSS correspondiente
$("#listadoNoticias").live("pageshow", function(prepage) {
	//Set the title
	$("h3", this).text("Últimas Noticias");
	cargaTitularesNoticias(true,false);
});

$("#listadoFormacion").live("pageshow", function(prepage) {
	//Set the title
	$("h3", this).text("Próximos eventos de formación");
	cargaTitularesFormacion(true,false);
});


$("#listadoEncuentros").live("pageshow", function(prepage) {
	//Set the title
	$("h3", this).text("Próximos encuentros");
	cargaTitularesEncuentros(true,false);
});

// antes de pintarse la ficha se descargan los datos
$(document).delegate("#contentPageNoticias", "pageshow", function(prepage) {
	url2 = url2+"/"+ (new Date().getTime());
	$.ajax({
		url:url2,
		success:function(res,code) {
			try{
				$("#entryTextNoticias").html(res);
			} catch (ex){
				error(ex.message);
				alert("no se ha podido cargar el contenido.");
			}
		},
		error:function(jqXHR,status,error) {
			error(error);
			alert("no se ha podido cargar el contenido.");
		}
	});
});

//antes de pintarse la ficha se descargan los datos
$(document).delegate("#contentPageFormacion", "pageshow", function(prepage) {
	url2 = url2+"/"+ (new Date().getTime());
	$.ajax({
		url:url2,
		success:function(res,code) {
			try{
				$("#entryTextFormacion").html(res);
			} catch (ex){
				error(ex.message);
				alert("no se ha podido cargar el contenido.");
			}
		},
		error:function(jqXHR,status,error) {
				error(error);
				alert("no se ha podido cargar el contenido.");
		}
	});
});

//antes de pintarse la ficha se descargan los datos
$(document).delegate("#contentPageEncuentros", "pageshow", function(prepage) {
	url2 = url2+"/"+ (new Date().getTime());
	$.ajax({
		url:url2,
		success:function(res,code) {
			try{
				$("#entryTextEncuentros").html(res);
			} catch (ex){
				error(ex.message);
				alert("no se ha podido cargar el contenido.");
			}
		},
		error:function(jqXHR,status,error) {
			error(error);
			alert("no se ha podido cargar el contenido.");
		}
	});
});
//
//$("#contentPage").live("pageshow", function(prepage) {
//	url2 = url2+"/"+ (new Date().getTime());
//	alert(url2);	
//	$.ajax({
//		url:url2,
//		success:function(res,code) {
//			try{
//				alert(res);
//				$("#entryText").html(res);
//			} catch (ex){
//				alert("ERROR: "+ex);
//			}
//		},
//		error:function(jqXHR,status,error) {
//			alert("ERROR");
//			alert(jqXHR);
//			alert(status);
//			alert(error);
//		},
//		always: function(){
//			alert("always");
//		}
//	});
//});

//FUNCIONES

// prepara la url para cargar más noticias, formacion o encuentros
// la variable vieneDeInicio dice si está llamando desde el botón de inicio, en cuyo caso
// no se cargan más registros salvo que no haya ninguno
// la variable resetea se utiliza para limpiar la lista cuando se cambia el filtro de canal
function cargaTitularesNoticias(vieneDeInicio,resetea){
	if (resetea){
		$("#linksListNoticias").html('');		
	}	
	var primero = $("#linksListNoticias> li").length;
	if (!vieneDeInicio || primero == 0){
		var url = RSS + "noticias" + "/" + primero + "/" + numero ;	
		var canal=$("#provinciaNoticias").val();		
		if (canal!='0'){
			url = url + "/" + canal;
		}
		cargaTitulares(url,"noticias");
	}
}

function cargaTitularesFormacion(vieneDeInicio,resetea){
	if (resetea){
		$("#linksListFormacion").html('');		
	}
	var primero = $("#linksListFormacion> li").length;	
	if (!vieneDeInicio || primero == 0){
		var url = RSS + "cursos" + "/" + primero + "/" + numero ;	
		var canal=$("#provinciaFormacion").val();
		if (canal!='0'){
			url = url + "/" + canal;
		}
		cargaTitulares(url,"eventos");
	}
}

function cargaTitularesEncuentros(vieneDeInicio,resetea){
	if (resetea){
		$("#linksListEncuentros").html('');		
	}
	var primero = $("#linksListEncuentros> li").length;	
	if (!vieneDeInicio || primero == 0){
		var url = RSS + "encuentros" + "/" + primero + "/" + numero ;	
		var canal=$("#provinciaEncuentros").val();
		if (canal!='0'){
			url = url + "/" + canal;
		}
		cargaTitulares(url,"encuentros");
	}
}

//carga la lista de elementos a pintar
function cargaTitulares(url,seccionActual){
	$.ajax({
		url:url,
		success:function(res,code) {
			try{
				entries = [];
				var xml = $(res);
				var items = xml.find("item");
				$.each(items, function(i, v) {
					entry = { 
						title:$(v).find("title").text(), 
						link:$(v).find("link").text(),
						pubDate:$(v).find("pubDate").text(),
						description:$.trim($(v).find("description").text()
						)
					};
					entries.push(entry);
				});
				$("#status").html("");
				//store entries
				//localStorage["entries"] = JSON.stringify(entries);
				renderEntries(seccionActual,entries);
			} catch (ex){
				error(ex.message);
				alert("no se ha podido cargar el contenido.");
			}
		},
		error:function(jqXHR,status,error) {
			try{
					$("#status").html("Lo sentimos, no podemos obtener los datos.");
			} catch (ex){
				error(ex.message);
				alert("no se ha podido cargar el contenido.");
			}
		}
	});
}

// pinta el lista de elementos descargados
function renderEntries(seccionActual,entries) {
	try{
		var s = '';
		var contenidoURL = '';
		var lista = "";
		if (seccionActual == ("noticias")){
			lista="#linksListNoticias";
			contenidoURL="#contentPageNoticias";
		} else if (seccionActual == ("eventos")){
			lista="#linksListFormacion";
			contenidoURL="#contentPageFormacion";
		} else if (seccionActual == ("encuentros")){
			lista="#linksListEncuentros";
			contenidoURL="#contentPageEncuentros";
		}	
		$.each(entries, function(i, v) {
			//recuperamos el id de la cadena del xml <link>
			var d = new Date(Date.parse(v.pubDate));
			var fecha = rellenarPorLaIzquierda(d.getDate().toString(),'0',2)+"/"+rellenarPorLaIzquierda(d.getMonth().toString(),'0',2)+"/"+d.getFullYear().toString().substr(2,2);
		    s += '<li><a href="'+contenidoURL+'" class="contentLink" data-entryid="'+i+'" data-entryTipo="'+seccionActual+'">' + fecha + ' - ' + v.title + '</a></li>';
		});
		$(lista).html($(lista).html()+s);
		$(lista).listview("refresh");
	} catch (error){
				error(error.message);
				alert("no se ha podido cargar el contenido.");
	}
}

// 
function abreFichaNotificacion(tipo,id){
	// preparar las variables para descargar el contenido correcto.
	
	var pagina = "contentPageNoticias";
	
	if (tipo.indexOf("noticias")!=-1){		
		url2 = URL_FICHAS + "noticiaAPP/" + id;
		
	} else if (tipo.indexOf("eventos")!=-1){		
		url2 = URL_FICHAS + "formacionAPP/" + id;
		pagina = "contentPageFormacion";
	} else if (tipo.indexOf("encuentros")!=-1){		
		url2 = URL_FICHAS + "encuentroAPP/" + id;
		pagina = "contentPageEncuentros";
	}
	location.href="#"+pagina;
}
 
function guardarConf(){
	// vemos si se ha indicado que se quieren notificaciones
	if ($("#sliderNotificaciones").val()=="off"){
		userPrefs = JSON.parse("{\"push\": \"off\"}");
	} else {
		userPrefs = JSON.parse("{\"push\": \"on\"}");
		// ahora vemos los tags
		var tags = [];
		if($("#sliderNotificaciones").attr('value')=='on'){
			tags.push("sliderNotificaciones");
		} 
		if($("#sonidos").attr('checked')=='checked'){
			tags.push("sonidos");
		} 
		if($("#vibracion").attr('checked')=='checked'){
			tags.push("vibracion");
		}
		if($("#errores").attr('checked')=='checked'){
			tags.push("errores");
		}
		if($("#noticia").attr('checked')=='checked'){
			tags.push("noticia");
		} else {
			if($("#noticia-1").attr('checked')=='checked'){
				tags.push("noticia-1");
			}
			if($("#noticia-2").attr('checked')=='checked'){
				tags.push("noticia-2");
			}
			if($("#noticia-3").attr('checked')=='checked'){
				tags.push("noticia-3");
			}
			if($("#noticia-4").attr('checked')=='checked'){
				tags.push("noticia-4");
			}
			if($("#noticia-5").attr('checked')=='checked'){
				tags.push("noticia-5");
			}
			if($("#noticia-6").attr('checked')=='checked'){
				tags.push("noticia-6");
			}
			if($("#noticia-7").attr('checked')=='checked'){
				tags.push("noticia-7");
			}
			if($("#noticia-8").attr('checked')=='checked'){
				tags.push("noticia-8");
			}
		}
		if($("#curso").attr('checked')=='checked'){
			tags.push("curso");
		} else {
			if($("#curso-1").attr('checked')=='checked'){
				tags.push("curso-1");
			}
			if($("#curso-2").attr('checked')=='checked'){
				tags.push("curso-2");
			}
			if($("#curso-3").attr('checked')=='checked'){
				tags.push("curso-3");
			}
			if($("#curso-4").attr('checked')=='checked'){
				tags.push("curso-4");
			}
			if($("#curso-5").attr('checked')=='checked'){
				tags.push("curso-5");
			}
			if($("#curso-6").attr('checked')=='checked'){
				tags.push("curso-6");
			}
			if($("#curso-7").attr('checked')=='checked'){
				tags.push("curso-7");
			}
			if($("#curso-8").attr('checked')=='checked'){
				tags.push("curso-8");
			}
		}
		if($("#encuentro").attr('checked')=='checked'){
			tags.push("encuentro");
		} else {
			if($("#encuentro-1").attr('checked')=='checked'){
				tags.push("encuentro-1");
			}
			if($("#encuentro-2").attr('checked')=='checked'){
				tags.push("encuentro-2");
			}
			if($("#encuentro-3").attr('checked')=='checked'){
				tags.push("encuentro-3");
			}
			if($("#encuentro-4").attr('checked')=='checked'){
				tags.push("encuentro-4");
			}
			if($("#encuentro-5").attr('checked')=='checked'){
				tags.push("encuentro-5");
			}
			if($("#encuentro-6").attr('checked')=='checked'){
				tags.push("encuentro-6");
			}
			if($("#encuentro-7").attr('checked')=='checked'){
				tags.push("encuentro-7");
			}
			if($("#encuentro-8").attr('checked')=='checked'){
				tags.push("encuentro-8");
			}
		}
		userPrefs.tags = tags;
	}
	
	localStorage["userPrefs"]= JSON.stringify(userPrefs);

	PhoneGap.exec(
            null,           			// called when signature capture is successful
            null,           			// called when signature capture encounters an error
            'PushNotificationPlugin',  	// Tell PhoneGap that we want to run "PushNotificationPlugin"
            'pushConf',    				// Tell the plugin the action we want to perform
            [userPrefs]);   			// List of arguments to the plugin
	alert("Preferencias guardadas");
}


function error(mensage)
{
	
	if(localStorage["userPrefs"]){
		userPrefs = JSON.parse(localStorage["userPrefs"]);
	} else {		
		userPrefs = JSONParse("{\"push\": \"on\"}");
	}
    eval("p = {\"error\": \"\\n"+mensage+"\\n\"};");
    
	PhoneGap.exec(
            null,           // called when signature capture is successful
            null,           // called when signature capture encounters an error
            'PushNotificationPlugin',  // Tell PhoneGap that we want to run "PushNotificationPlugin"
            'error',    		   // Tell the plugin the action we want to perform
            [userPrefs,p]);                       // List of arguments to the plugin   
}

/*
1. $('#frm2 .checkall').tap(function(event) {
	   2.    $('#frm2 input[type="checkbox"]').attr('checked', 'checked').checkboxradio( "refresh" );
	   3. });
*/
function pintaConf(userPrefs){
	$.each(userPrefs.tags, function(i, v) {
		var myswitch = $("select#sliderNotificaciones");
		myswitch[0].selectedIndex = 1;
		myswitch .slider("refresh");
		$("input[type='checkbox']:#"+v).attr("checked",true).checkboxradio("refresh");
	});
}