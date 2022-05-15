package com.swproject.swprojectapp.Adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.NoticeData

class RVAdapter(val items: MutableList<NoticeData>) : RecyclerView.Adapter<RVAdapter.ViewHolder>() {

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


            // 결과 화면으로 intent
//            val link = items.get(position).link
//            val intent = Intent(holder.itemView.context, ClickResult::class.java)
//            intent.putExtra("result_link", link)
//            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(item: NoticeData) {
            itemView.findViewById<TextView>(R.id.tv_title).text = item.title
            itemView.findViewById<TextView>(R.id.tv_date).text = item.date
        }
    }
}