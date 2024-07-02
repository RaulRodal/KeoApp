package es.rodal.keoapp.data.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Recordatorio (
    val id: Long?,
    val name: String,
    val recurrence: String,
    val endDate: Date,
    val recordatorioDone: Boolean,
    val recordatorioTime: Date
) : Parcelable {
    constructor(name: String, recordatorioDone: Boolean, recordatorioTime: Date) :
            this(null, name, "", Date(), recordatorioDone, recordatorioTime)
}