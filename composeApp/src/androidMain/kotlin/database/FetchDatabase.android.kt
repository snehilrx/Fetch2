package database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.otaku.fetch2.Database
import com.otaku.fetch2.FetchApp

actual class DriverFactory actual constructor() {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(Database.Schema, FetchApp.appInstance, "Database")
    }
}