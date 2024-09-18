package com.cvelez.dittodemo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cvelez.dittodemo.data.remote.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import live.ditto.Ditto
import live.ditto.DittoCollection
import live.ditto.DittoLiveQuery
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val ditto: Ditto // Inyectar Ditto directamente
) : ViewModel() {
    private val tasksCollection: DittoCollection = ditto.store.collection("tasks")

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private var liveQuery: DittoLiveQuery? = null

    init {
        liveQuery = tasksCollection.find("isDeleted == false").observeLocal { documents, _ ->
            _tasks.value = documents.map { doc ->
                Task(
                    id = doc.id.toString(),
                    name = doc["name"].stringValue,
                    completed = doc["completed"].booleanValue
                )
            }
        }

        ditto.presence.observe { syncStatus ->
            Log.d("SyncStatus", syncStatus.toString())
        }
    }

    override fun onCleared() {
        super.onCleared()
        liveQuery?.stop()
    }

    fun addTask(name: String) {
        viewModelScope.launch {
            tasksCollection.upsert(mapOf("name" to name, "completed" to false, "isDeleted" to false))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            tasksCollection.findById(task.id).remove()
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            tasksCollection.findAll().remove()
        }
    }

    fun evictDeletedTasks() {
        viewModelScope.launch {
            ditto.store.execute("EVICT FROM tasks WHERE isDeleted == true")
        }
    }
}
