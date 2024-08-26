package es.rodal.keoapp.ui.screens.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.model.isSameDay
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class RecordatorioCalendarViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository
) : ViewModel() {

    var recordatorioState by mutableStateOf(emptyList<Recordatorio>())
    var recordatoriosFilteredState by mutableStateOf(emptyList<Recordatorio>())

    init {
        loadRecordatorios()
    }

    fun loadRecordatorios() {
        viewModelScope.launch {
            recordatorioRepository.getRecordatorios().collect { recordatorios ->
                recordatorioState = recordatorios
                filterRecordatorios(Date())
            }
        }
    }

    fun filterRecordatorios(day: Date) {
        recordatoriosFilteredState = recordatorioState.filter { it.recordatorioTime.isSameDay(day) }
    }
}