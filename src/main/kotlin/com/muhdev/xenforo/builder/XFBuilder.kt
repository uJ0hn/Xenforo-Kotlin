package com.muhdev.xenforo.builder

import com.muhdev.xenforo.XFApi

class XFBuilder {

    private var host : String? = null
    private var port : String? = null
    private var dbname : String? = null
    private var user : String? = null
    private var password : String? = null

    fun setHost(host :String) : XFBuilder {
        this.host = host
        return this
    }
    fun setPort(port :String) : XFBuilder {
        this.port = port
        return this
    }

    fun setDbName(dbname :String) : XFBuilder {
        this.dbname = dbname
        return this
    }
    fun setUser(user :String) : XFBuilder {
        this.user = user
        return this
    }
    fun setPassword(password :String) : XFBuilder {
        this.password = password
        return this
    }

    fun build() : XFApi? {
        if (host == null || port == null || dbname == null || user == null || password == null) {
            println("Nem todos os valores foram definidos")
            return null
        }
        return XFApi(host!!, port!!, dbname!!, user!!, password!!)
    }


}