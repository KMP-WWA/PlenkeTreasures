package com.netmarble.bn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.netmarble.bn.ui.theme.Gold
import com.netmarble.bn.R

@Composable
fun RulesDialog(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .width(550.dp)
                .height(750.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_rules_dialog),
                contentDescription = stringResource(R.string.rules_background),
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = 150.dp,
                        start = 60.dp,
                        end = 50.dp,
                        bottom = 0.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.rules_text).trimIndent(),
                    color = Gold

                )

                Spacer(modifier = Modifier.height(24.dp))

                IconButton(onClick = onDismiss) {
                    Image(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = stringResource(R.string.close),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}