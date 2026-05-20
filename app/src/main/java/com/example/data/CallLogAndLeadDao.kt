package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogAndLeadDao {
    // Call Logs
    @Query("SELECT * FROM call_logs ORDER BY timestamp DESC")
    fun getAllCallLogs(): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLogs(callLogs: List<CallLogEntity>)

    @Delete
    suspend fun deleteCallLog(callLog: CallLogEntity)

    @Query("DELETE FROM call_logs WHERE id = :id")
    suspend fun deleteCallLogById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDismissedCall(dismissedCall: DismissedCallEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM dismissed_calls WHERE idString = :idString)")
    suspend fun isCallDismissed(idString: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM leads WHERE phoneNumber = :phoneNumber)")
    suspend fun isLeadExists(phoneNumber: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM call_logs WHERE phoneNumber = :phoneNumber AND timestamp = :timestamp)")
    suspend fun isCallLogExists(phoneNumber: String, timestamp: Long): Boolean

    // Leads
    @Query("SELECT * FROM leads ORDER BY createdAt DESC")
    fun getAllLeads(): Flow<List<LeadEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLead(lead: LeadEntity)

    @Update
    suspend fun updateLead(lead: LeadEntity)

    @Delete
    suspend fun deleteLead(lead: LeadEntity)

    @Query("DELETE FROM leads WHERE id = :id")
    suspend fun deleteLeadById(id: Int)
}
