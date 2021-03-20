package com.example.telega.telega.UI.fragments

import androidx.fragment.app.Fragment
import com.example.telega.telega.MainActivity
import com.example.telega.telega.utilits.*
import com.example.telegram.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.fragment_enter_phone_number.*
import java.util.concurrent.TimeUnit

class EnterPhoneNumberFragment : Fragment(R.layout.fragment_enter_phone_number) {

    private lateinit var mPhoneNumber: String
    private lateinit var mCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onStart() {
        super.onStart()

        /* Callback который возвращает результат верификации */
        mCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                /* Функция срабатывает если верификация уже была произведена,
                * пользователь авторизируется в приложении без потверждения по смс */
                AUTH.signInWithCredential(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("Добро пожаловать")
                        REGISTER_ACTIVITY.replaceActivity(MainActivity())
                } else showToast(task.exception?.message.toString())
            }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                /* Функция срабатывает если верификация не удалась*/
                showToast(p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken)
            {
                /* Функция срабатывает если верификация впервые, и отправлена смс */
                replaceFragment(EnterCodeFragment(mPhoneNumber, id))
            }
        }
        register_btn_next.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        /* Функция проверяет поле для ввода номер телефона, если поле пустое выводит сообщение.
         * Если поле не пустое, то начинает процедуру авторизации/ регистрации */
        if (registerInputPhoneNumber.text.toString().isEmpty()) {
            showToast(getString(R.string.register_toast_enter_phone))
        } else {
            authUser()
        }
    }

    private fun authUser()
    {
        /* Инициализация */
        mPhoneNumber = registerInputPhoneNumber.text.toString()
        PhoneAuthProvider.verifyPhoneNumber(
                PhoneAuthOptions
                        .newBuilder(FirebaseAuth.getInstance())
                        .setActivity(REGISTER_ACTIVITY)
                        .setPhoneNumber(mPhoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setCallbacks(mCallback)
                        .build()
        )
    }
}