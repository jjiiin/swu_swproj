package com.swproject.swprojectapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PushLinkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_push_link)
        var link=intent.getStringExtra("link")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
        finish()
    }
}