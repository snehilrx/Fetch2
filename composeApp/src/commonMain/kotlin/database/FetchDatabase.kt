package database

import app.cash.sqldelight.db.SqlDriver
import com.otaku.fetch2.Database
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect class DriverFactory() {
    fun createDriver(): SqlDriver
}

fun createDatabase(driverFactory: DriverFactory): Database {
    val driver = driverFactory.createDriver()
    return Database(driver)
}