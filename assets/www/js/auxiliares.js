function rellenarPorLaIzquierda(cadena, caracter, longitud){
	var cadcero='';
	for(i=0;i<(longitud-cadena.length);i++){
		cadcero+=caracter;
	}
	return cadcero+cadena;
}