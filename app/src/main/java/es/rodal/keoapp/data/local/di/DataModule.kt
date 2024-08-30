package es.rodal.keoapp.data.local.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.data.domain.repository.RecordatorioRepositoryImpl
import es.rodal.keoapp.data.local.database.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMedicationDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "recordatorio_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedicationRepository(
        db: AppDatabase
    ): RecordatorioRepository {
        return RecordatorioRepositoryImpl(
            dao = db.recordatorioDao
        )
    }
}
