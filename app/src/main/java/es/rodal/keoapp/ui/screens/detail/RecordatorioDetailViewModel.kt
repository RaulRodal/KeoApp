package es.rodal.keoapp.ui.screens.detail

import android.content.Context
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.RecordatorioNotificationService
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecordatorioDetailViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recordatorioId: Long = checkNotNull(savedStateHandle[NavigationDestinations.RecordatorioDetailDestination.recordatorioIdArg])


    var recordatorioState = MutableStateFlow(Recordatorio("", "", Date()))

    init {
        viewModelScope.launch {
            recordatorioRepository.getRecordatorioById(recordatorioId).collect { recordatorio ->
                recordatorioState.value = recordatorio
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
}
