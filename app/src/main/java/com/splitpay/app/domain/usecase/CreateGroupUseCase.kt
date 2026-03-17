package com.splitpay.app.domain.usecase

import com.splitpay.app.domain.model.Group
import com.splitpay.app.domain.repository.GroupRepository

class CreateGroupUseCase(private val groupRepository: GroupRepository) {

    suspend operator fun invoke(group: Group) {
        require(group.name.isNotBlank()) { "Group name cannot be empty" }
        require(group.members.isNotEmpty()) { "Group must have at least one member" }
        groupRepository.createGroup(group)
    }
}
