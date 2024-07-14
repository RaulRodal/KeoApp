package es.rodal.keoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val RECORDATORIO_INTENT = "recordatorio_intent"
const val RECORDATORIO_NOTIFICATION = "recordatorio_notification"
class RecordatorioNotificationReceiver: BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {

        val title = intent.getStringExtra("title") ?: "Alarma"
        val description = intent.getStringExtra("description") ?: "Descripción de la alarma"

        showNotification(context, title, description)

    }

    private fun showNotification(context: Context, title: String, description: String) {
        val notificationId = System.currentTimeMillis().toInt()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel("default", "Alarm", NotificationManager.IMPORTANCE_DEFAULT)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(context, channel.id)
            .setSmallIcon(R.mipmap.ic_launcher_foreground) // Asegúrate de tener este recurso de ícono
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}