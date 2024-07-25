package es.rodal.keoapp.data.mapper

import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.local.database.RecordatorioEntity
import java.util.Date

fun Recordatorio.toRecordatorioEntity(): RecordatorioEntity = RecordatorioEntity(
            id = id,
            name = name,
            description = description,
            active = active,
            recordatorioDone = recordatorioDone,
            recordatorioTime = recordatorioTime
)


//fun RecordatorioEntity.toRecordatorio(): Recordatorio = Recordatorio(
//        id = id,
//        name = name,
//        description = description,
//        active = active,
//        recordatorioDone = recordatorioDone,
//        recordatorioTime = recordatorioTime
//)

fun RecordatorioEntity?.toRecordatorio(): Recordatorio {
    return if (this != null) {
        Recordatorio(
            id = id,
            name = name,
            description = description,
            active = active,
            recordatorioDone = recordatorioDone,
            recordatorioTime = recordatorioTime
        )
    } else {
        // Handle the case where the entity is null, e.g., return a default Recordatorio object
        Recordatorio("", "", Date())
    }
}