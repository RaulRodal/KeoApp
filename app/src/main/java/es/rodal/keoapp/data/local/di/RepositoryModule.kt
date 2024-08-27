package es.rodal.keoapp.data.local.di

import android.app.Application
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.data.domain.repository.RecordatorioRepositoryImpl
import es.rodal.keoapp.data.local.database.AppDatabase
import es.rodal.keoapp.data.local.database.RecordatorioDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecordatorioRepository(
        impl: RecordatorioRepositoryImpl
    ): RecordatorioRepository
}
