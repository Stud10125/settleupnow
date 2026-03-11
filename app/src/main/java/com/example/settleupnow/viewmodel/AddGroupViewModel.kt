package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AddGroupViewModel : ViewModel() {

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    private val _groupDescription = MutableStateFlow("")
    val groupDescription: StateFlow<String> = _groupDescription

    private val _groupMembers = MutableStateFlow("")
    val groupMembers: StateFlow<String> = _groupMembers


    fun onGroupNameChanged(name: String) {
        _groupName.value = name
    }

    fun onGroupDescriptionChanged(desc: String) {
        _groupDescription.value = desc
    }

    fun onGroupMembers(member: String) {
        _groupMembers.value = member
    }


    fun createGroup(onSuccess: () -> Unit) {
        onSuccess()
    }

    fun cancel(onCancel: () -> Unit) {
        onCancel()
    }

}