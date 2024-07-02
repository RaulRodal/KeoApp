package es.rodal.keoapp.ui.recordatorio.entry

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.RecordatorioNotificationService
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecordatorioEntryViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository
) : ViewModel() {


    private val _isRecordatorioSaved = MutableSharedFlow<Unit>()
    val isRecordatorioSaved = _isRecordatorioSaved.asSharedFlow()
    fun addRecordatorio(context: Context, recordatorio: Recordatorio) {
        viewModelScope.launch {
            val recordatorioAdded = recordatorioRepository.insertRecordatorio(recordatorio)

            val service = RecordatorioNotificationService(context)
            service.scheduleNotification(recordatorio)

            _isRecordatorioSaved.emit(recordatorioAdded)
        }
    }
}