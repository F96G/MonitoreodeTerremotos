package com.example.monitoreodeterremotos.main

import com.example.monitoreodeterremotos.Terremoto
import com.example.monitoreodeterremotos.api.EqJsonResponse
import com.example.monitoreodeterremotos.api.service
import com.example.monitoreodeterremotos.database.EqDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
//Se pide el argumento de la database
class MainRepository(private val database: EqDatabase) {

    //Suspend permite ejecutar currutinas dentro del metodo
     suspend fun recuperarTerremotos(tipoClas: Boolean): MutableList<Terremoto>{
        //Dispatchers determina en que hilo se ejecuta y IO es un hilo distinto de main, si el whitcontext no esta dentro
        // de una currutina debe ser en un metodo suspend
        return withContext(Dispatchers.IO){
            val eqJsonResponse = service.getUltimoTerremoto()
            //Lo que debueble la currutina
            val listaTerremotos = parseEqResultado(eqJsonResponse)

            //Le envio la lista de terremotos a DAO
            database.eqDao.insertAll(listaTerremotos)

            //Lo descargado de la API se lo paso al database
            recuperarTerremotosDeDatabase(tipoClas)
//            listaTerremotos <- devuelvo directamente de la API
            //database.eqDao.getTerremoto()
        }
    }

    suspend fun recuperarTerremotosDeDatabase(tipoClas: Boolean): MutableList<Terremoto>{
        return withContext(Dispatchers.IO) {
            if (tipoClas)
                 database.eqDao.getTerremotoPorMagnitud()
            else
                 database.eqDao.getTerremotoPorTiempo()
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