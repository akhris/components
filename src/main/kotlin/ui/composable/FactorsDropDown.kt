package ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.*
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import domain.valueobjects.Factor
import domain.valueobjects.factors
import domain.valueobjects.scientificString

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FactorsDropDown(modifier: Modifier = Modifier, factor: Factor, onFactorSelected: (Factor) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxHeight().fillMaxWidth().clickable { expanded = true },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = factor.prefix, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Rounded.ArrowDropDown,
                contentDescription = "show list of factors"
            )
        }
        DropdownMenu(expanded = expanded, content = {
            factors.forEach { f ->
                val fontWeight = remember(f, factor) { if (f == factor) FontWeight.Bold else null }
                val background =
                    if (f == factor) MaterialTheme.colors.primarySurface else MaterialTheme.colors.background
                DropdownMenuItem(content = {
                    Surface(color = background) {
                        ListItem(
                            text = {
                                Text(
                                    "${f.prefix}: ${f.name}",
                                    fontWeight = fontWeight
                                )
                            },
                            secondaryText = { Text(f.scientificString, fontWeight = fontWeight) }
                        )
                    }

                }, onClick = {
                    onFactorSelected(f)
                    expanded = false
                })
            }
        }, onDismissRequest = {
            expanded = false
        })
    }
}

