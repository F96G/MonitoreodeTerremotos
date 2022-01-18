package com.example.monitoreodeterremotos.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.monitoreodeterremotos.Terremoto

//@Database defino que entidades se utilizaran
@Database(entities = [Terremoto::class], version = 1)
abstract class EqDatabase: RoomDatabase(){
    abstract val eqDao:EqDAO
}

//Este es un singleton, siempre se hace igual
private lateinit var INSTANCE:EqDatabase


fun getDatabase(context: Context): EqDatabase{
    synchronized(EqDatabase::class.java) {
        if (!::INSTANCE.isInitialized)
            INSTANCE = Room.databaseBuilder(context.applicationContext, EqDatabase::class.java,"terremotos_db").build()//terremotos_db es el nombre de toda la base de datos
    }
    return INSTANCE
}