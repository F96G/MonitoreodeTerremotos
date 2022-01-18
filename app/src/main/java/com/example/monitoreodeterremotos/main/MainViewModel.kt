package com.example.monitoreodeterremotos.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.monitoreodeterremotos.Terremoto
import com.example.monitoreodeterremotos.database.getDatabase
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.UnknownHostException
import java.net.UnknownServiceException

class MainViewModel(application: Application): AndroidViewModel(application) {
//    private var _eqList = MutableLiveData<MutableList<Terremoto>>()
//    val eqList: LiveData<MutableList<Terremoto>> get() = _eqList   <- ya no se necesitan ya que EqDAO ya contempla el livedata

    //Necesito el contexto pasado de un activity, para esto uso application
    private val database = getDatabase(application)
    //Esto crea un repositorio que se encarga de comunicarse con el servicio para que no lo haga view model
    //Debemos agregar el databse
    private val repositorio = MainRepository(database)

    //No utilizo livedata porque el database ya lo hace
    val eqList = repositorio.eqList

    init {
        //viewModelScope permite llamar metodos suspend o ejecutar withContext.
        viewModelScope.launch {
            try {
                repositorio.recuperarTerremotos()
            }catch (e:UnknownHostException){//En caso de no haber internet
                Log.d(MainViewModel::class.java.simpleName,"No internet connection", e )
            }

        }
    }
}