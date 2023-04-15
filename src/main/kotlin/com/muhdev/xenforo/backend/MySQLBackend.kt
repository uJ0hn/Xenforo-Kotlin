package com.muhdev.xenforo.backend


import java.sql.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.sql.rowset.CachedRowSet
import javax.sql.rowset.RowSetProvider

class MySQLBackend(private var host : String,
                   private var port : String,
                   private var dbname : String,
                   private var user : String,
                   private var password : String): Backend() {

    private var connection: Connection? = null
    private val executor: ExecutorService = Executors.newCachedThreadPool()


    init {
        openConnection()
    }

    private fun getConnection(): Connection? {
        if (!isConnected) {
            openConnection()
        }
        return connection
    }

    override fun closeConnection() {
        if (isConnected) {
            try {
                connection!!.close()
            } catch (e: SQLException) {
                println("Cannot close MySQL connection: $e")
            }
        }
    }

    private val isConnected: Boolean
        get() {
            try {
                return connection != null && !connection!!.isClosed && connection!!.isValid(5)
            } catch (e: SQLException) {
                println("MySQL error: $e")
            }
            return false
        }

    private fun openConnection() {
        if (!isConnected) {
            try {
                val bol = connection == null
                connection = DriverManager.getConnection(
                    "jdbc:mysql://" + host + ":" + port + "/" + dbname
                            + "?verifyServerCertificate=false&useSSL=false&allowPublicKeyRetrieval=true&useUnicode=yes&characterEncoding=UTF-8", user, password)
                if (bol) {
                    println("Connected to MySQL!")
                    return
                }
                println("Reconnected on MySQL!")
            } catch (e: SQLException) {
                println("Cannot open MySQL connection: $e")
            }
        }
    }

    override fun update(sql: String, vars: List<Any?>?) {
        try {
            val ps: PreparedStatement? = prepareStatement(sql, vars)
            ps?.execute()
            ps?.close()
        } catch (e: SQLException) {
            println("Cannot execute SQL $sql: $e")
        }
    }

    override fun execute(sql: String, vars: List<Any?>?) {
        executor.execute { update(sql, vars) }
    }

    override fun prepareStatement(query: String, vars: List<Any?>?): PreparedStatement? {
        try {
            val ps: PreparedStatement = getConnection()!!.prepareStatement(query)
            vars?.forEachIndexed { index, value ->
                ps.setObject(index + 1, value.toString())
            }

            return ps
        } catch (e: SQLException) {
            println("Cannot Prepare Statement $query: $e")
        }
        return null
    }


    override fun query(sql: String, vars: List<Any?>?): CachedRowSet? {
        try {
            val ps = prepareStatement(sql, vars)
            val rs = ps!!.executeQuery()
            val crs = RowSetProvider.newFactory().createCachedRowSet()
            crs.populate(rs)
            rs.close()
            ps.close()
            if (crs.next()) {
                return crs
            }
        } catch (e: Exception) {
            println("Cannot execute Query $sql: $e")
        }
        return null
    }
}
