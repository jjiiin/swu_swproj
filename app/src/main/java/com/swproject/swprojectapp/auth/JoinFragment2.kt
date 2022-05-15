package com.swproject.swprojectapp.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.volley.VolleyLog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.swproject.swprojectapp.MainActivity
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.Userdata
import com.swproject.swprojectapp.databinding.FragmentJoin1Binding
import com.swproject.swprojectapp.databinding.FragmentJoin2Binding
import com.swproject.swprojectapp.utils.FBRef

class JoinFragment2 :Fragment() {

    private lateinit var binding: FragmentJoin2Binding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join2, container, false)
        auth = Firebase.auth


        var adapter =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.majorList)) }
        binding.joinMajor1.adapter = adapter
        binding.joinMajor2.adapter = adapter
        binding.joinMajor3.adapter = adapter

        binding.joinMajor1.setSelection(0)
        binding.joinMajor2.setSelection(0)
        binding.joinMajor3.setSelection(0)

        var major1 = 0
        var go = false;
        binding.joinMajor1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                major1 = binding.joinMajor1.selectedItem.toString()
                major1 = binding.joinMajor1.selectedItemPosition
                if(binding.joinMajor1.selectedItemPosition != 0)
                    go = true
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        var major2 = 0
        binding.joinMajor2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                major2 = binding.joinMajor2.selectedItem.toString()
                major2 = binding.joinMajor2.selectedItemPosition
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                major2 = binding.joinMajor2.selectedItemPosition
            }
        }

        var major3 = 0
        binding.joinMajor3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//                major3 = binding.joinMajor3.selectedItem.toString()
                major3 = binding.joinMajor3.selectedItemPosition
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                major3 = binding.joinMajor3.selectedItemPosition
            }
        }

        binding.joinBtn.setOnClickListener {
            if(go){
                var id = ""
                var pwd = ""
                var name = ""
                arguments?.let {
                    id = it.getString("id").toString()
                    pwd = it.getString("pwd").toString()
                    name = it.getString("name").toString()
                }

                //신규회원 가입
                activity?.let { it1 ->
                    auth.createUserWithEmailAndPassword(id, pwd).addOnCompleteListener(it1) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "가입성공", Toast.LENGTH_SHORT).show()

                            //회원 데이터베이스에 이메일, 닉네임, 전공 정보 저장
                            val userData = Userdata(id, name, major1, major2, major3)
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
                            val intent = Intent(activity, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Toast.makeText(context, "가입실패", Toast.LENGTH_SHORT).show()
                        }
                    }
                }


            }
            else
                Toast.makeText(context,"학과를 입력해주세요",Toast.LENGTH_SHORT).show()
        }




        return binding.root
    }
}