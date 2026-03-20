package com.splitpay.app.data.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class MockCollection<T>(private val getId: (T) -> String) {
    private val store = MutableStateFlow<Map<String, T>>(emptyMap())

    fun getAll(): Flow<List<T>> = store.map { it.values.toList() }

    fun getById(id: String): Flow<T?> = store.map { it[id] }

    fun query(predicate: (T) -> Boolean): Flow<List<T>> =
        store.map { map -> map.values.filter(predicate) }

    fun put(item: T) {
        store.update { it + (getId(item) to item) }
    }

    fun update(id: String, transform: (T) -> T) {
        store.update { map ->
            val existing = map[id] ?: return@update map
            map + (id to transform(existing))
        }
    }

    fun delete(id: String) {
        store.update { it - id }
    }
}

object MockFirestore {
    inline fun <reified T> collection(name: String, noinline getId: (T) -> String): MockCollection<T> {
        @Suppress("UNCHECKED_CAST")
        return collections.getOrPut(name) {
            MockCollection(getId)
        } as MockCollection<T>
    }

    val collections = mutableMapOf<String, Any>()
}
