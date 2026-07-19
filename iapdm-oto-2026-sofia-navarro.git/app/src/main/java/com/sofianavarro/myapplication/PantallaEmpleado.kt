package com.sofianavarro.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PantallaEmpleados(modifier: Modifier = Modifier) {
    var listaEmpleados by remember { mutableStateOf(listOf<Empleado>()) }

    var nombre by remember { mutableStateOf("") }
    var cargo by remember { mutableStateOf("") }
    var departamento by remember { mutableStateOf("") }
    var salario by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }

    var salarioError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }

    val fechaRegex = Regex("""^\d{2}/\d{2}/\d{4}$""")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Empleados",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cargo,
            onValueChange = { cargo = it },
            label = { Text("Cargo") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = departamento,
            onValueChange = { departamento = it },
            label = { Text("Departamento") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = salario,
            onValueChange = {
                salario = it
                salarioError = it.isNotBlank() && it.toDoubleOrNull() == null
            },
            label = { Text("Salario") },
            isError = salarioError,
            supportingText = { if (salarioError) Text("Debe ser un número válido") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
                fechaError = it.isNotBlank() && !it.matches(fechaRegex)
            },
            label = { Text("Fecha de contratación (DD/MM/AAAA)") },
            isError = fechaError,
            supportingText = { if (fechaError) Text("Use el formato DD/MM/AAAA") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val esSalarioValido = salario.isNotBlank() && salario.toDoubleOrNull() != null
                val esFechaValida = fecha.isNotBlank() && fecha.matches(fechaRegex)

                salarioError = !esSalarioValido
                fechaError = !esFechaValida

                if (nombre.isNotBlank() && cargo.isNotBlank() && departamento.isNotBlank() && esSalarioValido && esFechaValida) {
                    listaEmpleados = listaEmpleados + Empleado(
                        nombre = nombre,
                        cargo = cargo,
                        departamento = departamento,
                        salario = salario,
                        fechaContratacion = fecha
                    )
                    nombre = ""; cargo = ""; departamento = ""; salario = ""; fecha = ""
                    salarioError = false
                    fechaError = false
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Empleados")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Lista de Empleados",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(listaEmpleados, key = { it.id }) { empleado ->
                ItemEmpleado(
                    empleado = empleado,
                    onEliminar = { listaEmpleados = listaEmpleados.filter { it.id != empleado.id } }
                )
            }
        }
    }
}

@Composable
fun ItemEmpleado(empleado: Empleado, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Text(
                text = empleado.nombre,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item { SuggestionChip(onClick = {}, label = { Text("Cargo: ${empleado.cargo}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Dep: ${empleado.departamento}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Salario: ${empleado.salario}") }) }
                item { SuggestionChip(onClick = {}, label = { Text("Fecha: ${empleado.fechaContratacion}") }) }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onEliminar,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Eliminar")
            }
        }
    }
}