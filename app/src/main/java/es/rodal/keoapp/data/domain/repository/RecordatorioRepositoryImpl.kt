package es.rodal.keoapp.data.domain.repository

import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.local.database.RecordatorioDao
import es.rodal.keoapp.data.mapper.toRecordatorio
import es.rodal.keoapp.data.mapper.toRecordatorioEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date

class RecordatorioRepositoryImpl(
    private val dao: RecordatorioDao
): RecordatorioRepository {
    override suspend fun getRecordatorios(): Flow<List<Recordatorio>> {
        return dao.getRecordatorios().map { entities ->
            entities.map { it.toRecordatorio() }
        }
    }

    override suspend fun getRecordatoriosByDate(date: Date): Flow<List<Recordatorio>> {
        return dao.getRecordatoriosByDate(date).map { entities ->
            entities.map { it.toRecordatorio() }
        }
    }

    override suspend fun insertRecordatorio(item: Recordatorio) {
        dao.insertRecordatorio(item.toRecordatorioEntity())
    }

    override suspend fun updateRecordatorio(item: Recordatorio) {
        dao.updateRecordatorio(item.toRecordatorioEntity())
    }

    override suspend fun deleteRecordatorio(item: Recordatorio) {
        dao.deleteRecordatorio(item.toRecordatorioEntity())
    }

}