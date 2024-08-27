package es.rodal.keoapp.data.local.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.data.domain.repository.RecordatorioRepositoryImpl
import es.rodal.keoapp.data.local.database.AppDatabase
import es.rodal.keoapp.data.local.database.RecordatorioDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideRecordatorioDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "recordatorio_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRecordatorioDao(db: AppDatabase): RecordatorioDao {
        return db.recordatorioDao
    }

//    @Provides
//    @Singleton
//    fun provideRecordatorioRepository(dao: RecordatorioDao): RecordatorioRepository {
//        return RecordatorioRepositoryImpl(dao)
//    }
}
