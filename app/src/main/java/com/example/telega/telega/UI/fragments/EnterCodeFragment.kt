package com.example.telega.telega.UI.fragments

import androidx.fragment.app.Fragment
import com.example.telega.telega.MainActivity
import com.example.telega.telega.activities.RegisterActivity
import com.example.telega.telega.utilits.*
import com.example.telegram.R
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_code.*

class EnterCodeFragment(val phoneNumber: String, val id: String) :
        Fragment(R.layout.fragment_enter_code) {


    override fun onStart() {
        super.onStart()
        (activity as RegisterActivity).title = phoneNumber
        registerInputCode.addTextChangedListener(AppTextWatcher {
            val string = registerInputCode.text.toString()
            if (string.length == 6) {
                enterCode()
            }
        })
    }

    private fun enterCode() {
        /* Функция проверяет код, если все нормально, производит создания информации о пользователе в базе данных.*/
        val code = registerInputCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                val uid = AUTH.currentUser?.uid.toString()
                val dateMap = mutableMapOf<String, Any>()
                dateMap[CHILD_ID] = uid
                dateMap[CHILD_PHONE] = phoneNumber
                dateMap[CHILD_USERNAME] = uid
                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                        .addOnFailureListener { showToast(it.message.toString()) }
                        .addOnSuccessListener {
                            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                                    .addOnSuccessListener{
                                            showToast("Добро пожаловать")
                                            REGISTER_ACTIVITY.replaceActivity(MainActivity())
                                        }
                                    .addOnFailureListener { showToast(it.message.toString()) }
                                    }
            }
            else showToast(task.exception?.message.toString())}

            }
        }
