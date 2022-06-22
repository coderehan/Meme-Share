package com.rehan.memeshareapp

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private lateinit var ivMeme: ImageView
    private lateinit var progressBar : ProgressBar
    private lateinit var btnShare : Button
    private lateinit var btnNext : Button

    var currentImageUrl : String ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ivMeme = findViewById(R.id.ivMeme)
        progressBar = findViewById(R.id.progressBar)
        btnShare = findViewById(R.id.btnShare)
        btnNext = findViewById(R.id.btnNext)

        //call loadMethod
        loadMeme()

        btnShare.setOnClickListener(View.OnClickListener {
            shareMeme()
        })

        btnNext.setOnClickListener(View.OnClickListener {
            nextMeme()
        })
    }


    private fun loadMeme() {
        progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"      //this is the URL from which we are getting memes

        //volley library to make a request network call to server and get response from server
        // Request a jsonObjectRequest response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")     //"url" is one of the key name in that provided URL on which image link is the value of that key
                Glide.with(this).load(currentImageUrl).listener(object : RequestListener<Drawable>{     //here we are adding listener as an interface to control the load of progressbar
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        return false
                    }

                }).into(ivMeme)
            },
            {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
            })

        //Get the instance from MySingleton class and the request to the queue
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    private fun nextMeme() {
        loadMeme()
    }

    private fun shareMeme() {
       val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Meme App")
        intent.putExtra(Intent.EXTRA_TEXT, "Hey! Look at this cool meme from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent, "Share this meme using...")
        startActivity(chooser)
    }

}