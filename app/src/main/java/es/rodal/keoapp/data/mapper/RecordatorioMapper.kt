package es.rodal.keoapp.data.mapper

import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.local.database.RecordatorioEntity

    fun Recordatorio.toRecordatorioEntity(): RecordatorioEntity {
        return RecordatorioEntity(
            id = id ?: 0L,
            name = name,
            recurrence = recurrence,
            endDate = endDate,
            recordatorioDone = recordatorioDone,
            recordatorioTime = recordatorioTime
        )
    }

    fun RecordatorioEntity.toRecordatorio(): Recordatorio {
        return Recordatorio(
            id = id,
            name = name,
            recurrence = recurrence,
            endDate = endDate,
            recordatorioDone = recordatorioDone,
            recordatorioTime = recordatorioTime
        )
    }
