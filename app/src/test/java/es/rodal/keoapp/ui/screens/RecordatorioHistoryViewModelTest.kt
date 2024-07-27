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

package es.rodal.keoapp.ui.screens


import es.rodal.keoapp.data.domain.model.Recordatorio
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import es.rodal.keoapp.data.domain.repository.RecordatorioRepository
import es.rodal.keoapp.ui.screens.history.RecordatorioUiState
import es.rodal.keoapp.ui.screens.history.RecordatorioHistoryViewModel
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class RecordatorioHistoryViewModelTest {
    @Test
    fun uiState_initiallyLoading() = runTest {
        val viewModel = RecordatorioHistoryViewModel(FakeRecordatorioRepository())
        assertEquals(viewModel.recordatorioState.first(), RecordatorioUiState.Loading)
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        val viewModel = RecordatorioHistoryViewModel(FakeRecordatorioRepository())
        assertEquals(viewModel.recordatorioState.first(), RecordatorioUiState.Loading)
    }
}

private class FakeRecordatorioRepository : RecordatorioRepository {

    private val data = mutableListOf<String>()

    override suspend fun getRecordatorios(): Flow<List<Recordatorio>> {
        TODO("Not yet implemented")
    }

    override fun getRecordatorioById(id: Long): Flow<Recordatorio> {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecordatorio(item: Recordatorio): Long {
        TODO("Not yet implemented")
    }

    override suspend fun updateRecordatorio(item: Recordatorio) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecordatorio(item: Recordatorio) {
        TODO("Not yet implemented")
    }
}
