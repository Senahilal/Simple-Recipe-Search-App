//MealViewModel.kt

package com.example.simplerecipesearchapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MealViewModel : ViewModel() {

    //from lecture example

    private val _mealState = MutableStateFlow<MealState>(MealState.Initial)
    val mealState: StateFlow<MealState> = _mealState

    fun searchMeals(query: String) {
        viewModelScope.launch {
            _mealState.value = MealState.Loading
            try {
                val response = ApiClient.apiService.searchMeals(query)
                _mealState.value = MealState.Success(response.meals)
            } catch (e: Exception) {
                _mealState.value = MealState.Error(e.message ?: "Unknown error")
            }
        }
    }

    sealed class MealState {
        object Initial : MealState()
        object Loading : MealState()
        data class Success(val meals: List<Meal>) : MealState()
        data class Error(val message: String) : MealState()
    }
}