package es.rodal.keoapp.data.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioDao {
    @Query("SELECT * FROM recordatorioentity")
    fun getRecordatorios(): Flow<List<RecordatorioEntity>>

//    @Query("SELECT * FROM recordatorioentity WHERE endDate > :date")
//    fun getRecordatoriosByDate(date: Date): Flow<List<RecordatorioEntity>>

    @Query("SELECT * FROM recordatorioentity WHERE id = :id")
    fun getRecordatorioById(id: Long): RecordatorioEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecordatorio(recordatorioEntity: RecordatorioEntity): Long

    @Delete
    suspend fun deleteRecordatorio(recordatorioEntity: RecordatorioEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateRecordatorio(recordatorioEntity: RecordatorioEntity)


}