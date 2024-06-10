/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.rodal.keoapp.ui.recordatorio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import es.rodal.keoapp.data.RecordatorioRepository
import es.rodal.keoapp.ui.recordatorio.RecordatorioUiState.Error
import es.rodal.keoapp.ui.recordatorio.RecordatorioUiState.Loading
import es.rodal.keoapp.ui.recordatorio.RecordatorioUiState.Success
import javax.inject.Inject

@HiltViewModel
class RecordatorioViewModel @Inject constructor(
    private val recordatorioRepository: RecordatorioRepository
) : ViewModel() {

    val uiState: StateFlow<RecordatorioUiState> = recordatorioRepository
        .recordatorios.map<List<String>, RecordatorioUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addRecordatorio(name: String) {
        viewModelScope.launch {
            recordatorioRepository.add(name)
        }
    }
}

sealed interface RecordatorioUiState {
    object Loading : RecordatorioUiState
    data class Error(val throwable: Throwable) : RecordatorioUiState
    data class Success(val data: List<String>) : RecordatorioUiState
}
