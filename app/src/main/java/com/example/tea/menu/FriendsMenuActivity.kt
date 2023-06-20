package com.example.tea.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tea.adapters.FriendAdapter
import com.example.tea.databinding.ActivityFriendsBinding
import com.example.tea.user.User
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
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = ActivityFriendsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val query = db.collection("users/${auth.uid}/myfriends").orderBy("addedAt")
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
        if (friendEmail.isNotEmpty()) {
            val uid = Firebase.auth.uid
            if (uid != null) {
                val user = User(uid)
                user.friendManager.getIdByEmail(friendEmail) { ffid ->
                    if (ffid != null) {
                        val timestamp =
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmssSSS"))

                        val friend =
                            FriendMenu(fid = ffid, email = friendEmail, addedAt = timestamp)
                        db.collection("users/${auth.uid}/myfriends")
                            .document(timestamp)
                            .set(friend)
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }

                        val thisUserAsFriend = FriendMenu(fid=user.getId(), email=auth.currentUser?.email ?: "", addedAt=timestamp)
                        db.collection("users/${friend.fid}/myfriends")
                            .document(timestamp)
                            .set(thisUserAsFriend)
                            .addOnFailureListener { e ->
                                Log.w(TAG, "Error adding document", e)
                            }

                        if (ffid.isNotEmpty()) user.friendManager.addFriend(ffid) { maybeFriend ->
                            if (maybeFriend != null) {
                                Toast.makeText(
                                    this@FriendsMenuActivity,
                                    "Added friend successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }


            binding.etFriendEmail.text.clear()
        }
    }

    private val deleteFriendListener = View.OnClickListener {
        //TODO: Delete friends from FRIEND MANAGER (OR SKIP?) !!!
        val nDeleted = friendAdapter.deleteCheckedItems()
        if (nDeleted > 0) {
            Toast.makeText(
                this@FriendsMenuActivity,
                "Deleted $nDeleted item${if (nDeleted > 1) "s" else ""}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}