package com.swproject.swprojectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.swproject.swprojectapp.Adapter.RVAdapter
import com.swproject.swprojectapp.dataModel.NoticeData
import com.swproject.swprojectapp.utils.Auth
import com.swproject.swprojectapp.utils.FBRef
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class Bookmark_Activity : AppCompatActivity() {
    val noticeDatas = mutableListOf<NoticeData>()
    val rvAdapter = RVAdapter(noticeDatas)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)

        val rv = findViewById<RecyclerView>(R.id.recyclerview)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.addItemDecoration(DividerItemDecoration(this, 1))

        getBookmarkData()
    }

    fun getBookmarkData() {
        FBRef.bookmarkRef.child(Auth.current_uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    noticeDatas.clear()
                    for (data in snapshot.children) {
                        if (data.getValue() != null) {
                            try{
                                val noticeData = data.getValue(NoticeData::class.java)
                                checkUrl(noticeData!!)
                            } catch (e:Exception){
                                continue
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    //유효한 URL인지 확인
    fun checkUrl(data:NoticeData){
        //스레드 생성
        thread{
            try{
                HttpURLConnection.setFollowRedirects(false)
                val con : HttpURLConnection = URL(data.link).openConnection() as HttpURLConnection
                con.requestMethod = "HEAD"
                if(con.responseCode == HttpURLConnection.HTTP_OK){
                    //유효한 주소인경우
                    noticeDatas.add(data)
                    //UI에 접근할 수 있음
                    this.runOnUiThread {
                        Log.d("url체크", data.toString())
                        rvAdapter.notifyDataSetChanged()
                    }
                } else{
                    //없는 주소인 경우 북마크에서 삭제
                    FBRef.bookmarkRef.child(Auth.current_uid).child(data.bookmarkKey).removeValue()
                }
            } catch (e:Exception){
                Log.d("url체크", e.toString())
                //없는 주소인 경우 북마크에서 삭제
                FBRef.bookmarkRef.child(Auth.current_uid).child(data.bookmarkKey).removeValue()
            }
        }.join()
    }
}