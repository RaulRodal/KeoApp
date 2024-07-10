package es.rodal.keoapp.data.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class Recordatorio (
    val id: Long?,
    val name: String,
    val description: String,
    val active: Boolean,
    val recordatorioDone: Boolean,
    val recordatorioTime: Date
) : Parcelable {
    constructor(name: String, description: String, recordatorioTime: Date) :
            this(null, name, description, true, false, recordatorioTime)
}