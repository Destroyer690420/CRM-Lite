package com.example.data

import kotlinx.coroutines.flow.Flow

class CrmRepository(private val dao: CallLogAndLeadDao) {
    val allCallLogs: Flow<List<CallLogEntity>> = dao.getAllCallLogs()
    val allLeads: Flow<List<LeadEntity>> = dao.getAllLeads()

    private fun normalizePhone(phone: String): String {
        return phone.replace(Regex("[^0-9+]"), "").trim()
    }

    suspend fun insertCallLog(callLog: CallLogEntity) {
        dao.insertCallLog(callLog.copy(phoneNumber = normalizePhone(callLog.phoneNumber)))
    }

    suspend fun insertCallLogs(callLogs: List<CallLogEntity>) {
        dao.insertCallLogs(callLogs.map { it.copy(phoneNumber = normalizePhone(it.phoneNumber)) })
    }

    suspend fun deleteCallLog(callLog: CallLogEntity) {
        dao.deleteCallLog(callLog)
    }

    suspend fun deleteCallLogById(id: Int) {
        dao.deleteCallLogById(id)
    }

    suspend fun insertLead(lead: LeadEntity) {
        dao.insertLead(lead.copy(phoneNumber = normalizePhone(lead.phoneNumber)))
    }

    suspend fun updateLead(lead: LeadEntity) {
        dao.updateLead(lead.copy(phoneNumber = normalizePhone(lead.phoneNumber)))
    }

    suspend fun deleteLead(lead: LeadEntity) {
        dao.deleteLead(lead)
    }

    suspend fun deleteLeadById(id: Int) {
        dao.deleteLeadById(id)
    }

    suspend fun dismissCallLog(callLog: CallLogEntity) {
        val uniqueString = "${normalizePhone(callLog.phoneNumber)}_${callLog.timestamp}"
        dao.insertDismissedCall(DismissedCallEntity(uniqueString, normalizePhone(callLog.phoneNumber), callLog.timestamp))
        dao.deleteCallLog(callLog)
    }

    suspend fun promoteCallLogToLead(callLog: CallLogEntity) {
        // Create new lead using call details
        val lead = LeadEntity(
            name = callLog.contactName,
            phoneNumber = normalizePhone(callLog.phoneNumber),
            notes = ""
        )
        dao.insertLead(lead)
        // Remove from call logs (triage resolved)
        dao.deleteCallLog(callLog)
    }

    suspend fun syncDeviceCallLogs(context: android.content.Context) {
        if (androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CALL_LOG
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val contentResolver = context.contentResolver
        val uri = android.provider.CallLog.Calls.CONTENT_URI
        
        val projectionWithContacts = arrayOf(
            android.provider.CallLog.Calls._ID,
            android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.DATE,
            android.provider.CallLog.Calls.DURATION,
            android.provider.CallLog.Calls.TYPE,
            android.provider.CallLog.Calls.CACHED_NAME
        )

        val projectionNoContacts = arrayOf(
            android.provider.CallLog.Calls._ID,
            android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.DATE,
            android.provider.CallLog.Calls.DURATION,
            android.provider.CallLog.Calls.TYPE
        )

        var cursor: android.database.Cursor? = null
        var isCursorWithContact = true

        try {
            cursor = contentResolver.query(
                uri,
                projectionWithContacts,
                null,
                null,
                "${android.provider.CallLog.Calls.DATE} DESC"
            )
        } catch (e: Exception) {
            android.util.Log.e("CrmRepository", "Query with contacts column failed, trying fallback without contacts", e)
            try {
                cursor = contentResolver.query(
                    uri,
                    projectionNoContacts,
                    null,
                    null,
                    "${android.provider.CallLog.Calls.DATE} DESC"
                )
                isCursorWithContact = false
            } catch (ex: Exception) {
                android.util.Log.e("CrmRepository", "Fallback query failed completely", ex)
            }
        }

        var count = 0
        cursor?.use { c ->
            val numberIndex = c.getColumnIndex(android.provider.CallLog.Calls.NUMBER)
            val dateIndex = c.getColumnIndex(android.provider.CallLog.Calls.DATE)
            val durationIndex = c.getColumnIndex(android.provider.CallLog.Calls.DURATION)
            val typeIndex = c.getColumnIndex(android.provider.CallLog.Calls.TYPE)
            val nameIndex = if (isCursorWithContact) c.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME) else -1

            while (c.moveToNext() && count < 50) {
                count++
                val rawNumber = if (numberIndex >= 0) c.getString(numberIndex) ?: "" else ""
                val phoneNumber = rawNumber.trim()
                if (phoneNumber.isEmpty()) continue

                val normalizedPhone = normalizePhone(phoneNumber)
                val timestamp = if (dateIndex >= 0) c.getLong(dateIndex) else 0L
                val durationSec = if (durationIndex >= 0) c.getLong(durationIndex) else 0L
                val rawType = if (typeIndex >= 0) c.getInt(typeIndex) else 0
                val contactName = if (nameIndex >= 0) c.getString(nameIndex) ?: "Unknown" else "Unknown"

                val minutes = durationSec / 60
                val seconds = durationSec % 60
                val durationStr = if (minutes > 0) "${minutes}m ${seconds}s" else "${seconds}s"

                val callTypeStr = when (rawType) {
                    android.provider.CallLog.Calls.INCOMING_TYPE -> "Incoming"
                    android.provider.CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                    android.provider.CallLog.Calls.MISSED_TYPE -> "Missed"
                    android.provider.CallLog.Calls.REJECTED_TYPE -> "Missed"
                    else -> "Incoming"
                }

                val idString = "${normalizedPhone}_${timestamp}"

                val isDismissed = dao.isCallDismissed(idString)
                val isAlreadyLead = dao.isLeadExists(normalizedPhone)
                val isAlreadyLogged = dao.isCallLogExists(normalizedPhone, timestamp)

                if (!isDismissed && !isAlreadyLead && !isAlreadyLogged) {
                    val actualName = if (contactName.isBlank() || contactName == "Unknown") {
                        phoneNumber
                    } else {
                        contactName
                    }
                    dao.insertCallLog(
                        CallLogEntity(
                            contactName = actualName,
                            phoneNumber = normalizedPhone,
                            timestamp = timestamp,
                            duration = durationStr,
                            callType = callTypeStr
                        )
                    )
                }
            }
        }
    }
}
