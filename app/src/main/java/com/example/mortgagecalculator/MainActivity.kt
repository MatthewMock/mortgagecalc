package com.example.mortgagecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mortgagecalculator.ui.theme.MortgageCalculatorTheme
import java.text.NumberFormat
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MortgageCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MortgageCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorLayout() {
    var pInput by remember { mutableStateOf("") }
    var rInput by remember { mutableStateOf("") }
    var nInput by remember { mutableStateOf("") }

    val p = pInput.toInt()
    val r = rInput.toDoubleOrNull()  ?: 0.0 /100 /12
    val n = nInput.toInt() *12

    val mortgage = calculateMortgage(p, r, n)

    Column(
        modifier = Modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        EditNumberField(
            label = R.string.loan_amount,
            leadingIcon = R.drawable.money,
            value = pInput,
            onValueChange = { pInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth())
        EditNumberField(
            label = R.string.interest_rate,
            leadingIcon = R.drawable.percent,
            value = rInput,
            onValueChange = { rInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth())
        EditNumberField(
            label = R.string.number_of_years,
            leadingIcon = R.drawable.calendar,
            value = nInput,
            onValueChange = { nInput = it },
            modifier = Modifier.padding(bottom = 32.dp).fillMaxWidth())
        Text(
            text = stringResource(R.string.monthly_payment, mortgage),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null)},
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier
    )
}

private fun calculateMortgage(p: Int, r: Double, n: Int): String {
    var mortgage = p * ( (r*(1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1) )
    if (mortgage.isNaN() || mortgage.isInfinite()) mortgage = 0.0
    return NumberFormat.getCurrencyInstance().format(mortgage)
}

@Preview(showBackground = true)
@Composable
fun MortgageCalculatorLayoutPreview() {
    MortgageCalculatorTheme {
        MortgageCalculatorLayout()
    }
}