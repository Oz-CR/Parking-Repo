package com.example.parking.ui.detail

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val titleTextView: TextView = findViewById(R.id.detailTitleTextView)
        val descriptionTextView: TextView = findViewById(R.id.detailDescriptionTextView)
        val imageView: ImageView = findViewById(R.id.detailImageView)

        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val imageUrl = intent.getStringExtra("imageUrl")

        titleTextView.text = title
        descriptionTextView.text = description

        Picasso.get()
            .load(imageUrl)
            .fit()
            .centerCrop()
            .into(imageView)
    }
}