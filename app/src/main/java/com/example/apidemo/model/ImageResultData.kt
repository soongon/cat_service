package com.example.apidemo.model

import com.squareup.moshi.Json

data class ImageResultData(
    @field:Json(name="url") val url: String,
    val breeds: List<CatBreedData>
)