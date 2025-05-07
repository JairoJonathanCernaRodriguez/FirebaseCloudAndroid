package com.example.firebasecloudandroid

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class Mensajes : FirebaseMessagingService() {

    private val random= Random

    //reacciona cuando se recive un nuevo mensaje
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        sendNotification(message)
    }

    //mostrar la notificacion en la parte de arriba
    private fun sendNotification(message: RemoteMessage) {
        val intent= Intent(this,MainActivity::class.java).apply {
            addFlags(FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendinfIntent  =  PendingIntent.getActivity(
            this,0,intent,FLAG_IMMUTABLE
        )
        //crear canal de notificacion
        val channelId= this.getString(R.string.default_notification_channel_id)
        val notificationBuilder= NotificationCompat.Builder(this,channelId)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSmallIcon(R.drawable.baseline_notifications_active_24)
            .setAutoCancel(true)
            .setContentIntent(pendinfIntent)

        val manager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val channel= NotificationChannel(channelId,CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }
        manager.notify(random.nextInt(), notificationBuilder.build())
    }

    //se genera un nuevo toque para cada discpositivo cuando se ejecuta la app por
    //primera vez, nos sirve par enviar notificaciones para usuarios especificos
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    companion object {
        const val CHANNEL_NAME="Notification Channel"
    }

}