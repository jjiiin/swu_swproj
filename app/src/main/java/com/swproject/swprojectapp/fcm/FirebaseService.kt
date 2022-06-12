package com.swproject.swprojectapp.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.swproject.swprojectapp.PushLinkActivity
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.utils.FBRef
import java.util.*

class FirebaseService : FirebaseMessagingService() {
    private lateinit var auth:FirebaseAuth
    private var token:String = ""
    //사용자의 토큰..딱히 필요 없을 듯
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        //Log.d("fcm_",message.toString())
        if(message.data.isNotEmpty()) {
//            val title = message.data["title"].toString()
//            val body = message.data["content"].toString()
            val title=message.notification?.title.toString()
            val body=message.notification?.body.toString()
            val date = Calendar.getInstance().time
            val link=message.data?.getValue("URL").toString()
            Log.d("fcm_",message.data?.getValue("URL").toString())
            createNotificationChannel()
            sendNotification(title, body, link)
//            notiList(title,body,date)

        }else{
            Log.d("fcm_",message.data.toString()+"에러 발생")
        }

    }

    private fun createNotificationChannel(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("Test_Channel", name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title:String, body:String,link:String){
        val intent= Intent(this, PushLinkActivity::class.java)
        intent.putExtra("link",link)
        val mPendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this,"Test_Channel")
            .setSmallIcon(R.drawable.noti)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(mPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)){
            notify(123,builder.build())
        }
    }

    private fun notiList(title:String, body:String, date:Date){
        val newNoti=NotiModel(title,body,date)
        auth= Firebase.auth
        FBRef.usersRef.child(auth.currentUser?.uid.toString())
            .child("Notification")
            .push()
            .setValue(newNoti)
    }
}