package es.rodal.keoapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import es.rodal.keoapp.data.domain.model.Recordatorio

class RecordatorioNotificationService(
    private val context: Context
) {
    fun scheduleNotification(recordatorio: Recordatorio) {
        val intent = Intent(context, RecordatorioNotificationReceiver::class.java)
        intent.putExtra(RECORDATORIO_INTENT, recordatorio)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            //si no existe crea un id con el hashcode
            recordatorio.id?.toInt() ?: recordatorio.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = recordatorio.recordatorioTime.time

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                alarmService.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
                Log.d("RNServiceADD", "Añadiendo alarma ${recordatorio.id}")
            } catch (exception: SecurityException) {
                Log.e("RNService", "Error al programar la alarma")
               ///FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }

        ///analyticsHelper.trackNotificationScheduled(recordatorio)
    }

    fun deleteNotification(recordatorio: Recordatorio) {
        val intent = Intent(context, RecordatorioNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            recordatorio.id?.toInt() ?: recordatorio.hashCode(),
            intent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        Log.d("RNServiceDELETE", "Cancelando alarma ${recordatorio.id}")
        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.cancel(pendingIntent)
    }
    companion object {
        const val RECORDATORIO_CHANNEL_ID = "recordatorio_channel"
    }
}