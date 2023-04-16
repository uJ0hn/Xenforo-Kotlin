package com.muhdev.xenforo

import com.muhdev.xenforo.backend.Backend
import com.muhdev.xenforo.interfaces.Group
import com.muhdev.xenforo.interfaces.User
import com.muhdev.xenforo.manager.GroupManager
import com.muhdev.xenforo.manager.UsersManager

class XFApi(private val host : String,
            private val port : String,
            private val dbname : String,
            private val user : String,
            private val password : String) {

    init {
        api = this
        Backend.makeBackend(this.host, this.port, this.dbname, this.user, this.password)
    }

    fun getUserByID(id: Int): User? {
        return if(UsersManager.ById(id).userExists()) UsersManager.ById(id)
        else null
    }

    fun getUserByName(user: String): User? {
        return if(!UsersManager.ByName(user).userExists()) null
        else UsersManager.ByName(user)
    }

    fun getGroupByName(user: String) : Group? {
        return if(!GroupManager.ByName(user).groupExists()) null
        else GroupManager.ByName(user)
    }

    fun getGroupById(id: Int) : Group? {
        return if(!GroupManager.ById(id).groupExists()) null
        else GroupManager.ById(id)
    }

    companion object {
        private lateinit var api : XFApi
        fun getApi() : XFApi {
            return api
        }
    }


}