package com.example.monitoreodeterremotos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.monitoreodeterremotos.Terremoto

//Dao es data acces objet. dentro de la interfaz metemos los metodos donde utilizarmeos la tabla de terremotos
@Dao
interface EqDAO {
    //Gracias a @Insert Dao solo se encarga de insertar los elementos a la tabla
    //onCooonflict determina que hacer en caso de que dos elementos tengan el mismo id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(eqList: MutableList<Terremoto>)


    //@Query hace una pregunta para obtener datos de una base de datos, de la API vienen ordenados por tiempo por defecto
    @Query("SELECT * FROM terremotos")//Selecciona tod´o de la tabla terremotos
    fun getTerremotoPorTiempo():MutableList<Terremoto>

    @Query("SELECT * FROM terremotos order by magnitud ASC")//los ordena por magnitud en orden acendente
    fun getTerremotoPorMagnitud():MutableList<Terremoto>

    //Update remplaza lo que hay en la tabla por lo nuevo
    //Delete elimina el elemento pasado por argumento de la tabla
    //Tutorial de SQL https://hackaprende.com/2021/02/15/micro-tutorial-de-sqlite/
}