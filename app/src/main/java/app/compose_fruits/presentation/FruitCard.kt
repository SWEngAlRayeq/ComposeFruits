package app.compose_fruits.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.compose_fruits.domain.model.Fruit
import coil.compose.AsyncImage
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun FruitCard(fruit: Fruit, onClick: () -> Unit, onLongPress: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() }
            .pointerInput(Unit) {
                detectTapGestures(onLongPress = { onLongPress() })
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.linearGradient(listOf(Color(0xFFFFF3E0), Color(0xFFFFFDE7)))
                    )
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = imageForFruit(fruit.name),
                        contentDescription = fruit.name,
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(10.dp))
                    )
                    Spacer(Modifier.width(12.dp))

                }
                Text(fruit.name, fontWeight = FontWeight.Bold)
                Text(fruit.genus ?: "", color = Color.Gray)

                Text(
                    "Calories: ${fruit.nutritions.calories ?: "—"} kcal",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.weight(1f))
            }
        }
    }
}


@Composable
fun RowScope.NutritionItem(label: String, value: String) {
    Column(
        modifier = Modifier.weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, fontWeight = FontWeight.SemiBold)
        Text(value, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
    }
}

fun imageForFruit(name: String): String {
    val query = name.replace(" ", "+")
    return "https://source.unsplash.com/featured/?fruit,$query"
}

fun nutritionSummary(f: Fruit): String {
    return buildString {
        append("${f.name} — Nutrition\n")
        append("Calories: ${f.nutritions.calories ?: "—"} kcal\n")
        append("Carbs: ${f.nutritions.carbohydrates ?: "—"} g\n")
        append("Protein: ${f.nutritions.protein ?: "—"} g\n")
        append("Fat: ${f.nutritions.fat ?: "—"} g\n")
        append("Sugar: ${f.nutritions.sugar ?: "—"} g\n")
    }
}

fun Double.roundTo(decimals: Int): String {
    val factor = 10.0.pow(decimals)
    return ((this * factor).roundToInt() / factor).toString()
}