package com.example.settleupnow.model


data class Group(

    val groupId: String = "",

    val groupName: String = "",

    val description: String = "",

    val createdBy: String = "",

    val members: Map<String, Boolean> = emptyMap()

)