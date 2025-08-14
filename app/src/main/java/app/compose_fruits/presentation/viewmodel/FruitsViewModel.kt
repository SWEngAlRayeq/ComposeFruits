package app.compose_fruits.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.compose_fruits.domain.model.Fruit
import app.compose_fruits.domain.usecase.GetAllFruitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FruitsUiState(
    val fruits: List<Fruit> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class FruitsViewModel @Inject constructor(
    private val getAllFruitsUseCase: GetAllFruitsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FruitsUiState(isLoading = true))
    val uiState: StateFlow<FruitsUiState> = _uiState

    init {
        loadFruits()
    }

    fun loadFruits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val list = getAllFruitsUseCase()
                _uiState.value = _uiState.value.copy(fruits = list, isLoading = false)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(
                    error = t.localizedMessage ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }


}