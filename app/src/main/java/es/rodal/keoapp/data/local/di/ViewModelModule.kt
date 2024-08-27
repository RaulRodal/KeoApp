package es.rodal.keoapp.data.local.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.data.domain.repository.RecordatorioRepositoryImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

//    @Binds
//    abstract fun bindRecordatorioRepository(
//        impl: RecordatorioRepositoryImpl
//    ): RecordatorioRepository
}