package com.swproject.swprojectapp.mainFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.swproject.swprojectapp.Adapter.kwAdapter
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.databinding.FragmentNotiBinding
import com.swproject.swprojectapp.utils.FBRef


class NotiFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    val dataModelList = mutableListOf<String>()
    private lateinit var binding: FragmentNotiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_noti, container, false)

        auth= Firebase.auth

        //users-(사용자 uid)-keyword에 keyword 등록하기
        binding.saveBtn.setOnClickListener {
            val keyword=binding.kwEt.text.toString()
            FBRef.usersRef.child(auth.currentUser!!.uid).child("keyword").push()
                .setValue(keyword)

            //키워드 리스트에 등록하기
            FBRef.keywordRef.push().setValue(keyword)


            //user-token값 받아오기
            //키워드 구독 리스트에 등록하기
            var user_token=""
            val tokenRef =FBRef.usersRef.child(auth.currentUser!!.uid).child("token")
            tokenRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    user_token=snapshot.getValue(String::class.java)!!
                    Log.d("token",user_token)
                    FBRef.keyword_Subscribe_Ref.child(keyword).push().setValue(user_token)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }
        //RVAdapter장착하기
        val rv = binding.keywordRV
        val rvAdapter = kwAdapter(dataModelList)
        rv.adapter = rvAdapter
        val layout = LinearLayoutManager(requireActivity().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false)
        rv.layoutManager = layout
        rv.setHasFixedSize(true)

        //키워드 리스트 받아오기
        val schRef =FBRef.usersRef.child(auth.currentUser!!.uid).child("keyword")
        schRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rv.removeAllViewsInLayout()
                dataModelList.clear()
                for (DataModel in snapshot.children) {
                    if(DataModel!=null) {
                        dataModelList.add(DataModel.getValue(String::class.java)!!)
                    }
                }
                rvAdapter.notifyDataSetChanged()
                Log.d("data",dataModelList.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })



        return binding.root
    }


}