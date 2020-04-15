package com.example.aidl

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text.setOnClickListener {
            var intent = Intent(this@MainActivity, WebViewActivity::class.java)
            this@MainActivity.startActivity(intent)
        }

    }
}
