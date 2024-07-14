package es.rodal.keoapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

const val RECORDATORIO_INTENT = "recordatorio_intent"
const val RECORDATORIO_NOTIFICATION = "recordatorio_notification"
class RecordatorioNotificationReceiver: BroadcastReceiver() {

    companion object{
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Alarm", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "default")
            .setContentTitle("Alarma")
            .setContentText("La alarma se ha activado.")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .build()

        notificationManager.notify(1, notification)
    }
}