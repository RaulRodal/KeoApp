package es.rodal.keoapp.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.ui.navigation.NavigationDestinations
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val recordatorioState: StateFlow<Recordatorio> =
        recordatorioRepository.getRecordatorioById(recordatorioId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = Recordatorio("", "", Date())
            )


        fun deleteRecordatorio(recordatorio: Recordatorio) {
            viewModelScope.launch {
                recordatorioRepository.deleteRecordatorio(recordatorio)
            }
        }
}
