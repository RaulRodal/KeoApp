package es.rodal.keoapp.data.mapper

import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.local.database.RecordatorioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

fun Recordatorio.toRecordatorioEntity(): RecordatorioEntity {
        return RecordatorioEntity(
            id = id ?: 0L,
            name = name,
            description = description,
            active = active,
            recordatorioDone = recordatorioDone,
            recordatorioTime = recordatorioTime
        )
    }

fun RecordatorioEntity.toRecordatorio(): Recordatorio {
    return Recordatorio(
        id = id,
        name = name,
        description = description,
        active = active,
        recordatorioDone = recordatorioDone,
        recordatorioTime = recordatorioTime
    )
}
