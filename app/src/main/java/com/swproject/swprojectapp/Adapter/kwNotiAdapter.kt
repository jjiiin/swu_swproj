package com.swproject.swprojectapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.kwModel
import java.util.*

class kwNotiAdapter(val items:MutableList<kwModel>) : RecyclerView.Adapter<kwNotiAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): kwNotiAdapter.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.keywordnoti_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: kwNotiAdapter.ViewHolder, position: Int) {
        val item=items[position]
        holder.body.text=item.body
        //시간
        val calendar:Calendar = Calendar.getInstance()
        Log.d("time_1",calendar.get(Calendar.YEAR).toString())
        Log.d("time_3",calendar.toString())
        Log.d("time_2",item.date.year.toString())
        if ((calendar.get(Calendar.YEAR) - item.date.year) != 0) {
            holder.date.text =
                (calendar.get(Calendar.YEAR) - item.date.year).toString() + "년 전"
        } else if ((calendar.get(Calendar.MONTH)+1 - item.date.month) != 0) {
            holder.date.text =
                (calendar.get(Calendar.MONTH)+1 - item.date.month).toString() + "달 전"
        } else if ((calendar.get(Calendar.DAY_OF_MONTH) - item.date.day) != 0) {
            holder.date.text =
                (calendar.get(Calendar.DAY_OF_MONTH) - item.date.day).toString() + "일 전"
        } else if ((calendar.get(Calendar.HOUR_OF_DAY)- item.date.hour) != 0) {
            holder.date.text =
                (calendar.get(Calendar.HOUR_OF_DAY) - item.date.hour).toString() + "시간 전"
        } else if((calendar.get(Calendar.MINUTE) - item.date.minute) != 0) {
            holder.date.text =
                (calendar.get(Calendar.HOUR_OF_DAY) - item.date.minute).toString() + "분 전"
        }

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val body: TextView =itemView.findViewById(R.id.content)
        val date: TextView =itemView.findViewById(R.id.date)
    }

    // (2) 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener: OnItemClickListener

}