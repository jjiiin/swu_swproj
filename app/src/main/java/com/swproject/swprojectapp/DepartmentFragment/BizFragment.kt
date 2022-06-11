package com.swproject.swprojectapp.DepartmentFragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import kotlin.properties.Delegates


class BizFragment : Fragment() {
    val noticeDatas = mutableListOf<NoticeData>()
    val rvAdapter = RVAdapter(noticeDatas)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_biz, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rv)
        rv.adapter = rvAdapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.addItemDecoration(DividerItemDecoration(context, 1))
        var clickIndex: Int by Delegates.observable(1) { props, old, new ->
            val old_resourceName = resources.getIdentifier(
                "btn${old}",
                "id",
                "com.swproject.swprojectapp.DepartmentFragment"
            )
            val new_resourceName = resources.getIdentifier(
                "btn${new}",
                "id",
                "com.swproject.swprojectapp.DepartmentFragment"
            )
            view.findViewById<TextView>(old_resourceName)
                .setBackgroundColor(Color.parseColor("000000"))
            view.findViewById<TextView>(new_resourceName)
                .setBackgroundColor(Color.parseColor("FFDEDE"))
        }
        //크롤링
        //딜레이 시키기
        Handler().postDelayed(Runnable {
            crawlingThread(1)   //앱 들어가면 1페이지 보이게
        }, 250)

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
            val URL =
                "https://bizswu.swu.ac.kr/bbs/bbs/?bbs_no=12&page_no=${page}&sub_id=&search_kind=&search_text="//
            val doc: Document = Jsoup.connect(URL).get()
            val elements: Elements = doc.select("table").get(0)
                .select("tbody")
                .select("tr")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {
                    var top = false
                    if (element.getElementsByTag("td").get(0).text().contains("-"))
                        top = true

                    val title: String =
                        element.getElementsByTag("td").get(1).text()
                    val date: String = element.select("td").get(3).text()
                    val value: String = element.select("td a").attr("value").toString()
                    val link =
                        "https://bizswu.swu.ac.kr/bbs/bbs/view.php?bbs_no=12&data_no=${value}&page_no=${page}&sub_id="
                    val noticeData = NoticeData(title, date, link, "biz" + value, top)
                    Log.d("notice_", noticeData.toString())
                    noticeDatas.add(noticeData)
                    //북마크 저장할때 사용할 키
                    //val pushKey = FBRef.bookmarkRef.child(Auth.current_uid).push().key
                    //pushKeyList.add("biz" + value)
                }

                //UI에 접근할 수 있음
                requireActivity().runOnUiThread {
                    rvAdapter.notifyDataSetChanged()
                }
            }
        }.join()
    }
}