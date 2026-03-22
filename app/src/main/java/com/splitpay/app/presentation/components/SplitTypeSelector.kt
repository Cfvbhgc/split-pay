package com.splitpay.app.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.splitpay.app.domain.model.SplitType

@Composable
fun SplitTypeSelector(
    selectedType: SplitType,
    onTypeSelected: (SplitType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        FilterChip(
            selected = selectedType == SplitType.EQUAL,
            onClick = { onTypeSelected(SplitType.EQUAL) },
            label = { Text("Equal Split") },
            modifier = Modifier.padding(end = 8.dp)
        )
        FilterChip(
            selected = selectedType == SplitType.CUSTOM,
            onClick = { onTypeSelected(SplitType.CUSTOM) },
            label = { Text("Custom Split") }
        )
    }
}
