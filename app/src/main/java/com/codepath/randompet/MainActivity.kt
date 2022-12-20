package com.codepath.randompet

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    var petImageURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        getDogImageURL()
        Log.d("petImageURL", "pet image URL set")

        val button = findViewById<Button>(R.id.petButton)
        val imageView = findViewById<ImageView>(R.id.petImage)

        getNextImage(button, imageView)
    }

    private fun getDogImageURL() {
        val client = AsyncHttpClient()

        client["https://dog.ceo/api/breeds/image/random", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Dog", "response successful$json")

                petImageURL = json.jsonObject.getString("message")
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Dog Error", errorResponse)
            }
        }]
    }

    private fun getCatImageURL() {
        val client = AsyncHttpClient()

        client["https://api.thecatapi.com/v1/images/search", object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {
                Log.d("Cat", json.jsonArray.toString())

                var resultsJSON = json.jsonArray.getJSONObject(0)
                petImageURL = resultsJSON.getString("url")
                Log.d("cat url", petImageURL)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Cat Error", errorResponse)
            }
        }]
    }

    private fun getNextImage(button: Button, imageView: ImageView) {
        button.setOnClickListener {
            var choice = Random.nextInt(2)

            if (choice == 0) {
                getDogImageURL()
            }
            else {
                getCatImageURL()
            }

            Glide.with(this)
                .load(petImageURL)
                .fitCenter()
                .into(imageView)
        }
    }
}