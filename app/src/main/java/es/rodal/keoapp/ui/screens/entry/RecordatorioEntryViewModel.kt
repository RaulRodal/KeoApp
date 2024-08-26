package es.rodal.keoapp.ui.screens.entry

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.RecordatorioNotificationService
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecordatorioEntryViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recordatorioId: Long = checkNotNull(savedStateHandle[NavigationDestinations.RecordatorioEntryDestination.recordatorioIdArg])

    var recordatorioState = MutableStateFlow(Recordatorio("", "", Date()))

    init {
        viewModelScope.launch {
            recordatorioRepository.getRecordatorioById(recordatorioId).collect { recordatorio ->
                recordatorioState.value = recordatorio
            }
        }
    }

    fun addRecordatorio(context: Context, recordatorio: Recordatorio) {
        viewModelScope.launch {
            //Guardo el id para poder añadir el Recordatorio con id en el scheduleNotification
            val idRecordatorioAdded = recordatorioRepository.insertRecordatorio(recordatorio)
            val service = RecordatorioNotificationService(context)
            val recordatorioAdded = recordatorio.copy(id = idRecordatorioAdded)
            service.scheduleNotification(recordatorioAdded)

            //_isRecordatorioSaved.emit(recordatorioAdded)
        }
    }

    fun updateRecordatorio(context: Context, recordatorio: Recordatorio) {
        viewModelScope.launch {
            val oldRecordatorio = recordatorioRepository.getRecordatorioById(recordatorio.id).first()
            val service = RecordatorioNotificationService(context)
            if (oldRecordatorio.recordatorioTime != recordatorio.recordatorioTime) {
                service.deleteNotification(oldRecordatorio)
                service.scheduleNotification(recordatorio)
            }
            recordatorioRepository.updateRecordatorio(recordatorio)
            service.scheduleNotification(recordatorio)
        }
    }
}