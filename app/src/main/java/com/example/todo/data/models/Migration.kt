package com.example.todo.data.models

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `${TABLE_NAME}` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `priority` TEXT NOT NULL, `description` TEXT NOT NULL, `dueTime` INTEGER NOT NULL, `notificationID` INTEGER NOT NULL)")
        }
    }

    companion object {
        private const val TABLE_NAME = "todo_table"
    }

}