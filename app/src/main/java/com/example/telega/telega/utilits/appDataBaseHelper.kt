package com.example.telega.telega.utilits

import android.net.Uri
import com.example.telega.telega.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var USER:User
lateinit var CURRENT_UID:String

lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference

const val REF_PROFILE_IMAGE = "profile_image"
const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_URL = "photoUrl"
const val CHILD_STATUS = "status"

fun initFireBase()
{
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    USER = User()
    CURRENT_UID = AUTH.currentUser?.uid.toString()
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
}
inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline function: () -> Unit)
{
    path.putFile(uri)
            .addOnSuccessListener { function() }
            .addOnFailureListener { showToast(it.message.toString()) }
}
inline fun getUrlFromStorage(path: StorageReference, crossinline function: (Uri:String) -> Unit)
{
    path.downloadUrl
            .addOnSuccessListener { function(it.toString()) }
            .addOnFailureListener { showToast(it.message.toString()) }
}
inline fun putUrlToDataBase(url: String,crossinline function: () -> Unit)
{
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .child(CHILD_URL).setValue(url)
            .addOnSuccessListener {function() }
            .addOnFailureListener { showToast(it.message.toString()) }
}
inline fun initUser(crossinline function: () -> Unit)
{
    REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID)
            .addListenerForSingleValueEvent(AppEventListener{
                USER = it.getValue(User::class.java) ?: User()
                if (USER.username.isEmpty())
                {
                    USER.username = CURRENT_UID
                }
                function()
            })
}