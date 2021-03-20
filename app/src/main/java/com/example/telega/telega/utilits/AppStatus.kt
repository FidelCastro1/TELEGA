package com.example.telega.telega.utilits

enum class AppStatus(val status: String) {
    ONLINE("в сети"),
    OFFLINE("был недавно"),
    TYPING("печатает...");



    companion object {
        fun updateStates(appStatus:AppStatus)
        {
            REF_DATABASE_ROOT.child(NODE_USERS).child(CURRENT_UID).child(CHILD_STATUS)
                    .setValue(appStatus.status)
                    .addOnSuccessListener { USER.state = appStatus.status}
                    .addOnFailureListener { showToast(it.message.toString()) }
        }
    }
}