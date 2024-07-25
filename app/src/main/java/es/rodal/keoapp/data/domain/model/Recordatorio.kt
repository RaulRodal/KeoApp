package es.rodal.keoapp.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Parcelize
data class Recordatorio (
    val id: Long = 0L,
    val name: String,
    val description: String,
    val active: Boolean,
    val recordatorioDone: Boolean,
    val recordatorioTime: Date
) : Parcelable {
    constructor(name: String, description: String, recordatorioTime: Date) :
            this(0, name, description, true, false, recordatorioTime)


}

fun Date.getFormattedDateTime(): String {
    val dateTimeFormat = SimpleDateFormat("EEE, MMM d, yyyy 'at' h:mm a", Locale.getDefault())
    return dateTimeFormat.format(this)
}

fun Date.getFormattedDate(): String {
    val dateFormat = SimpleDateFormat("EEE, MMM d, yyyy", Locale.getDefault())
    return dateFormat.format(this)
}

fun Date.getFormattedTime(): String {
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    return timeFormat.format(this)
}

fun Date.isSameDay(other: Date): Boolean {
    val calendar1 = Calendar.getInstance().apply { time = this@isSameDay }
    val calendar2 = Calendar.getInstance().apply { time = other }
    return calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
            calendar1.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
}