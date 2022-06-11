package com.swproject.swprojectapp.DepartmentFragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import kotlin.concurrent.thread
import kotlin.properties.Delegates

class LibraryInfoFragment : Fragment() {
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
            crawlingThread(1)   //앱 들어가면 1페이지 보이게
        },250)

        //앱 들어가면 1페이지 클릭된것처럼
        view.findViewById<TextView>(R.id.btn1)
            .setTextColor(Color.parseColor("#FFFFFF"))
        view.findViewById<TextView>(R.id.btn1)
            .setBackgroundColor(Color.parseColor("#a53235"))

        view.findViewById<TextView>(R.id.btn1).setOnClickListener {
            crawlingThread(1)
            clickIndex = 1
        }
        view.findViewById<TextView>(R.id.btn2).setOnClickListener {
            crawlingThread(2)
            clickIndex = 2
        }
        view.findViewById<TextView>(R.id.btn3).setOnClickListener {
            crawlingThread(3)
            clickIndex = 3
        }
        view.findViewById<TextView>(R.id.btn4).setOnClickListener {
            crawlingThread(4)
            clickIndex = 4
        }
        view.findViewById<TextView>(R.id.btn5).setOnClickListener {
            crawlingThread(5)
            clickIndex = 5
        }
        view.findViewById<TextView>(R.id.btn6).setOnClickListener {
            crawlingThread(6)
            clickIndex = 6
        }
        view.findViewById<TextView>(R.id.btn7).setOnClickListener {
            crawlingThread(7)
            clickIndex = 7
        }
        view.findViewById<TextView>(R.id.btn8).setOnClickListener {
            crawlingThread(8)
            clickIndex = 8
        }
        view.findViewById<TextView>(R.id.btn9).setOnClickListener {
            crawlingThread(9)
            clickIndex = 9
        }
        view.findViewById<TextView>(R.id.btn10).setOnClickListener {
            crawlingThread(10)
            clickIndex = 10
        }
        return view
    }

    fun crawlingThread(page: Int) {
        //스레드 생성
        thread {
            val URL = "http://swulis.net/?pageid=${page}&page_id=51&mod=list"
            val doc: Document = Jsoup.connect(URL).get()

            val elements: Elements = doc.select("div.kboard-list").select("tbody tr")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {
                    // 상단 고정은 안보이게
                    if(element.className().equals("kboard-list-notice"))
                        continue

                    val title = element.getElementsByClass("kboard-default-cut-strings").text()
                    val date = element.getElementsByClass("contents-item kboard-date").text()
                    if((title != "")){
                        val link = "http://swulis.net" + element.getElementsByTag("a").attr("href")
                        val link1 = element.getElementsByTag("a").attr("href")
                        val link2 = link1.split("&uid=")
                        val id = link2[1]
                        val noticeData = NoticeData(title,date, link, "libraryInfo" + id)
                        noticeDatas.add(noticeData)
                    }

                }
                //UI에 접근할 수 있음
                requireActivity().runOnUiThread {
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }.join()

    }
}