package es.rodal.keoapp.ui.screens.notification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class RecordatorioNotificationViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val recordatorioId: Long =
        checkNotNull(savedStateHandle[NavigationDestinations.RecordatorioDetailDestination.recordatorioIdArg])

    var recordatorioState = MutableStateFlow(Recordatorio("", "", Date()))

    init {
        viewModelScope.launch {
            recordatorioRepository.getRecordatorioById(recordatorioId).collect { recordatorio ->
                recordatorioState.value = recordatorio
            }
        }
    }

}