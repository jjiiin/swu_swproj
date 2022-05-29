package com.swproject.swprojectapp.DepartmentFragment

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
import com.swproject.swprojectapp.utils.Auth
import com.swproject.swprojectapp.utils.FBRef
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import kotlin.concurrent.thread

class German_Fragment : Fragment() {
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
            crawlingThread(1)   //앱 들어가면 1페이지 보이게
        },250)

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
            val URL = "https://german.swu.ac.kr/bbs/bbs/?bbs_no=6&page_no=${page}"
            val doc: Document = Jsoup.connect(URL).get()

            val elements: Elements = doc.select("table").select("tbody tr")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {
                    val title = element.getElementsByTag("a").text()
                    val date = element.getElementsByClass("text-center regdate").text()
                    if((title != "")){
                        val link = "https://german.swu.ac.kr/bbs/bbs/view.php?bbs_no=6&data_no=" + element.getElementsByTag("a").attr("value")+"&page_no=${page}"
                        val id = element.getElementsByTag("a").attr("value")
                        val noticeData = NoticeData(title,date, link, "german" + id)
                        noticeDatas.add(noticeData)
                        //북마크 저장할때 사용할 키
                        //val pushKey = FBRef.bookmarkRef.child(Auth.current_uid).push().key
                        //pushKeyList.add("german" + id)
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