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

package es.rodal.keoapp.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import es.rodal.keoapp.data.local.database.Recordatorio
import es.rodal.keoapp.data.local.database.RecordatorioDao

/**
 * Unit tests for [DefaultRecordatorioRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultRecordatorioRepositoryTest {

    @org.junit.jupiter.api.Test
    fun recordatorios_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultRecordatorioRepository(FakeRecordatorioDao())

        repository.add("Repository")

        assertEquals(repository.recordatorios.first().size, 1)
    }

}

private class FakeRecordatorioDao : RecordatorioDao {

    private val data = mutableListOf<Recordatorio>()

    override fun getRecordatorios(): Flow<List<Recordatorio>> = flow {
        emit(data)
    }

    override suspend fun insertRecordatorio(item: Recordatorio) {
        data.add(0, item)
    }
}
