package com.cvelez.dittodemo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cvelez.dittodemo.data.Task
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.imePadding

@Composable
fun TaskScreen(viewModel: TaskViewModel = viewModel(), modifier: Modifier = Modifier) {
    var taskName by remember { mutableStateOf("") }
    val tasks by viewModel.tasks.collectAsState()

    // Controlador del teclado para ocultarlo al agregar una tarea
    val keyboardController = LocalSoftwareKeyboardController.current

    ProvideWindowInsets { // Esto garantiza que se gestione el espacio para el teclado
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding() // Agrega relleno automático para el teclado
        ) {
            // Título de la pantalla
            Text(
                text = "Lista de Tareas",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Mostrar la lista de tareas
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(tasks) { task ->
                    TaskItem(task = task, onDeleteTask = { taskToDelete ->
                        // Llamar a la función para eliminar la tarea
                        viewModel.deleteTask(taskToDelete)
                    })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para añadir nueva tarea
            TextField(
                value = taskName,
                onValueChange = { taskName = it },
                label = { Text("Nueva Tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botones para agregar y eliminar tareas
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón para agregar tarea
                Button(
                    onClick = {
                        viewModel.addTask(taskName)
                        taskName = "" // Limpiar el campo
                        keyboardController?.hide() // Ocultar el teclado al agregar una tarea
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Agregar Tarea")
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Botón para eliminar todas las tareas
                Button(
                    onClick = {
                        viewModel.deleteAllTasks()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Eliminar Todas las Tareas")
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onDeleteTask: (Task) -> Unit, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = task.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 18.sp
                )
            }

            IconButton(onClick = { onDeleteTask(task) }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    TaskScreen()
}