package app.compose_fruits.presentation

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.compose_fruits.domain.model.Fruit
import app.compose_fruits.presentation.viewmodel.FruitsViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FruitsScreen(viewModel: FruitsViewModel = androidx.hilt.navigation.compose.hiltViewModel()) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    var query by remember { mutableStateOf("") }
    var selected by remember { mutableStateOf<Fruit?>(null) }
    var sheetVisible by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🍉 Fruits Nutrition", fontWeight = FontWeight.ExtraBold)
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF2E7D32))
            )
        },
        containerColor = Color(0xFFF1F8E9)
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("Search fruit, e.g. Apple") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                    return@Column
                }

                state.error?.let { err ->
                    Text("❌ $err", color = Color.Red, modifier = Modifier.padding(8.dp))
                }

                val list = if (query.isBlank()) state.fruits else state.fruits.filter { it.name.contains(query, true) }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 140.dp),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(list) { fruit ->
                        FruitCard(
                            fruit = fruit,
                            onClick = {
                                selected = fruit
                                sheetVisible = true
                            },
                            onLongPress = {
                                val txt = nutritionSummary(fruit)
                                clipboard.setText(AnnotatedString(txt))
                                Toast.makeText(context, "Nutrition copied", Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }

            if (sheetVisible && selected != null) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .heightIn(min = 220.dp, max = 420.dp)
                        .shadow(8.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                    tonalElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = imageForFruit(selected!!.name),
                                contentDescription = selected!!.name,
                                modifier = Modifier.size(84.dp).clip(RoundedCornerShape(12.dp))
                            )
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(selected!!.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                                Text(selected!!.family ?: "", color = Color.Gray)
                            }
                            Spacer(Modifier.weight(1f))
                            IconButton(onClick = { sheetVisible = false }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "close")
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                            NutritionItem("Calories", selected!!.nutritions.calories?.toString() ?: "—")
                            NutritionItem("Carbs", selected!!.nutritions.carbohydrates?.roundTo(1) ?: "—")
                            NutritionItem("Protein", selected!!.nutritions.protein?.roundTo(1) ?: "—")
                            NutritionItem("Fat", selected!!.nutritions.fat?.roundTo(1) ?: "—")
                            NutritionItem("Sugar", selected!!.nutritions.sugar?.roundTo(1) ?: "—")
                        }

                        Spacer(Modifier.height(12.dp))

                        Row {
                            Button(onClick = {
                                val txt = nutritionSummary(selected!!)
                                clipboard.setText(AnnotatedString(txt))
                                Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
                            }) {
                                Text("📋 Copy Nutrition")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = {
                                val intent = Intent(Intent.ACTION_SEND)
                                intent.type = "text/plain"
                                intent.putExtra(Intent.EXTRA_TEXT, nutritionSummary(selected!!))
                                context.startActivity(Intent.createChooser(intent, "Share via"))
                            }) {
                                Text( "🔗 Share")
                            }
                        }
                    }
                }
            }
        }
    }
}