package com.example.monitoreodeterremotos.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


//Siempre se hace igual, ademas del application, si el view model necesita mas cosas se pueden agregar
class MainVewModelFactory(private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(application) as T
    }
}