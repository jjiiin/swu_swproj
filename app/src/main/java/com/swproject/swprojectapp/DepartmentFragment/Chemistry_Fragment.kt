package com.swproject.swprojectapp.DepartmentFragment

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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

class Chemistry_Fragment : Fragment() {
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

        //크롤링
        //딜레이 시키기
        Handler().postDelayed(Runnable {
            crawlingThread()   //앱 들어가면 1페이지 보이게
        },250)
        view.findViewById<LinearLayout>(R.id.linearLayout2).visibility = View.GONE
        return view
    }

    fun crawlingThread() {
        //스레드 생성
        thread {
            val URL = "https://swuchemistry.weebly.com/news--events"
            val doc: Document = Jsoup.connect(URL).get()

            val elements: Elements = doc.select("div.blog-post")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {
                    if(element.className().equals("blog-page-nav-previous"))
                        break
                    val title = element.getElementsByClass("blog-title").text()
                    val date = element.getElementsByClass("blog-date").text()
                    if((title != "")){
                        val link = "https://swuchemistry.weebly.com/news--events"
                        val noticeData = NoticeData(title,date, link)
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