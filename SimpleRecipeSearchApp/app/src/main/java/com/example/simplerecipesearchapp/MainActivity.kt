package com.example.simplerecipesearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.simplerecipesearchapp.ui.theme.SimpleRecipeSearchAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleRecipeSearchAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFFFF8E1) // a soft light yellow
                ) {
                    RecipeSearchScreen()
                }
            }
        }
    }
}

@Composable
fun RecipeSearchScreen(mealViewModel: MealViewModel = viewModel()) {
    var query by remember { mutableStateOf("") }

    //state from the ViewModel
    val mealState by mealViewModel.mealState.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(20.dp))

        // Search Bar
        //help from ai
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Search Recipe") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                cursorColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Search Button
        Button(
            onClick = { mealViewModel.searchMeals(query) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // based on state change what to show
        // credit to lecture examples
        when (mealState) {
            is MealViewModel.MealState.Initial -> {}
            is MealViewModel.MealState.Loading -> {
                CircularProgressIndicator()
            }
            is MealViewModel.MealState.Success -> {
                val meals = (mealState as MealViewModel.MealState.Success).meals
                LazyColumn {
                    items(meals) { meal ->
                        MealItem(meal = meal)
                    }
                }
            }
            is MealViewModel.MealState.Error -> {
                val error = (mealState as MealViewModel.MealState.Error).message
                Text("Error: $error", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun MealItem(meal: Meal) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xfff4d58d), shape = MaterialTheme.shapes.medium) // background color

            .border(
                width = 1.dp,
                color = Color(0xfffff3b0),
                shape = MaterialTheme.shapes.medium
            )
            .padding(25.dp)
    ) {
        Text(text = meal.strMeal, style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(4.dp))

        //to asynchronously load and display images from a URL
        //got help from ai
        AsyncImage(
            model = meal.strMealThumb,
            contentDescription = "Meal Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = meal.strInstructions.take(200) + "...", // Truncate long instructions
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimpleRecipeSearchAppTheme {
    }
}