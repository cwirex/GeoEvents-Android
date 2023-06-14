package com.example.tea.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tea.adapters.FriendAdapter
import com.example.tea.databinding.ActivityFriendsBinding
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FriendsMenuActivity : AppCompatActivity() {
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var binding: ActivityFriendsBinding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    companion object {
        const val TAG = "FriendsMenuActivity"
        const val REF = "friends"    // todo <- users$uid$friends
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = db.collection(REF).orderBy("addedAt")
        val options = FirestoreRecyclerOptions.Builder<FriendMenu>()
            .setQuery(query, FriendMenu::class.java)
            .setLifecycleOwner(this@FriendsMenuActivity).build()
        friendAdapter = FriendAdapter(options)
        binding.rvFriendItems.adapter = friendAdapter
        binding.rvFriendItems.layoutManager = LinearLayoutManager(this)

        binding.btnAddFriend.setOnClickListener(addFriendListener)
        binding.btnDeleteCheckedFriends.setOnClickListener(deleteFriendListener)
    }

    private val addFriendListener = View.OnClickListener {
        val friendEmail = binding.etFriendEmail.text.toString()
        if(friendEmail.isNotEmpty()) {
            //TODO("GET USER ID BY EMAIL")
            // if fid != null ->
            // ADD FROM USER FRIEND MANAGER as well!!!
            val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"))
            val fid = ""

            val friend = FriendMenu(fid=fid, email=friendEmail, addedAt=timestamp)
            db.collection(REF)
                .document(timestamp)
                .set(friend)
                .addOnSuccessListener { _ ->
                    Log.d(TAG, "DocumentSnapshot added with ID: $timestamp")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
            binding.etFriendEmail.text.clear()
        }
    }

    private val deleteFriendListener = View.OnClickListener {
        //TODO: Delete friends from FRIEND MANAGER (OR SKIP?) !!!
        val nDeleted = friendAdapter.deleteCheckedItems()
        if (nDeleted > 0){
            Toast.makeText(this@FriendsMenuActivity,
                "Deleted $nDeleted item${if (nDeleted > 1) "s" else ""}",
                Toast.LENGTH_SHORT).show()
        }
    }
}