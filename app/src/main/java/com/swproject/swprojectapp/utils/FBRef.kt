package com.swproject.swprojectapp.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {

    companion object {
        private val database = Firebase.database
        val usersRef = database.getReference("users")
        val keywordRef=database.getReference("keyword")
        val keyword_Subscribe_Ref= database.getReference("keyword_subscribe")
    }
}