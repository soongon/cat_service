package com.example.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.apidemo.api.TheCatApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class MainActivity : AppCompatActivity() {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    private val theCatApiService by lazy {
        retrofit.create(TheCatApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = theCatApiService.searchImages(1, "full")
        call.enqueue(object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e("MainActivity", "Error from cat service", t)
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
//                    Log.i("MainActivity", response.body().toString())
                    findViewById<TextView>(R.id.textDisplay).text = response.body()
                } else {
                    Log.e("MainActivity", "error in onResponse")
                }
            }
        })
    }
}