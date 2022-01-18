package com.example.monitoreodeterremotos.main

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.CheckBoxPreference
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monitoreodeterremotos.EqAdapter
import com.example.monitoreodeterremotos.R
import com.example.monitoreodeterremotos.Terremoto
import com.example.monitoreodeterremotos.api.ApiResposeStatus
import com.example.monitoreodeterremotos.databinding.ActivityMainBinding
//Para implementar la API utilizaremos retrofit con implementation 'com.squareup.retrofit2:retrofit:(insert latest version)'
//Para agregar el RecyclerView debo implementar implementation 'androidx.recyclerview:recyclerview-selection:1.1.0'
class MainActivity : AppCompatActivity() {
    companion object{
        val SORT_TYPE_KEY = "sort_key"
    }

    lateinit private var viewModel:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var tipoClasific = getTipoClasific()

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)
        //Como los viewModel no se les insancia se debe crear un factory
        viewModel = ViewModelProvider(this, MainVewModelFactory(application, tipoClasific)).get(MainViewModel::class.java)
        val adapter = EqAdapter(this)

        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, Observer {
            eqList -> adapter.submitList(eqList)

            manejarListaVacia(eqList, binding)
        })

        viewModel.status.observe(this){
            when(it){
                (ApiResposeStatus.DONE)->{
                    binding.pbLoading.visibility = View.GONE
                }(ApiResposeStatus.LOADING)->{
                binding.pbLoading.visibility = View.VISIBLE
                }(ApiResposeStatus.ERROR)->{
                binding.pbLoading.visibility = View.GONE
                Toast.makeText(this, "Error de descarga, ver internet",Toast.LENGTH_SHORT).show()
                }

            }
        }


        adapter.onItemClickListener = {
            Toast.makeText(this,it.lugar, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTipoClasific(): Boolean {
        //getBolean devuelve la preferencia guardada y si es nula devuelve falso
        return getPreferences(MODE_PRIVATE).getBoolean(SORT_TYPE_KEY, false)
    }

    //Guardara el tipo de clasificacion cuando la app cambia de estado
    private fun saveClasificacion(tipoClas:Boolean){
        //Se guarda una preferencia, se pone el nombre y si es compartido a otras apps. puedo usarlo en otras clases
        //val pref = getSharedPreferences("eq_prefs", MODE_PRIVATE)
        val pref = getPreferences(MODE_PRIVATE)
        //Es parecido a pasar datos con un intent
        val editor = pref.edit()
        editor.putBoolean(SORT_TYPE_KEY, tipoClas)
        editor.apply()
    }



    private fun manejarListaVacia(eqList: MutableList<Terremoto>, binding: ActivityMainBinding){
        //Cuando la lista esta vacia se muestra un empty
        if (eqList.isEmpty())
            binding.eqEmptyView.visibility = View.VISIBLE
        else
            binding.eqEmptyView.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.iSortMagnitude ->{
                viewModel.cargarTerremotosDeDb(true)
                saveClasificacion(true)
            }
            R.id.iSortTime ->{
                viewModel.cargarTerremotosDeDb(false)
                saveClasificacion(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}