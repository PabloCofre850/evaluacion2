package com.example.evaluacion2.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.ui.unit.sp

@Composable
fun PantallaMenu(
    onClientes: () -> Unit,
    onMedidores: () -> Unit,
    onLecturas: () -> Unit,
    onBoletas: () -> Unit

) {

    // Colores
    val CGEBlue  = Color(0xFF4A148C)
    val CGEWhite = Color.White
    val CGEBlueOscuro = Color(0xFF1A237E)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CGEWhite)
            .border(10.dp, CGEBlue, RoundedCornerShape(0.dp))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // -- Columna Bienvenida (2/3) --
            Column(
                modifier = Modifier
                    .weight(2f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            )
            {
                Text(
                    text = "CGE",
                    fontSize = 64.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001689),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
                Text(
                    text = "¡¡Bienvenido!!",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF001689),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.width(40.dp))

            // -- Columna Panel de gestion (1/3 del tamaño de la pantalla) --
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(CGEBlue, RoundedCornerShape(10.dp))
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    "Panel de gestion",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = CGEWhite,
                        fontSize = 32.sp
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Colores para el OutlinedButton
                val outlineColors = ButtonDefaults.outlinedButtonColors(
                    containerColor = CGEWhite,     // fondo blanco
                    contentColor = CGEBlueOscuro        // texto azul
                )

                // Borde azul
                val outlineBorder = BorderStroke(2.dp, CGEBlue)

                listOf(

                    "Gestionar Clientes" to onClientes,

                    "Gestionar Medidores" to onMedidores,

                    "Gestionar Lecturas" to onLecturas,

                    "Gestionar Boletas" to onBoletas

                ).forEach { (label, action) ->
                    OutlinedButton(
                        onClick = action,
                        colors = outlineColors,
                        border = outlineBorder,
                        shape = RoundedCornerShape(38.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)

                    ) {
                        Text(label, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

