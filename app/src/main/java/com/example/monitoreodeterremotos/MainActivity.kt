package com.example.monitoreodeterremotos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.monitoreodeterremotos.databinding.ActivityMainBinding
//Para implementar la API utilizaremos retrofit con implementation 'com.squareup.retrofit2:retrofit:(insert latest version)'
//Para agregar el RecyclerView debo implementar implementation 'androidx.recyclerview:recyclerview-selection:1.1.0'
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.eqRecycler.layoutManager = LinearLayoutManager(this)
        val viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val adapter = EqAdapter(this)

        binding.eqRecycler.adapter = adapter

        viewModel.eqList.observe(this, Observer {
            eqList -> adapter.submitList(eqList)

            manejarListaVacia(eqList, binding)
        })


        adapter.onItemClickListener = {
            Toast.makeText(this,it.lugar, Toast.LENGTH_SHORT).show()
        }
    }



    private fun manejarListaVacia(eqList: MutableList<Terremoto>, binding: ActivityMainBinding){
        //Cuando la lista esta vacia se muestra un empty
        if (eqList.isEmpty())
            binding.eqEmptyView.visibility = View.VISIBLE
        else
            binding.eqEmptyView.visibility = View.GONE
    }
}