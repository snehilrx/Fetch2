package database

import Constants
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.otaku.fetch2.Database
import java.io.File

actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val dbPath = File(System.getProperty("java.io.tmpdir"), Constants.DB_FILE)
        val driver = JdbcSqliteDriver(url = "jdbc:sqlite:${dbPath.absolutePath}", )
        Database.Schema.create(driver)
        return driver
    }
}