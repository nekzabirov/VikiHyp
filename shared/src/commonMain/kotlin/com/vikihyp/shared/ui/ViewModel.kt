package com.vikihyp.shared.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.*
import org.koin.core.component.KoinComponent
import kotlin.coroutines.CoroutineContext

@Composable
internal fun <
        STATE : ViewState,
        EVENT : ViewEvent,
        T : ViewModel<STATE, EVENT>> viewModel(factor: () -> T): T {
    val viewModel = remember(factor)

    DisposableEffect(viewModel) {
        onDispose { viewModel.onClear() }
    }

    return viewModel
}

internal abstract class ViewModel<
        STATE : ViewState,
        EVENT : ViewEvent> : CoroutineScope, KoinComponent {
    private val job = Job()

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        println(throwable)
        processError(throwable)
    }

    override val coroutineContext: CoroutineContext = Dispatchers.Default + errorHandler + job

    private val _state by lazy { MutableStateFlow(internalState()) }
    val state: StateFlow<STATE>
        get() = _state

    private val _event = Channel<EVENT>(Channel.BUFFERED)
    val event: Flow<EVENT>
        get() = _event.receiveAsFlow()

    init {
        launch {
            _event.consumeEach {
                processEvent(it)
            }
        }
    }

    protected abstract fun internalState(): STATE

    protected abstract fun processEvent(event: EVENT)

    protected abstract fun processError(error: Throwable)

    protected fun setState(state: STATE) {
        _state.value = state
    }

    fun sendEvent(event: EVENT) {
        _event.trySend(event)
    }

    open fun onClear() {
        job.cancelChildren()
    }
}