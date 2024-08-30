package es.rodal.keoapp.ui.screens.history

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.RecordatorioNotificationService
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RecordatorioHistoryViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository
) : ViewModel() {

    var recordatorioState by mutableStateOf(emptyList<Recordatorio>())

    init {
        loadRecordatorios()
    }

    fun loadRecordatorios() {
        viewModelScope.launch {
            recordatorioRepository.getRecordatorios().collect { recordatorios ->
                recordatorioState = recordatorios
            }
        }
    }

    fun deleteRecordatorio(context: Context, recordatorio: Recordatorio) {
        viewModelScope.launch {
            recordatorioRepository.deleteRecordatorio(recordatorio)
            val service = RecordatorioNotificationService(context)
            service.deleteNotification(recordatorio)
        }
    }

    fun reverseActive(context: Context, recordatorio: Recordatorio) {
        viewModelScope.launch {
            recordatorioRepository.updateRecordatorio(recordatorio.copy(active = !recordatorio.active))
            val service = RecordatorioNotificationService(context)
            if (recordatorio.active) {
                service.deleteNotification(recordatorio)
            } else {
                service.scheduleNotification(recordatorio)
            }
        }
    }
}

