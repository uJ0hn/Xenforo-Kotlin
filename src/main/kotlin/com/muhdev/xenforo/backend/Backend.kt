package com.muhdev.xenforo.backend

import java.sql.PreparedStatement
import javax.sql.rowset.CachedRowSet

abstract class Backend {


    abstract fun closeConnection()

    abstract fun update(sql: String, vars: List<Any?>?)

    abstract fun execute(sql: String, vars: List<Any?>?)

    abstract fun query(sql: String, vars: List<Any?>?): CachedRowSet?

    abstract fun prepareStatement(query: String, vars: List<Any?>?): PreparedStatement?


    companion object {
        private lateinit var instance: Backend

        fun getInstance(): Backend {
            return instance
        }

        fun makeBackend( host : String,
                         port : String,
                         dbname : String,
                         user : String,
                         password : String) {
            instance = MySQLBackend(host, port, dbname, user, password)
        }
    }

}