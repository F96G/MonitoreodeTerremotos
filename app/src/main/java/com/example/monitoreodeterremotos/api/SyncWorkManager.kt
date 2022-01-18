package com.example.monitoreodeterremotos.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.monitoreodeterremotos.database.getDatabase
import com.example.monitoreodeterremotos.main.MainRepository

//SyncWorkManager se va a encargar de ejecutar la llamada a la API para no hacerlo cada vez que abrimos la app
//WorkerParameters me dira cuando ejecutar y cada cuanto doWork
class SyncWorkManager(appContext: Context, params:WorkerParameters): CoroutineWorker(appContext, params) {

    companion object{
        const val WORK_NAME = "SyncWorkManager"
    }

    private val database = getDatabase(appContext)
    private val repositorio = MainRepository(database)


    override suspend fun doWork(): Result {
        repositorio.recuperarTerremotosDeDatabase(true)

        return Result.success()
    }
}