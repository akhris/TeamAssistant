package persistence.exposed.dao

import domain.EntitiesList
import domain.IDao
import domain.ISpecification
import domain.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.InsertStatement
import org.jetbrains.exposed.sql.statements.UpdateStatement
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import persistence.exposed.dto.EntityUser
import persistence.exposed.dto.Tables
import persistence.exposed.toUser
import utils.log
import utils.toUUID
import java.util.*

class UsersDao : IDao<User> {

    private fun updateStatement(entity: User): Tables.Users.(UpdateStatement) -> Unit = {
        it[name] = entity.name
        it[middleName] = entity.middleName
        it[surname] = entity.surname
        it[email] = entity.email
        it[phoneNumber] = entity.phoneNumber
        it[roomNumber] = entity.roomNumber
        it[color] = entity.color
    }

    private fun insertStatement(entity: User): Tables.Users.(InsertStatement<Number>) -> Unit = {
        it[id] = entity.id.toUUID()
        it[name] = entity.name
        it[middleName] = entity.middleName
        it[surname] = entity.surname
        it[email] = entity.email
        it[phoneNumber] = entity.phoneNumber
        it[roomNumber] = entity.roomNumber
        it[color] = entity.color
    }

    override suspend fun getByID(id: String): User? {
        return newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            try {
                EntityUser[id.toUUID()].toUser()
            } catch (e: Exception) {
                log(e.localizedMessage)
                null
            }
        }
    }

    override suspend fun removeById(id: String) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Users.deleteWhere { Tables.Users.id eq id.toUUID() }
        }
    }

    override suspend fun query(specs: List<ISpecification>): EntitiesList<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getItemsCount(specs: List<ISpecification>): Long {
        TODO("Not yet implemented")
    }

    override suspend fun update(entities: List<User>) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            entities.forEach { entity ->
                Tables.Users.update(where = {
                    Tables.Users.id eq entity.id.toUUID()
                }, body = updateStatement(entity))
            }
        }
    }

    override suspend fun update(entity: User) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Users.update(where = {
                Tables.Users.id eq entity.id.toUUID()
            }, body = updateStatement(entity))
        }
    }

    override suspend fun insert(entity: User) {
        newSuspendedTransaction {
            addLogger(StdOutSqlLogger)
            Tables.Users.insert(insertStatement(entity))
        }
    }
}