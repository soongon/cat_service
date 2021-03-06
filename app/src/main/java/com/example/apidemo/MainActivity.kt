package com.example.apidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.example.apidemo.api.TheCatApiService
import com.example.apidemo.model.ImageResultData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {

    private val imageView: ImageView by lazy { findViewById(R.id.imageView) }
    private val breedText: TextView by lazy { findViewById(R.id.textBreed) }
    private val temperamentText: TextView by lazy { findViewById(R.id.textTemperament) }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    private val theCatApiService by lazy {
        retrofit.create(TheCatApiService::class.java)
    }

    private val imageLoader: ImageLoader by lazy { GlideImageLoader(this) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val call = theCatApiService.searchImages(1, "full")
        call.enqueue(object: Callback<List<ImageResultData>> {
            override fun onFailure(call: Call<List<ImageResultData>>, t: Throwable) {
                Log.e("MainActivity", "Error from cat service", t)
            }

            override fun onResponse(
                call: Call<List<ImageResultData>>,
                response: Response<List<ImageResultData>>) {
                if (response.isSuccessful) {
                    val result: List<ImageResultData>? = response.body()
                    val imageUrl = result?.firstOrNull()?.url ?: "No URL"
                    val breed = result?.firstOrNull()?.breeds?.firstOrNull()?.name
                    val temperament = result?.firstOrNull()?.breeds?.firstOrNull()?.temperament

                    imageLoader.loadImage(imageUrl, imageView)
                    breedText.text = breed ?: "unknown"
                    temperamentText.text = temperament ?: "unknown"

//                    Toast.makeText(this@MainActivity, imageURL, Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("MainActivity", "error in onResponse")
                }
            }
        })
    }
}
