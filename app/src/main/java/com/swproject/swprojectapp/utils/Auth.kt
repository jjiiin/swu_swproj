package com.swproject.swprojectapp.utils

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Auth {
    companion object {
        private val user = Firebase.auth.currentUser
        public val current_uid = user!!.uid
    }
}