package com.example.tea

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddFriendActivity : AppCompatActivity() {

    lateinit var addFriendButton: Button
    lateinit var friendEmailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        addFriendButton = findViewById(R.id.addFriendButton)
        friendEmailEditText = findViewById(R.id.friendEmailEditText)

        addFriendButton.setOnClickListener{
            addFriend()
        }
    }

    private fun addFriend() {
        val friendEmail = friendEmailEditText.text.toString().trim()
        Toast.makeText(this, "friends email: $friendEmail", Toast.LENGTH_SHORT).show()
    }
}
