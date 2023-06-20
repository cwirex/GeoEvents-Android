package com.example.tea

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tea.auth.login.ui.LoginActivity
import com.example.tea.menu.MenuActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent =
            if (Firebase.auth.currentUser != null)
                Intent(this, MenuActivity::class.java)
            else
                Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
