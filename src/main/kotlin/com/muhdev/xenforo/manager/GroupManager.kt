package com.muhdev.xenforo.manager

import com.muhdev.xenforo.XFApi
import com.muhdev.xenforo.backend.Backend
import com.muhdev.xenforo.interfaces.Group
import com.muhdev.xenforo.interfaces.User
import javax.print.attribute.standard.RequestingUserName
import javax.sql.rowset.CachedRowSet

class GroupManager  {

    class ById(private val id: Int) : Group {



        override fun getName(): String {
            return cachedrowset()!!.getString("title")
        }

        private fun cachedrowset() : CachedRowSet? {
            return Backend.getInstance().query("SELECT * FROM xf_user_group WHERE user_group_id=?", listOf(id))
        }
        override fun getId(): Int {
            return id
        }

        override fun getUsersWithPrimary(): List<User> {
            val list = ArrayList<User>()
            val query = Backend.getInstance().query("SELECT * FROM xf_user",null) ?: return list
            var i = 0
            while (i < query.size()) {
                i++
                if(query.getString("user_group_id") == getId().toString()) list.add(XFApi.getApi().getUserByName(query.getString("username"))!!)
                query.next()
            }
            return list
        }

        override fun getUsersWithSecundary(): List<User> {
            val list = ArrayList<User>()
            val query = Backend.getInstance().query("SELECT * FROM xf_user",null) ?: return list
            var i = 0
            while (i < query.size()) {
                i++
                val string = String(query.getBytes("secondary_group_ids"), Charsets.UTF_8)

                if(string.split(",").contains(getId().toString())) list.add(XFApi.getApi().getUserByName(query.getString("username"))!!)
                query.next()
            }
            return list
        }

        override fun groupExists(): Boolean {
            return cachedrowset() != null
        }

    }

    class ByName(val userName: String) : Group {

        override fun groupExists(): Boolean {
            return cachedrowset() != null
        }
        override fun getName(): String {
            return userName
        }

        private fun cachedrowset() : CachedRowSet? {
            return Backend.getInstance().query("SELECT * FROM xf_user_group WHERE title=?", listOf(userName))
        }
        override fun getId(): Int {
            return cachedrowset()!!.getInt("user_group_id")
        }

        override fun getUsersWithPrimary(): List<User> {
            val list = ArrayList<User>()
            val query = Backend.getInstance().query("SELECT * FROM xf_user",null) ?: return list
            var i = 0
            while (i < query.size()) {
                i++
                if(query.getString("user_group_id") == getId().toString()) list.add(XFApi.getApi().getUserByName(query.getString("username"))!!)
                query.next()
            }
            return list
        }

        override fun getUsersWithSecundary(): List<User> {
            val list = ArrayList<User>()
            val query = Backend.getInstance().query("SELECT * FROM xf_user",null) ?: return list
            var i = 0
            while (i < query.size()) {
                i++
                val binaryData = query.getBytes("secondary_group_ids")
                if(String(binaryData, Charsets.UTF_8).split(",").contains(getId().toString())) list.add(XFApi.getApi().getUserByName(query.getString("username"))!!)
                query.next()
            }
            return list
        }

    }

}