package es.rodal.keoapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import es.rodal.keoapp.data.domain.model.Recordatorio
import java.util.Date

class RecordatorioNotificationService(
    private val context: Context
) {
    fun scheduleNotification(recordatorio: Recordatorio) {
        val intent = Intent(context, RecordatorioNotificationReceiver::class.java).apply {
            putExtra(RECORDATORIO_INTENT, recordatorio)
            putExtra("title", recordatorio.name)
            putExtra("description", recordatorio.description)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            recordatorio.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = recordatorio.recordatorioTime.time
        val now = Date()

        if (time > now.time) {
            try {
                alarmService.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    time,
                    pendingIntent
                )
                Log.d("RNServiceADD", "Añadiendo alarma ${recordatorio.id}")

                // Habilitar receiver despues de reincio
                val receiver = ComponentName(context, RecordatorioNotificationReceiver::class.java)

                context.packageManager.setComponentEnabledSetting(
                    receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            } catch (exception: SecurityException) {
                Log.e("RNService", "Error al programar la alarma: ${exception.message}")
                // FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }

        // analyticsHelper.trackNotificationScheduled(recordatorio)
    }

    fun deleteNotification(recordatorio: Recordatorio) {
        val intent = Intent(context, RecordatorioNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            recordatorio.id.toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        Log.d("RNServiceDELETE", "Cancelando alarma ${recordatorio.id}")
        val alarmService = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmService.cancel(pendingIntent)

        // deshabilitar receiver despues de apagado
//        val receiver = ComponentName(context, RecordatorioNotificationReceiver::class.java)
//
//        context.packageManager.setComponentEnabledSetting(
//            receiver,
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP
//        )
    }
}
