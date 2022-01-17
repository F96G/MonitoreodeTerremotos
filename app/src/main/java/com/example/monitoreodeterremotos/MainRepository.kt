package com.example.monitoreodeterremotos

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainRepository {
    //Suspend permite ejecutar currutinas dentro del metodo
     suspend fun recuperarTerremotos(): MutableList<Terremoto>{
        //Dispatchers determina en que hilo se ejecuta y IO es un hilo distinto de main, si el whitcontext no esta dentro
        // de una currutina debe ser en un metodo suspend
        return withContext(Dispatchers.IO){
            val eqJsonResponse = service.getUltimoTerremoto()
            //Lo que debueble la currutina
            val listaTerremotos = parseEqResultado(eqJsonResponse)
            listaTerremotos
        }
    }
    //Transformo mi string en un JSON objet
    private fun parseEqResultado(eqJsonResponse: EqJsonResponse): MutableList<Terremoto>{
        val listaTerremotos = mutableListOf<Terremoto>()
        val featureList = eqJsonResponse.features

        for (feature in featureList){
            val id = feature.id

            val magnitud = feature.properties.mag
            val lugar = feature.properties.place
            val tiempo = feature.properties.time

            val longitud = feature.geometry.longitu
            val latitud = feature.geometry.latitud

            listaTerremotos.add(Terremoto(id,lugar,magnitud,tiempo,longitud,latitud))
        }

        /* //Obtengo mi JSON objet
         val terremotoJsonObjet = JSONObject(eqListString)
         //Obtengo el array donde esta la informacion que necesito, eso depende del creador del objeto JSON
         var featureJsonArray = terremotoJsonObjet.getJSONArray("features")



         for (i in 0 until  featureJsonArray.length()){
             val featuresJsonObjet = featureJsonArray[i] as JSONObject
             val id = featuresJsonObjet.getString("id")

             val propertiesJsonObjet =  featuresJsonObjet.getJSONObject("properties")
             val magnitud = propertiesJsonObjet.getDouble("mag")
             val lugar = propertiesJsonObjet.getString("place")
             val tiempo = propertiesJsonObjet.getLong("time")

             val geometryJsonObjet = featuresJsonObjet.getJSONObject("geometry")
             val coordinatesJsonArray = geometryJsonObjet.getJSONArray("coordinates")
             //Aclarar que al ver la estructura del JSON array es un array comun
             val longitud = coordinatesJsonArray.getDouble(0)
             val latitud = coordinatesJsonArray.getDouble(1)

             listaTerremotos.add(Terremoto(id,lugar,magnitud,tiempo,longitud,latitud))
         }*/

        return listaTerremotos
    }
}