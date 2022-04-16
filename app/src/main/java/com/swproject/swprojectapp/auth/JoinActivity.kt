package com.swproject.swprojectapp.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.android.volley.VolleyLog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.swproject.swprojectapp.MainActivity
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.Userdata
import com.swproject.swprojectapp.databinding.ActivityJoinBinding
import com.swproject.swprojectapp.utils.FBRef

class JoinActivity : AppCompatActivity() {
    private lateinit var binding : ActivityJoinBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        binding = DataBindingUtil.setContentView(this, R.layout.activity_join)

        //회원가입시 토큰 값 같이 저장하기
        binding.joinBtn.setOnClickListener {
            var isGotoJoin = true
            val id = binding.idArea.text.toString()
            val pwd = binding.pwdArea.text.toString()
            val name = binding.nickArea.text.toString()
            if (id.isEmpty()) {
                //Toast.makeText(this,"id를 입력해주세요",Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }
            if (pwd.isEmpty()) {
                Toast.makeText(this, "pwd를 입력해주세요", Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }
            if (name.isEmpty()) {
                Toast.makeText(this, "닉네임을 입력해주세요", Toast.LENGTH_LONG).show()
                isGotoJoin = false
            }

            if (isGotoJoin) {
                //신규회원 가입
                Toast.makeText(this, id, Toast.LENGTH_LONG).show()
                auth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "가입성공", Toast.LENGTH_LONG).show()

                        //회원 데이터베이스에 이메일, 닉네임 정보 저장
                        val userData = Userdata(id, name)
                        FBRef.usersRef.child(auth.currentUser!!.uid).setValue(userData)

                        //FCM을 위한 token값 저장
                        FirebaseMessaging.getInstance().token.addOnCompleteListener(
                            OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(
                                        VolleyLog.TAG,
                                        "Fetching FCM registration token failed",
                                        task.exception
                                    )
                                    return@OnCompleteListener
                                }

                                // Get new FCM registration token
                                val token = task.result
                                FBRef.usersRef.child(auth.currentUser!!.uid).child("token")
                                    .setValue(token)

                                // Log and toast
                                Log.e("token", token.toString())
                            })


                        //MainActivity로
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "가입실패", Toast.LENGTH_LONG).show()
                    }
                }


            }
        }
    }
}