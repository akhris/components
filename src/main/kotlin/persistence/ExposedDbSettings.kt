package persistence

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import persistence.dto.exposed.Tables

object ExposedDbSettings {
    val db by lazy {
        Database.connect("jdbc:sqlite:/home/anatoly/.components_app/data.db", "org.sqlite.JDBC")
//        Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
//        TransactionManager.manager.defaultIsolationLevel =
//            TRANSACTION_READ_UNCOMMITTED
        transaction {
            addLogger(StdOutSqlLogger)

            SchemaUtils.create(
                Tables.Items,
                Tables.Containers,
                Tables.Suppliers,
                Tables.Units,
                Tables.ItemIncomes,
                Tables.ItemOutcomes,
                Tables.ObjectTypes,
                Tables.Values,
                Tables.Parameters,
                Tables.ContainerToContainers,
                Tables.ValuesToItem,
                Tables.ParametersToObjectType
            )
        }
    }
}

/*

// In file
Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
// In memory
Database.connect("jdbc:sqlite:file:test?mode=memory&cache=shared", "org.sqlite.JDBC")
// For both: set SQLite compatible isolation level, see
// https://github.com/JetBrains/Exposed/wiki/FAQ
TransactionManager.manager.defaultIsolationLevel =
    Connection.TRANSACTION_SERIALIZABLE
    // or Connection.TRANSACTION_READ_UNCOMMITTED
//Gradle
implementation("org.xerial:sqlite-jdbc:3.30.1")


 */