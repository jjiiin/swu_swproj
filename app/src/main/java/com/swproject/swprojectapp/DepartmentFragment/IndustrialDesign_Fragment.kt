package com.swproject.swprojectapp.DepartmentFragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.swproject.swprojectapp.Adapter.RVAdapter
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.dataModel.NoticeData
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class IndustrialDesign_Fragment : Fragment() {
    val noticeDatas = mutableListOf<NoticeData>()
    val rvAdapter = RVAdapter(noticeDatas)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(DividerItemDecoration(context, 1))
        var clickIndex: Int by Delegates.observable(1) { props, old, new ->
            val old_resourceName = resources.getIdentifier(
                "btn${old}",
                "id",
                requireContext().packageName
            )
            val new_resourceName = resources.getIdentifier(
                "btn${new}",
                "id",
                requireContext().packageName
            )
            //이전에 선택된것 효과 해제
            view.findViewById<TextView>(old_resourceName)
                .setTextColor(Color.parseColor("#000000"))
            view.findViewById<TextView>(old_resourceName)
                .setBackgroundColor(Color.parseColor("#FFFFFF"))

            //현재 선택된것 효과 적용
            view.findViewById<TextView>(new_resourceName)
                .setTextColor(Color.parseColor("#FFFFFF"))
            view.findViewById<TextView>(new_resourceName)
                .setBackgroundColor(Color.parseColor("#a53235"))
        }

        //크롤링
        //딜레이 시키기
        Handler().postDelayed(Runnable {
            searchCrawlingThread(1)   //앱 들어가면 1페이지 보이게
        },250)

        //앱 들어가면 1페이지 클릭된것처럼
        view.findViewById<TextView>(R.id.btn1)
            .setTextColor(Color.parseColor("#FFFFFF"))
        view.findViewById<TextView>(R.id.btn1)
            .setBackgroundColor(Color.parseColor("#a53235"))

        var searchValue = getActivity()?.findViewById<AutoCompleteTextView>(R.id.editText)?.text
        view.findViewById<TextView>(R.id.btn1).setOnClickListener {
            searchCrawlingThread(1, searchValue.toString())
            clickIndex = 1
        }
        view.findViewById<TextView>(R.id.btn2).setOnClickListener {
            searchCrawlingThread(2, searchValue.toString())
            clickIndex = 2
        }
        view.findViewById<TextView>(R.id.btn3).setOnClickListener {
            searchCrawlingThread(3, searchValue.toString())
            clickIndex = 3
        }
        view.findViewById<TextView>(R.id.btn4).setOnClickListener {
            searchCrawlingThread(4, searchValue.toString())
            clickIndex = 4
        }
        view.findViewById<TextView>(R.id.btn5).setOnClickListener {
            searchCrawlingThread(5, searchValue.toString())
            clickIndex = 5
        }
        view.findViewById<TextView>(R.id.btn6).setOnClickListener {
            searchCrawlingThread(6, searchValue.toString())
            clickIndex = 6
        }
        view.findViewById<TextView>(R.id.btn7).setOnClickListener {
            searchCrawlingThread(7, searchValue.toString())
            clickIndex = 7
        }
        view.findViewById<TextView>(R.id.btn8).setOnClickListener {
            searchCrawlingThread(8, searchValue.toString())
            clickIndex = 8
        }
        view.findViewById<TextView>(R.id.btn9).setOnClickListener {
            searchCrawlingThread(9, searchValue.toString())
            clickIndex = 9
        }
        view.findViewById<TextView>(R.id.btn10).setOnClickListener {
            searchCrawlingThread(10, searchValue.toString())
            clickIndex = 10
        }


        getActivity()?.findViewById<Button>(R.id.searchBtn)?.setOnClickListener {
            searchValue = getActivity()?.findViewById<AutoCompleteTextView>(R.id.editText)?.text
            searchCrawlingThread(1, searchValue.toString())
            clickIndex = 1
        }
        return view
    }

    fun searchCrawlingThread(page: Int, value: String = "") {
        //스레드 생성
        thread {
            val URL = "http://swuid.hosting.paran.com/wp/?cat=1&paged=${page}&s=${value}"
            try {
                val doc: Document = Jsoup.connect(URL).get()
                val elements: Elements = doc.select("section.site-content").select("article")
                if (elements != null) {
                    noticeDatas.clear()
                    for (element in elements) {
                        val title = element.getElementsByClass("entry-title").text()
                        val datetemp = element.getElementsByClass("entry-date").text()
                        val arr = datetemp.split("년 ", "월 ", "일")
                        val date = arr[0]+"-"+arr[1]+"-"+arr[2]
                        if((title != "")){
                            val link = element.getElementsByTag("a").attr("href")
                            val link1 = link.split("?p=")
                            val id = link1[1]
                            val noticeData = NoticeData(title,date, link, "industrialDesign" +id)
                            noticeDatas.add(noticeData)
                        }
                    }
                    //UI에 접근할 수 있음
                    requireActivity().runOnUiThread {
                        rvAdapter.notifyDataSetChanged()
                    }
                }
            } catch (e : Exception){
                //UI에 접근할 수 있음
                requireActivity().runOnUiThread {
                    Toast.makeText(context,"페이지가 없습니다",Toast.LENGTH_LONG).show()
                }
            }
        }.join()


    }
}