package io.github.athorfeo.template.ui.component

import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.athorfeo.template.R
import io.github.athorfeo.template.ui.theme.ApplicationTheme

@Composable
fun ErrorAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    @StringRes dialogTitle: Int,
    @StringRes dialogText: Int
) {
    AlertDialog(
        title = {
            Text(text = stringResource(dialogTitle))
        },
        text = {
            Text(text = stringResource(dialogText))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.dismiss))
            }
        }
    )
}

@Preview
@Composable
fun ErrorAlertDialogPreview() {
    ApplicationTheme {
        ErrorAlertDialog({}, {}, R.string.title_default_error_dialog, R.string.text_default_error_dialog)
    }
}
