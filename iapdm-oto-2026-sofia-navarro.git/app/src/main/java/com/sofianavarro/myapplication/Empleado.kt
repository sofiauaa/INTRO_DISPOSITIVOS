package com.sofianavarro.myapplication

data class Empleado(
    val id: String = java.util.UUID.randomUUID().toString(),
    val nombre: String,
    val cargo: String,
    val departamento: String,
    val salario: String,
    val fechaContratacion: String
)