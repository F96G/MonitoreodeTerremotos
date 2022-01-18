package com.example.monitoreodeterremotos.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.monitoreodeterremotos.Terremoto
import com.example.monitoreodeterremotos.api.ApiResposeStatus
import com.example.monitoreodeterremotos.database.getDatabase
import kotlinx.coroutines.launch
import java.net.UnknownHostException

class MainViewModel(application: Application,private val tipoClas:Boolean): AndroidViewModel(application) {
    private var _eqList = MutableLiveData<MutableList<Terremoto>>()
    val eqList: LiveData<MutableList<Terremoto>> get() = _eqList

    //Necesito el contexto pasado de un activity, para esto uso application
    private val database = getDatabase(application)
    //Esto crea un repositorio que se encarga de comunicarse con el servicio para que no lo haga view model
    //Debemos agregar el databse
    private val repositorio = MainRepository(database)

    //No utilizo livedata porque el database ya lo hace

    //Dependiendo del estado de traer los datos va a cambiar el status
    private val _status = MutableLiveData<ApiResposeStatus>()
    val status : LiveData<ApiResposeStatus> get() = _status

    init {
        cargarTerremotosDeDb(tipoClas)//La primera vez carga por tiempo
    }

    fun cargarTerremotosDeDb(tClas:Boolean){
        viewModelScope.launch {
            _eqList.value = repositorio.recuperarTerremotosDeDatabase(tClas)
            //Si entra y la lista esta vacia llama a descargar los datos
            //Esto puede pasar porque worker todavia no fue llamado
            if (_eqList.value!!.isEmpty()){
                cargarTerremotos()
            }
        }

    }

    private fun cargarTerremotos() {
        //viewModelScope permite llamar metodos suspend o ejecutar withContext.
        viewModelScope.launch {
            try {
                _status.value = ApiResposeStatus.LOADING //Antes de traer los datos, status esta en loading
                _eqList.value = repositorio.recuperarTerremotos(tipoClas)
                _status.value = ApiResposeStatus.DONE
            }catch (e:UnknownHostException){//En caso de no haber internet
                Log.d(MainViewModel::class.java.simpleName,"No internet connection", e )
                _status.value = ApiResposeStatus.ERROR
            }

        }
    }
}