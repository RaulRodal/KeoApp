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

package es.rodal.keoapp.data.domain.repository

import es.rodal.keoapp.data.domain.model.Recordatorio
import kotlinx.coroutines.flow.Flow

interface RecordatorioRepository {

    suspend fun getRecordatorios(): Flow<List<Recordatorio>>
    fun getRecordatorioById(id: Long): Flow<Recordatorio>
    suspend fun insertRecordatorio(item: Recordatorio): Long
    suspend fun updateRecordatorio(item: Recordatorio)
    suspend fun deleteRecordatorio(item: Recordatorio)
}
