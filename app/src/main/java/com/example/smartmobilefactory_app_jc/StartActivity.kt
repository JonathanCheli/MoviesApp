package com.example.smartmobilefactory_app_jc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val action = intent?.action
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("INITIAL_ACTION", action)
        startActivity(intent)
        finish()

    }

}