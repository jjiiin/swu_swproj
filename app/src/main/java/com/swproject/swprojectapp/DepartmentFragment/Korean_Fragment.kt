package com.swproject.swprojectapp.DepartmentFragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
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
import kotlin.properties.Delegates

class Korean_Fragment : Fragment() {
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


        val searchValue = getActivity()?.findViewById<AutoCompleteTextView>(R.id.editText)?.text
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
            val searchValue = getActivity()?.findViewById<AutoCompleteTextView>(R.id.editText)?.text
            searchCrawlingThread(1, searchValue.toString())
            clickIndex = 1
        }
        return view
    }

    fun searchCrawlingThread(page: Int, value: String = "") {
        //스레드 생성
        thread {
            val URL = "http://korean.swu.ac.kr/bbs/bbs/?bbs_no=5&page_no=${page}&search_kind=subject&search_text=${value}"
            val doc: Document = Jsoup.connect(URL).get()

            val elements: Elements = doc.select("div.dh-board").select("tbody tr")
            if (elements != null) {
                noticeDatas.clear()
                for (element in elements) {

                    val title = element.getElementsByClass("subject").text()
                    val date = element.getElementsByClass("text-center regdate").text()
                    if((title != "")){
                        val link = "http://korean.swu.ac.kr/bbs/bbs/view.php?bbs_no=5&data_no=" + element.getElementsByTag("a").attr("value")+"&page_no=${page}"
                        val id = element.getElementsByTag("a").attr("value")
                        val noticeData = NoticeData(title,date, link, "korean" + id)
                        noticeDatas.add(noticeData)
                        //북마크 저장할때 사용할 키
                        //val pushKey = FBRef.bookmarkRef.child(Auth.current_uid).push().key
                        //pushKeyList.add("korean" + id)
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