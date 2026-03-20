package com.splitpay.app.data.repository

import com.splitpay.app.data.local.MockCollection
import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow

class GroupRepositoryImpl(
    private val collection: MockCollection<Group>
) : GroupRepository {

    override fun getAllGroups(): Flow<List<Group>> = collection.getAll()

    override fun getGroupById(groupId: String): Flow<Group?> = collection.getById(groupId)

    override suspend fun createGroup(group: Group) {
        collection.put(group)
    }

    override suspend fun updateGroup(group: Group) {
        collection.put(group)
    }

    override suspend fun deleteGroup(groupId: String) {
        collection.delete(groupId)
    }
}
