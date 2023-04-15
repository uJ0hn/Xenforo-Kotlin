package com.muhdev.xenforo

import com.muhdev.xenforo.backend.Backend
import com.muhdev.xenforo.interfaces.User
import com.muhdev.xenforo.manager.UserByName

class XFApi(private val host : String,
            private val port : String,
            private val dbname : String,
            private val user : String,
            private val password : String) {

    init {
        Backend.makeBackend(this.host, this.port, this.dbname, this.user, this.password)
    }

    fun getUserByName(user: String): User {
        return UserByName(user)
    }




}