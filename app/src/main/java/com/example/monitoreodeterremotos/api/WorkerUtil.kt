package com.example.monitoreodeterremotos.api

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object WorkerUtil {
    fun scheduleSync(context: Context){
        val constrains = Constraints.Builder()
                //Solo se conecta si el celuar esta conectado a internet
            .setRequiredNetworkType(NetworkType.CONNECTED).build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorkManager>(
            //Establezco el tiempo en que se ejecute, mimimo 15min
            1, TimeUnit.HOURS)
            .setConstraints(constrains).build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(SyncWorkManager.WORK_NAME,
                //KEPP si trato de instanciar un nuevo workmanager no lo crea y utiliza el mismo
                ExistingPeriodicWorkPolicy.KEEP, syncRequest)
    }
}