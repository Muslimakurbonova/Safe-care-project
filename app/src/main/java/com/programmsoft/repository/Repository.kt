package com.programmsoft.repository

import com.google.firebase.database.DatabaseReference
import com.programmsoft.room.database.RoomDB
import javax.inject.Inject

class Repository @Inject constructor(
    private val databaseReference: DatabaseReference
) {
    fun getTipsReference(): DatabaseReference {
        return databaseReference.child("tips")
    }

    fun getFavoritesReference(uid: String): DatabaseReference {
        return databaseReference.child("user_favorites").child(uid)
    }
}