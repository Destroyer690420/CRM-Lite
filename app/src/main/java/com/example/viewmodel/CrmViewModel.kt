package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.CallLogEntity
import com.example.data.CrmRepository
import com.example.data.LeadEntity
import com.example.data.populateInitialData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CrmViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CrmRepository

    val callLogs: StateFlow<List<CallLogEntity>>
    val leads: StateFlow<List<LeadEntity>>

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = CrmRepository(database.dao())
        
        callLogs = repository.allCallLogs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        leads = repository.allLeads.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun promoteToLead(callLog: CallLogEntity) {
        viewModelScope.launch {
            repository.promoteCallLogToLead(callLog)
        }
    }

    fun rejectCallLog(callLog: CallLogEntity) {
        viewModelScope.launch {
            repository.dismissCallLog(callLog)
        }
    }

    fun syncCallLogs(context: android.content.Context) {
        viewModelScope.launch {
            repository.syncDeviceCallLogs(context)
        }
    }

    fun addManualLead(name: String, phoneNumber: String, notes: String) {
        viewModelScope.launch {
            repository.insertLead(
                LeadEntity(
                    name = name,
                    phoneNumber = phoneNumber,
                    notes = notes
                )
            )
        }
    }

    fun updateLeadNotes(lead: LeadEntity, newNotes: String) {
        viewModelScope.launch {
            repository.updateLead(lead.copy(notes = newNotes))
        }
    }

    fun deleteLead(lead: LeadEntity) {
        viewModelScope.launch {
            repository.deleteLead(lead)
        }
    }

    fun generateSampleCallLogs() {
        viewModelScope.launch {
            populateInitialData(AppDatabase.getDatabase(getApplication(), viewModelScope).dao())
        }
    }
}
