package com.swproject.swprojectapp.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.databinding.FragmentJoin1Binding

class JoinFragment1 : Fragment() {

    private lateinit var binding: FragmentJoin1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_join1, container, false)


        binding.joinNextBtn.setOnClickListener {

            val id = binding.idArea.text.toString()
            val pwd = binding.pwdArea.text.toString()
            val pwdCheck = binding.pwdCheckArea.text.toString()
            val name = binding.nickArea.text.toString()

            if(!name.isEmpty() && !id.isEmpty() && !pwd.isEmpty() && !pwdCheck.isEmpty() && pwd.equals(pwdCheck)) {

                val bundle = Bundle()
                bundle.putString("id", id)
                bundle.putString("pwd", pwd)
                bundle.putString("name", name)

                (activity as JoinActivity).replaceFragment(JoinFragment2(), bundle)
            }
            else{
                if (name.isEmpty()) {
                    Toast.makeText(activity, "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else if (id.isEmpty()) {
                    Toast.makeText(activity,"이메일을 입력해주세요",Toast.LENGTH_SHORT).show()
                }
                else if (pwd.isEmpty()) {
                    Toast.makeText(activity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                }
                else if (pwdCheck.isEmpty()) {
                    Toast.makeText(activity, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(activity, "비밀번호가 맞는지 확인해주세요", Toast.LENGTH_SHORT).show()
            }



        }






        return binding.root
    }
}