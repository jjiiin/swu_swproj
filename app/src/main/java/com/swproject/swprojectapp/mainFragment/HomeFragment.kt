package com.swproject.swprojectapp.mainFragment

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.swproject.swprojectapp.Bookmark_Activity
import com.swproject.swprojectapp.DepartmentFragment.*
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.utils.FBRef


class HomeFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    var dept_ids = arrayOf(
        R.id.tv_korean, R.id.tv_english, R.id.tv_french, R.id.tv_german, R.id.tv_japanese, R.id.tv_history, R.id.tv_christian,
        R.id.tv_library, R.id.tv_socialwelfare, R.id.tv_children, R.id.tv_publicAdministration, R.id.tv_communication_media,R.id.tv_eduPsychology, R.id.tv_sports,
        R.id.tv_chemistry, R.id.tv_gardening, R.id.tv_food,
        R.id.tv_biz, R.id.tv_digitalMedia, R.id.tv_security_dept, R.id.tv_software, R.id.tv_industrialDesign
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_include_drawer, container, false)
        auth = Firebase.auth
        val res = resources

        val myRef = FBRef.usersRef.child(auth.currentUser!!.uid)
        var select_dept = arrayOfNulls<Int>(3)
        // 선택한 학과 가져오기
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                select_dept[0] = Integer.parseInt(snapshot.child("major1").value.toString())
                select_dept[1] = Integer.parseInt(snapshot.child("major2").value.toString())
                select_dept[2] = Integer.parseInt(snapshot.child("major3").value.toString())

                 //선택한 학과만 메뉴에 보이기
                var depts_array = res.getStringArray(R.array.majorList)
                for (i in 0 until dept_ids.count()) {
                    var gone = true
                    for (j in 0..2)
                        if ((depts_array[select_dept[j]!!].equals(
                                view.findViewById<TextView>(
                                    dept_ids[i]
                                ).text
                            ))
                        )
                            gone = false
                    if (gone)
                        view.findViewById<TextView>(dept_ids[i]).visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // 읽어오기 실패했을 경우
            }
        })


        view.findViewById<ImageView>(R.id.image_go_bookmark).setOnClickListener {
            val intent = Intent(requireActivity(), Bookmark_Activity::class.java)
            startActivity(intent)
        }


        //처음에 학사 공지 자동으로 보이게
        parentFragmentManager.beginTransaction().replace(R.id.view, OneFragement()).commit()

        //메뉴 오픈
        view.findViewById<ImageView>(R.id.menu_btn).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(GravityCompat.START)
        }


        // 학교 공지사항
        view.findViewById<TextView>(R.id.tv_one).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "학사"
                parentFragmentManager.beginTransaction().replace(R.id.view, OneFragement()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_scholarship).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "장학"
                parentFragmentManager.beginTransaction().replace(R.id.view, TwoFragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_three).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "행사"
                parentFragmentManager.beginTransaction().replace(R.id.view, ThreeFragement())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_four).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "채용/취업"
                parentFragmentManager.beginTransaction().replace(R.id.view, FourFragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_five).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            //딜레이 시키기
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "일반/봉사"
                parentFragmentManager.beginTransaction().replace(R.id.view, FiveFragement())
                    .commit()
            }, 250)
        }


        // 인문대
        view.findViewById<TextView>(R.id.tv_korean).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "국어국문학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, Korean_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_english).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "영어영문학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, English_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_french).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "불어불문학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, French_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_german).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "독어독문학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, German_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_japanese).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "일어일문학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, Japanese_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_history).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "사학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, History_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_christian).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "기독교학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, Christian_Fragment())
                    .commit()
            }, 250)
        }


        // 사과대
        view.findViewById<TextView>(R.id.tv_library).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "문헌정보학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, LibraryInfoFragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_socialwelfare).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "사회복지학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, SocialWelfare_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_children).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "아동학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Children_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_publicAdministration).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "행정학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, PublicAdministration_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_communication_media).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "언론영상학부"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Communications_Media_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_eduPsychology).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "교육심리학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, EduPsychology_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_sports).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "스포츠운동과학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Sports_Fragment()).commit()
            }, 250)
        }

        // 자과대
        view.findViewById<TextView>(R.id.tv_chemistry).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "화학생명환경과학부"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Chemistry_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_gardening).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "원예생명조경학과"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Gardening_Fragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_food).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "식품응용시스템학부"
                parentFragmentManager.beginTransaction()
                    .replace(R.id.view, Food_Fragment()).commit()
            }, 250)
        }


        // 미산융
        view.findViewById<TextView>(R.id.tv_biz).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "경영학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, BizFragment()).commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_digitalMedia).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "디지털미디어학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, DigitalMedia_fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_security_dept).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "정보보호학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, SecurityDeptFragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_software).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "소프트웨어융합학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, Software_Fragment())
                    .commit()
            }, 250)
        }
        view.findViewById<TextView>(R.id.tv_industrialDesign).setOnClickListener {
            view.findViewById<DrawerLayout>(R.id.drawer_layout).closeDrawer(GravityCompat.START)
            Handler().postDelayed(Runnable {
                view.findViewById<TextView>(R.id.title).text = "산업디자인학과"
                parentFragmentManager.beginTransaction().replace(R.id.view, IndustrialDesign_Fragment())
                    .commit()
            }, 250)
        }


        // Inflate the layout for this fragment
        return view
    }


}