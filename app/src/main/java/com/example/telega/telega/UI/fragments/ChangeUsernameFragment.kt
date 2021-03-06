package com.example.telega.telega.UI.fragments

import com.example.telega.telega.utilits.*
import com.example.telegram.R
import kotlinx.android.synthetic.main.fragment_change_username.*
import java.util.*

class ChangeUsernameFragment : BaseChangeFragment(R.layout.fragment_change_username) {

    lateinit var mNewUsername: String

    override fun onResume() {
        super.onResume()
        settings_input_username.setText(USER.username)
    }

    override fun change() {
        mNewUsername = settings_input_username.text.toString().toLowerCase(Locale.getDefault())
        if (mNewUsername.isEmpty()){
            showToast("Поле пустое")
        } else {
            REF_DATABASE_ROOT.child(NODE_USER_NAMES)
                .addListenerForSingleValueEvent(AppEventListener{
                    if (it.hasChild(mNewUsername)){
                        showToast("Такой пользователь уже существует")
                    }
                    else
                    {
                        changeUsername()
                    }
                })

        }
    }

    private fun changeUsername() {
        /* Изменение username в базе данных */
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(mNewUsername).setValue(CURRENT_UID)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    updateCurrentUsername()
                }
            }
    }

    private fun updateCurrentUsername() {
        /* Обновление username в базе данных у текущего пользователя */
        REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_USERNAME)
            .setValue(mNewUsername)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    deleteOldUsername()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    private fun deleteOldUsername() {
        /* Удаление старого username из базы данных  */
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(USER.username).removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    showToast(getString(R.string.toast_data_update))
                    fragmentManager?.popBackStack()
                    USER.username = mNewUsername
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }
}