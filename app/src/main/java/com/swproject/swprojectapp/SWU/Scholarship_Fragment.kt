package com.swproject.swprojectapp.SWU

import android.os.Bundle
import android.os.Handler
import android.util.Log
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

class Scholarship_Fragment : Fragment() {
    val noticeDatas = mutableListOf<NoticeData>()
    val rvAdapter = RVAdapter(noticeDatas)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_scholarship, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(DividerItemDecoration(context, 1))

        //크롤링
        crawlingThread(1)   //앱 들어가면 1페이지 보이게

        view.findViewById<TextView>(R.id.btn1).setOnClickListener {
            crawlingThread(1)
        }
        view.findViewById<TextView>(R.id.btn2).setOnClickListener {
            crawlingThread(2)
        }
        view.findViewById<TextView>(R.id.btn3).setOnClickListener {
            crawlingThread(3)
        }
        view.findViewById<TextView>(R.id.btn4).setOnClickListener {
            crawlingThread(4)
        }
        view.findViewById<TextView>(R.id.btn5).setOnClickListener {
            crawlingThread(5)
        }
        view.findViewById<TextView>(R.id.btn6).setOnClickListener {
            crawlingThread(6)
        }
        view.findViewById<TextView>(R.id.btn7).setOnClickListener {
            crawlingThread(7)
        }
        view.findViewById<TextView>(R.id.btn8).setOnClickListener {
            crawlingThread(8)
        }
        view.findViewById<TextView>(R.id.btn9).setOnClickListener {
            crawlingThread(9)
        }
        view.findViewById<TextView>(R.id.btn10).setOnClickListener {
            crawlingThread(10)
        }
        return view
    }

    fun crawlingThread(page: Int) {
        //스레드 생성
        thread {
            val URL = "https://www.swu.ac.kr/www/noticeb.html"
            val doc: Document = Jsoup.connect(URL).get()
            //iframe이라는 요소를 이용해서 웹페이지 안에 다른 웹페이지를 삽입해 놓은 구조. 즉 src의 주소로 들어가야 공지사항 나옴
            val iframes: Elements = doc.select("iframe[id=mainFrm]")
            val src: String = iframes.attr("src")
            val URL2 = "https://www.swu.ac.kr/" + src + "&currentPage=${page}"
            val doc2: Document = Jsoup.connect(URL2).get()
            val elements: Elements = doc2.select("div.b_list_wrap tbody tr")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {
                    val title: String = element.getElementsByClass("title").text().toString()
                    val time: String = element.getElementsByTag("td").get(3).text().toString()
                    val pkid: String = element.getElementsByTag("a").attr("onclick").split("'")[3]
                    val link: String =
                        "https://www.swu.ac.kr//front/boardview.do?pkid=${pkid}&menuGubun=1&siteGubun=1&bbsConfigFK=5"
                    val noticeData = NoticeData(title, time, link)
                    noticeDatas.add(noticeData)
                }

                //UI에 접근할 수 있음
                requireActivity().runOnUiThread {
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }.join()

    }
}