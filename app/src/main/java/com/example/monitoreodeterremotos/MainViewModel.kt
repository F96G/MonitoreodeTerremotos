package com.example.monitoreodeterremotos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainViewModel: ViewModel() {
    private var _eqList = MutableLiveData<MutableList<Terremoto>>()
    val eqList: LiveData<MutableList<Terremoto>> get() = _eqList
    //Esto crea un repositorio que se encarga de comunicarse con el servicio para que no lo haga view model
    private val repositorio = MainRepository()

    init {
        //viewModelScope permite llamar metodos suspend o ejecutar withContext.
        viewModelScope.launch {
            _eqList.value = repositorio.recuperarTerremotos()
        }
    }
}