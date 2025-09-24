package com.netmarble.bn.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.netmarble.bn.R
import com.netmarble.bn.ui.theme.Marcellus
import com.netmarble.bn.ui.theme.Gold

@Composable
fun BetControl(
    bet: Int,
    onDec: () -> Unit,
    onInc: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.7f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(R.drawable.ic_down),
                contentDescription = stringResource(R.string.content_desc_decrease),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onDec() }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                text = stringResource(R.string.bet_text, bet),
                fontFamily = Marcellus,
                color = Gold,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(Modifier.width(12.dp))

            Image(
                painter = painterResource(R.drawable.ic_up),
                contentDescription = stringResource(R.string.content_desc_increase),
                modifier = Modifier
                    .size(20.dp)
                    .clickable { onInc() }
            )
        }
    }
}