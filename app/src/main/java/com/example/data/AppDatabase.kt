package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CallLogEntity::class, LeadEntity::class, DismissedCallEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dao(): CallLogAndLeadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "lite_crm_database"
                )
                .fallbackToDestructiveMigration()
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class DatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.dao())
                }
            }
        }
    }
}

suspend fun populateInitialData(dao: CallLogAndLeadDao) {
    val now = System.currentTimeMillis()
    val demoLogs = listOf(
        CallLogEntity(
            contactName = "John Doe",
            phoneNumber = "+1555019283",
            timestamp = now - 1000 * 60 * 15, // 15 mins ago
            duration = "2m 14s",
            callType = "Incoming"
        ),
        CallLogEntity(
            contactName = "Alice Smith",
            phoneNumber = "9876543210",
            timestamp = now - 1000 * 60 * 60 * 2, // 2 hours ago
            duration = "0m 45s",
            callType = "Outgoing"
        ),
        CallLogEntity(
            contactName = "Michael Brown",
            phoneNumber = "+442079460192",
            timestamp = now - 1000 * 60 * 60 * 5, // 5 hours ago
            duration = "4m 50s",
            callType = "Incoming"
        ),
        CallLogEntity(
            contactName = "Emma Watson",
            phoneNumber = "1234567890",
            timestamp = now - 1000 * 60 * 60 * 24, // 1 day ago
            duration = "1m 15s",
            callType = "Missed"
        ),
        CallLogEntity(
            contactName = "David Beckman",
            phoneNumber = "+919876543210",
            timestamp = now - 1000 * 60 * 60 * 30, // 30 hours ago
            duration = "5m 12s",
            callType = "Outgoing"
        ),
        CallLogEntity(
            contactName = "Sophia Loren",
            phoneNumber = "+39066988384",
            timestamp = now - 1000 * 60 * 60 * 48, // 2 days ago
            duration = "3m 40s",
            callType = "Incoming"
        )
    )
    dao.insertCallLogs(demoLogs)
}
