package com.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log
import com.example.data.AppDatabase
import com.example.data.CrmRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class CallReceiver : BroadcastReceiver() {

    private val receiverScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        Log.d("CallReceiver", "Received intent action: $action")
        
        if (action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            Log.d("CallReceiver", "Phone state changed: $state")

            if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                // The phone call has just finished. We should immediately sync the new call log!
                val pendingResult = goAsync()
                receiverScope.launch {
                    try {
                        Log.d("CallReceiver", "Starting background call log sync...")
                        val database = AppDatabase.getDatabase(context.applicationContext, this)
                        val repository = CrmRepository(database.dao())
                        repository.syncDeviceCallLogs(context.applicationContext)
                        Log.d("CallReceiver", "Background call log sync complete")
                    } catch (e: Exception) {
                        Log.e("CallReceiver", "Error while syncing call logs in background", e)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }
}
