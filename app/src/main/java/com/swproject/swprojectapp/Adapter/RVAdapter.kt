package com.swproject.swprojectapp.Adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.NoticeData
import com.swproject.swprojectapp.utils.Auth
import com.swproject.swprojectapp.utils.FBRef

class RVAdapter(val items: MutableList<NoticeData>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {
    var bookmarkclick = false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_notice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
        // 클릭했을 때
        holder.itemView.setOnClickListener {
            // 웹으로 띄우기
            val openUrl = Intent(Intent.ACTION_VIEW)
            openUrl.data = Uri.parse(items.get(position).link)
            ContextCompat.startActivity(holder.itemView.context, openUrl, null)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: NoticeData) {
            itemView.findViewById<TextView>(R.id.tv_title).text = item.title
            itemView.findViewById<TextView>(R.id.tv_date).text = item.date
            // 상단고정 공지면 배경색 바꾸기
            if(item.noticeTop){
                itemView.findViewById<LinearLayout>(R.id.item_background).setBackgroundColor(
                    Color.parseColor("#FFDEDE"))
            }
            else{
                itemView.findViewById<LinearLayout>(R.id.item_background).setBackgroundColor(
                    Color.WHITE)
            }

            getBookmark(item.bookmarkKey, itemView)
            //북마크 클릭시
            itemView.findViewById<CheckBox>(R.id.checkbox_bookmark)
                .setOnCheckedChangeListener { button, isChecked ->
                    if (isChecked) {
                        FBRef.bookmarkRef.child(Auth.current_uid).child(item.bookmarkKey).setValue(item)
                    } else {
                        //체크해제되면
                        FBRef.bookmarkRef.child(Auth.current_uid).child(item.bookmarkKey).removeValue()
                    }
                }
        }
    }

    fun getBookmark(pushKey: String, itemView: View) {
        FBRef.bookmarkRef.child(Auth.current_uid).child(pushKey)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.getValue() != null) {
                        itemView.findViewById<CheckBox>(R.id.checkbox_bookmark).isChecked = true
                        Log.d("뭘까", snapshot.getValue().toString())
                    } else {
                        itemView.findViewById<CheckBox>(R.id.checkbox_bookmark).isChecked = false
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }
}