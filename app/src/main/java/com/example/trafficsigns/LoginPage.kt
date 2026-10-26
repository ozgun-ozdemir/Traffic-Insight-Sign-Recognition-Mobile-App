package com.example.trafficsigns

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class LoginPage : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // dark background -> light status bar icons
        enableEdgeToEdge(SystemBarStyle.dark(Color.TRANSPARENT), SystemBarStyle.dark(Color.TRANSPARENT))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        findViewById<Button>(R.id.Start).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.quit).setOnClickListener {
            finishAffinity()
        }
    }
}
