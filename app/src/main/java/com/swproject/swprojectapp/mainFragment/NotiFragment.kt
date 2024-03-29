package com.swproject.swprojectapp.mainFragment

import android.content.Intent
import android.net.Uri
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
import com.android.volley.VolleyLog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.swproject.swprojectapp.Adapter.kwAdapter
import com.swproject.swprojectapp.Adapter.kwNotiAdapter
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.kwModel
import com.swproject.swprojectapp.databinding.FragmentNotiBinding
import com.swproject.swprojectapp.fcm.NotiModel
import com.swproject.swprojectapp.fcm.PushNotification
import com.swproject.swprojectapp.fcm.RetrofitInstance
import com.swproject.swprojectapp.utils.FBRef
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class NotiFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    val dataModelList = mutableListOf<String>()
    val kwModelList= mutableListOf<kwModel>()
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

        auth = Firebase.auth

        //users-(사용자 uid)-keyword에 keyword 등록하기
        binding.saveBtn.setOnClickListener {
            val keyword = binding.kwEt.text.toString()
            FBRef.usersRef.child(auth.currentUser!!.uid).child("keyword").push()
                .setValue(keyword)
            //키워드 리스트에 등록하기
            FBRef.keywordRef.push().setValue(keyword)
            //키워드 구독 리스트에 등록하기
            keyWordSubscribe(keyword)
        }

        //RVAdapter장착하기
        val rv = binding.keywordRV
        val rvAdapter = kwAdapter(dataModelList)
        rv.adapter = rvAdapter
        val layout = LinearLayoutManager(
            requireActivity().getApplicationContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        rv.layoutManager = layout
        rv.setHasFixedSize(true)

        //키워드 리스트 받아오기
        val schRef = FBRef.usersRef.child(auth.currentUser!!.uid).child("keyword")
        schRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rv.removeAllViewsInLayout()
                dataModelList.clear()
                for (DataModel in snapshot.children) {
                    if (DataModel != null) {
                        dataModelList.add(DataModel.getValue(String::class.java)!!)
                    }
                }
                rvAdapter.notifyDataSetChanged()
                Log.d("data", dataModelList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        //키워드 알림 내역 recyclerview를 어댑터에 붙이기
        //RVAdapter장착하기
        val kwrv = binding.rv
        val kwrvAdapter = kwNotiAdapter(kwModelList)
        kwrv.adapter = kwrvAdapter
        var kwnotlayout=LinearLayoutManager(requireActivity().getApplicationContext())
        kwrv.layoutManager = kwnotlayout
        kwnotlayout.setReverseLayout(true)
        kwnotlayout.setStackFromEnd(true)
        kwrv.setHasFixedSize(true)
        val kwnotiRef = FBRef.usersRef.child(auth.currentUser!!.uid).child("notification")
        kwnotiRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                kwrv.removeAllViewsInLayout()
                kwModelList.clear()
                for (DataModel in snapshot.children) {
                    var item=DataModel.getValue(kwModel::class.java)!!
                    if (item != null) {
                        var dept=item.dept
                        var link=item.link
                        var body=item.body
                        var date=item.date
                        kwModelList.add(item)
                    }
                }
                kwrvAdapter.notifyDataSetChanged()
                Log.d("data_noti", kwModelList.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        kwrvAdapter.setItemClickListener(object:kwNotiAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse( kwModelList[position].link))
                startActivity(intent)
            }

        })


        return binding.root
    }


    private fun testPush(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        Log.d("pushNoti", notification.toString())
        RetrofitInstance.api.postNotification(notification)
    }

    private fun keyWordSubscribe(keyword: String) {
        var user_token = ""
        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(VolleyLog.TAG, "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                user_token = task.result.toString()
                FBRef.keyword_Subscribe_Ref.child(keyword).child(auth.currentUser!!.uid)
                    .setValue(user_token)
            }
        ) }
}