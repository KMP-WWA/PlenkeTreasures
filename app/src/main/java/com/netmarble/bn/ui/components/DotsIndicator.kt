package com.netmarble.bn.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DotsIndicator(
    total: Int,
    selectedIndex: Int,
    onDotClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(total) { i ->
            Surface(
                modifier = Modifier
                    .size(if (i == selectedIndex) 10.dp else 8.dp)
                    .clickable { onDotClick(i) },
                shape = MaterialTheme.shapes.small,
                color = if (i == selectedIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
            ) {}
        }
    }
}