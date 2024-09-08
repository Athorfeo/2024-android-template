package io.github.athorfeo.template.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(8.dp)),
    ) {
        OutlinedTextField(
            modifier = Modifier.weight(1f),
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent
            )
        )
        FilledTonalButton(
            modifier = Modifier.height(55.dp).padding(0.dp, 0.dp, 0.dp, 0.dp),
            onClick = onSearch,
            enabled = query.isNotEmpty(),
            shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp),
            border = BorderStroke(0.dp, Color.Transparent),
            contentPadding = PaddingValues(8.dp, 0.dp, 8.dp, 0.dp)
        ) {
            Icon(imageVector = Icons.Filled.Search, contentDescription = "")
        }
    }
}
