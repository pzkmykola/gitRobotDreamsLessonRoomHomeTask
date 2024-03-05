package com.example.roomongit
import com.example.roomongit.dbnew.TodoFB
import com.example.roomongit.dbnew.TodoRepository
import com.example.roomongit.util.FireStoreCollection

import com.example.roomongit.util.UiState
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepositoryImplementation @Inject constructor (private val database: FirebaseFirestore) : TodoRepository {
    override fun addTodo(todo: TodoFB, result: (UiState<Pair<TodoFB, String>>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .add(todo)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(todo, "Task added successfully!")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override fun updateTodo(todo: TodoFB, result: (UiState<Pair<TodoFB, String>>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .document(todo.id)
            .set(todo)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(todo, "Task updated successfully!")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override fun deleteTodo(todo: TodoFB, result: (UiState<Pair<TodoFB, String>>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .document(todo.id)
            .set(todo)
            .addOnSuccessListener {
                result.invoke(UiState.Success(Pair(todo, "Task deleted successfully!")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override fun getTodo(id: String, result: (UiState<Pair<TodoFB, String>>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .document(id)
            .get()
            .addOnSuccessListener {
                val task = it.toObject(TodoFB::class.java)
                if (task != null) {
                    result.invoke(UiState.Success(Pair(task, "Task got successfully!")))
                } else {
                    result.invoke(UiState.Failure("Task not found!"))
                }
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override fun getTodos(result: (UiState<List<TodoFB>>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .get()
            .addOnSuccessListener {
                val todos = it.toObjects(TodoFB::class.java)
                result.invoke(UiState.Success(todos.filterNotNull()))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }

    override fun storeTodos(todos: List<TodoFB>, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreCollection.TODOS)
            .document("")
            .collection(FireStoreCollection.TODOS)
            .get()
            .addOnSuccessListener {
                val todosInDatabase = it.toObjects(TodoFB::class.java)
                val todosToAdd = todos.filter { todo ->
                    todosInDatabase.find { it.id == todo.id } == null
                }
                val todosToUpdate = todos.filter { todo ->
                    todosInDatabase.find { it.id == todo.id } != null
                }
                val todosToDelete = todosInDatabase.filter { todo ->
                    todosInDatabase.find{ it.id == todo.id } == null
                }
                todosToAdd.forEach { todo ->
                    database.collection(FireStoreCollection.TODOS)
                        .document( "")
                        .collection(FireStoreCollection.TODOS)
                        .add(todo)
                }
                todosToUpdate.forEach { todo ->
                    database.collection(FireStoreCollection.TODOS)
                        .document("")
                        .collection(FireStoreCollection.TODOS)
                        .document(todo.id)
                        .set(todo)
                }
                todosToDelete.forEach { todo ->
                    database.collection(FireStoreCollection.TODOS)
                        .document("")
                        .collection(FireStoreCollection.TODOS)
                        .document(todo.id)
                        .delete()
                }
                result.invoke(UiState.Success("Tasks stored successfully!"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.message))
            }
    }
}




