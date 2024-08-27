package es.rodal.keoapp.ui.screens

import android.content.Context
import es.rodal.keoapp.data.domain.model.Recordatorio
import es.rodal.keoapp.data.local.di.FakeRecordatorioRepository
import es.rodal.keoapp.ui.screens.history.HistoryUiState
import es.rodal.keoapp.ui.screens.history.RecordatorioHistoryViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.Date


class RecordatorioHistoryViewModelTest {

    private val testDispatcher = TestCoroutineDispatcher()

    lateinit var viewModel: RecordatorioHistoryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = RecordatorioHistoryViewModel(FakeRecordatorioRepository())
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }


    @Test
    fun uiState_initiallyEmpty() = runTest {
        Assert.assertTrue(viewModel.recordatorioState.isEmpty())
    }

    @Test
    fun uiState_onItemSaved_isDisplayed() = runTest {
        viewModel.loadRecordatorios()
        assertEquals(viewModel.recordatorioState.first(), HistoryUiState.Loading)
    }
    @Test
    fun loadRecordatorios_populatesRecordatorioState() = runTest {
        val viewModel = RecordatorioHistoryViewModel(FakeRecordatorioRepository().apply {
            insertRecordatorio(Recordatorio(1, "Test", "", Date() ))
        })
        viewModel.loadRecordatorios()
        assertEquals(1, viewModel.recordatorioState.size)
    }

    @Test
    fun deleteRecordatorio_removesRecordatorioFromState() = runTest {
        val repository = FakeRecordatorioRepository().apply {
            insertRecordatorio(Recordatorio(1, "Test", "", Date() ))
        }
        val viewModel = RecordatorioHistoryViewModel(repository)
        viewModel.deleteRecordatorio(mock(Context::class.java), Recordatorio(1, "Test", "", Date() ))
        assertTrue(viewModel.recordatorioState.isEmpty())
    }

    @Test
    fun reverseActive_togglesRecordatorioActiveState() = runTest {
        val repository = FakeRecordatorioRepository().apply {
            insertRecordatorio(Recordatorio(1, "Test", "", Date() ))
        }
        val viewModel = RecordatorioHistoryViewModel(repository)
        viewModel.reverseActive(mock(Context::class.java), Recordatorio(1, "Test", "", Date() ))
        assertFalse(viewModel.recordatorioState.first().active)
    }

    @Test
    fun loadRecordatorios_handlesEmptyList() = runTest {
        val viewModel = RecordatorioHistoryViewModel(FakeRecordatorioRepository())
        viewModel.loadRecordatorios()
        assertTrue(viewModel.recordatorioState.isEmpty())
    }

    @Test
    fun deleteRecordatorio_handlesNonExistentRecordatorio() = runTest {
        val repository = FakeRecordatorioRepository()
        val viewModel = RecordatorioHistoryViewModel(repository)
        viewModel.deleteRecordatorio(mock(Context::class.java), Recordatorio(1, "Test", "", Date()))
        assertTrue(viewModel.recordatorioState.isEmpty())
    }

}