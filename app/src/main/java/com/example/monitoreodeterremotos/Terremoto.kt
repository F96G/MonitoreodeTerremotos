package com.example.monitoreodeterremotos

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//Data permite hacer un equals y utilizar room
//para utilizar room se debe implementar @Entity, @PrimaryKey especifica el id de cada terremoto
@Parcelize
@Entity(tableName = "terremotos")
data class Terremoto(@PrimaryKey val id:String, val lugar:String, val magnitud:Double, val time:Long, val longitud:Double, val latitud: Double) :
    Parcelable