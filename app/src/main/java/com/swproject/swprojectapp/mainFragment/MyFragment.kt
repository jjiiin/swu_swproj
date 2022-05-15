package com.swproject.swprojectapp.mainFragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.swproject.swprojectapp.MainActivity
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.auth.LoginActivity
import com.swproject.swprojectapp.databinding.AlertdialogEdittextBinding
import com.swproject.swprojectapp.databinding.FragmentMyBinding
import com.swproject.swprojectapp.utils.FBRef


class MyFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentMyBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        auth= Firebase.auth

        var adapter =
            context?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, resources.getStringArray(R.array.majorList)) }
        binding.mypageMajor1.adapter = adapter
        binding.mypageMajor2.adapter = adapter
        binding.mypageMajor3.adapter = adapter


        val myRef = FBRef.usersRef.child(auth.currentUser!!.uid)
        // 처음에 값 가져와서 보여주기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.mypageName.text = snapshot.child("nickname").value.toString()
                binding.mypageEmail.text = snapshot.child("email").value.toString()
                binding.mypageMajor1.setSelection(Integer.parseInt(snapshot.child("major1").value.toString()))
                binding.mypageMajor2.setSelection(Integer.parseInt(snapshot.child("major2").value.toString()))
                binding.mypageMajor3.setSelection(Integer.parseInt(snapshot.child("major3").value.toString()))
            }
            override fun onCancelled(error: DatabaseError) {
                // 읽어오기 실패했을 경우
            }
        })

        // 로그아웃
        binding.mypageLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(context, "로그아웃되었습니다", Toast.LENGTH_SHORT).show()
            //LoginActivity로
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags= Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 계정 삭제 버튼
        binding.mypageDeleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("계정 삭제")
            builder.setMessage("계정을 삭제하시겠습니까?")
            builder.setPositiveButton("확인",DialogInterface.OnClickListener { dialog, id ->
                auth.currentUser!!.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "계정이 삭제되었습니다", Toast.LENGTH_LONG).show()

                            //로그아웃처리
                            auth.signOut()
                            myRef.removeValue()

                            //LoginActivity로
                            val intent = Intent(activity, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                }
            })
            builder.setNeutralButton("취소",null)
            builder.show()
        }

        // 비밀번호 재설정
        binding.mypagePwdReset.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val builderItem = AlertdialogEdittextBinding.inflate(layoutInflater)
            val editText = builderItem.editText
            with(builder){
                setTitle("비밀번호 재설정")
                setMessage("비밀번호를 입력하세요")
                setView(builderItem.root)
                setPositiveButton("입력"){ dialogInterface: DialogInterface, i: Int ->
                    if(editText.text != null) {
                        val newPassword = editText.text.toString()
                        auth.currentUser!!.updatePassword(newPassword)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(context, "비밀번호가 재설정되었습니다", Toast.LENGTH_LONG).show()

                                    //로그아웃처리
                                    auth.signOut()

                                    //LoginActivity로
                                    val intent = Intent(activity, LoginActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }
                            }
                    }
                }
                show()
            }
        }

        // 저장 버튼
        binding.mypageBtn.setOnClickListener {
            val major1=binding.mypageMajor1.selectedItemPosition
            val major2=binding.mypageMajor2.selectedItemPosition
            val major3=binding.mypageMajor3.selectedItemPosition

            myRef.child("major1").setValue(major1)
            myRef.child("major2").setValue(major2)
            myRef.child("major3").setValue(major3)

            Toast.makeText(context, "저장되었습니다", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }


}