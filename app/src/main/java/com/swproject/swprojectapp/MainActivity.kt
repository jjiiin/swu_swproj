package com.swproject.swprojectapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.swproject.swprojectapp.databinding.ActivityMainBinding
import com.swproject.swprojectapp.mainFragment.CommuFragment
import com.swproject.swprojectapp.mainFragment.HomeFragment
import com.swproject.swprojectapp.mainFragment.MyFragment
import com.swproject.swprojectapp.mainFragment.NotiFragment

class MainActivity : AppCompatActivity() {
    private  var mbinding : ActivityMainBinding?=null
    private val binding get()=mbinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        mbinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bn_main=binding.bnNav
        bn_main.itemIconTintList = null


        //하단 네비게이션 바
        bn_main.run{
            setOnNavigationItemSelectedListener {
                when(it.itemId) {
                    R.id.home-> {
                        // 다른 프래그먼트 화면으로 이동하는 기능
                        val homeFragment = HomeFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, homeFragment).commit()
                    }
                    R.id.noti -> {
                        val notiFragment = NotiFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, notiFragment).commit()
                    }
                    R.id.commu -> {
                        val commuFragment = CommuFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, commuFragment).commit()
                    }
                    R.id.my -> {
                        val myFragment = MyFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.fl_container, myFragment).commit()
                    }
                }
                true
            }
            selectedItemId = R.id.home
        }
    }
}