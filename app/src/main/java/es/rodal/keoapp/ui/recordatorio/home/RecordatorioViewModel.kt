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

package es.rodal.keoapp.ui.recordatorio.home

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
class RecordatorioViewModel @Inject constructor(
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

sealed interface RecordatorioUiState {
    object Loading : RecordatorioUiState
    data class Error(val throwable: Throwable) : RecordatorioUiState
    data class Success(val data: List<String>) : RecordatorioUiState
}
