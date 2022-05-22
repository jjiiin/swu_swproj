package com.swproject.swprojectapp.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.swproject.swprojectapp.R
import com.swproject.swprojectapp.utils.FBRef
import java.util.*

class FirebaseService : FirebaseMessagingService() {
    private lateinit var auth:FirebaseAuth
    private var token:String = ""
    //사용자의 토큰..딱히 필요 없을 듯
    override fun onNewToken(token: String) {
        this.token = super.onNewToken(token).toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val title = message.data["title"].toString()
        val body = message.data["content"].toString()
        val date = Calendar.getInstance().time

        createNotificationChannel()
        sendNotification(title, body)
        notiList(title,body,date)
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

    private fun sendNotification(title:String, body:String){
        var builder = NotificationCompat.Builder(this,"Test_Channel")
            .setSmallIcon(R.drawable.noti)
            .setContentTitle(title)
            .setContentText(body)
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