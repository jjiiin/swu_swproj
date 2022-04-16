package com.swproject.swprojectapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.swproject.swprojectapp.MainActivity
import com.swproject.swprojectapp.R

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth= Firebase.auth
        //user-id가 존재한다면 바로 main으로 넘어가고 아니면 intro의 로그인 화면으로 간다.
        if(auth.currentUser?.uid==null){
            Handler().postDelayed({
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            },2000)

        }else{
            Handler().postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            },2000)

        }

    }
}