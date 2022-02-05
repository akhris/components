package ui.nav_panel

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Button
import androidx.compose.material.NavigationRail
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SidePanel() {
    NavigationRail(modifier = Modifier.fillMaxHeight().defaultMinSize(minWidth = 200.dp)) {
        Button(onClick = {
            //navigate to...
        },
            content = { Text("Resistors") }
        )
        Button(onClick = {
            //navigate to...
        },
            content = { Text("Capacitors") }
        )
        Button(onClick = {
            //navigate to...
        },
            content = { Text("ICs") }
        )
    }
}