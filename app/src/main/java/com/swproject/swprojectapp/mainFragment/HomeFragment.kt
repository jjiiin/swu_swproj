package com.swproject.swprojectapp.mainFragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.swproject.swprojectapp.DepartmentFragment.Communications_Media_Fragment
import com.swproject.swprojectapp.DepartmentFragment.Software_Fragment
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.SWU.Scholarship_Fragment


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_include_drawer, container, false)

        //처음에 장학 공지 자동으로 보이게
        parentFragmentManager.beginTransaction().replace(R.id.view, Scholarship_Fragment()).commit()

        //메뉴 오픈
        view.findViewById<ImageView>(R.id.menu_btn).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }

        view.findViewById<TextView>(R.id.tv_scholarship).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "장학"
                parentFragmentManager.beginTransaction().replace(R.id.view, Scholarship_Fragment()).commit()
            },250)

        }
        view.findViewById<TextView>(R.id.tv_software).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "소프트웨어융합학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, Software_Fragment()).commit()
            },250)
        }
        view.findViewById<TextView>(R.id.tv_communication_media).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "언론영상학부"
                parentFragmentManager.beginTransaction().replace(R.id.view, Communications_Media_Fragment()).commit()
            },250)
        }

        // Inflate the layout for this fragment
        return view
    }


}