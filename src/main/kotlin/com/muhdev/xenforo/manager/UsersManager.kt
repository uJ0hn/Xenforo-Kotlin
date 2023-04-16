package com.muhdev.xenforo.manager

import com.muhdev.xenforo.XFApi
import com.muhdev.xenforo.backend.Backend
import com.muhdev.xenforo.interfaces.Group
import com.muhdev.xenforo.interfaces.User
import com.muhdev.xenforo.utils.BCrypt
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset
import javax.sql.rowset.CachedRowSet

class UsersManager  {

    class ById(val idd: Int) : User{
        override fun userExists(): Boolean {
            return cachedrowset() != null
        }

        override fun getId(): Int {
            return idd
        }


        private fun cachedrowset() : CachedRowSet? {
            return Backend.getInstance().query("SELECT * FROM xf_user WHERE user_id=?", listOf(idd))
        }

        override fun getName(): String {
            return cachedrowset()!!.getString("username")
        }

        override fun changePassword(newpass : String) {
            val tempFile = File.createTempFile("${getName()}.bin", null)
            try {
                val hashed = BCrypt.hashpw(newpass, BCrypt.gensalt())
                tempFile.writeText("a:1:{s:4:\"hash\";s:60:\"$hashed\";}")
                val inputStream = FileInputStream(tempFile)
                val bytes = inputStream.readBytes()

                val pstmt = Backend.getInstance().prepareStatement("UPDATE xf_user_authenticate SET data=? WHERE user_id=?", null)!!
                pstmt.setBytes(1, bytes)
                pstmt.setObject(2, getId())
                pstmt.executeUpdate()
                pstmt.close()
            } finally {
                tempFile.delete()
            }
        }

        override fun getHashedPass(): String {
            val query = Backend.getInstance().query("SELECT * FROM xf_user_authenticate WHERE user_id=?", listOf(getId()))!!
            val binaryData = query.getBytes("data")
            return String(binaryData, Charsets.UTF_8)
                .replace("a:1:{s:4:\"hash\";s:60:\"", "")
                .replace("\";}", "")
        }

        override fun getGroups(): List<Group> {
            val groups = ArrayList<Group>()
            val primary = cachedrowset()!!.getInt("user_group_id")
            groups.add(XFApi.getApi().getGroupById(primary)!!)
            val secundaries = String(cachedrowset()!!.getBytes("secondary_group_ids"), Charsets.UTF_8).split(",")
            for(a in secundaries) groups.add(XFApi.getApi().getGroupById(a.toInt())!!)

            return groups
        }

        override fun setFirstGroup(group: Group) {
            Backend.getInstance().execute("UPDATE xf_user SET user_group_id=? WHERE user_id=?", listOf(group.getId(), getId()))
        }

        override fun setSecondGroup(vararg group: Group, rmothers: Boolean) {
            var byte = ""
            if(!rmothers) byte = String(cachedrowset()!!.getBytes("secondary_group_ids"), Charsets.UTF_8)
            for(g in group) {
                if (byte.isEmpty()) byte = g.getId().toString()
                else if(!byte.contains(g.getId().toString())) byte += ",${g.getId()}"
            }
            Backend.getInstance().execute("UPDATE xf_user SET secondary_group_ids=? WHERE user_id=?", listOf(byte, getId()))
        }


    }

    class ByName(val username: String) : User{
        override fun userExists(): Boolean {
            return cachedrowset() != null
        }

        override fun getId(): Int {
            return cachedrowset()!!.getInt("user_id")
        }


        private fun cachedrowset() : CachedRowSet? {
            return Backend.getInstance().query("SELECT * FROM xf_user WHERE username=?", listOf(username))
        }

        override fun getName(): String {
            return this.username
        }

        override fun changePassword(newpass : String) {
            val tempFile = File.createTempFile("$username.bin", null)
            try {
                val hashed = BCrypt.hashpw(newpass, BCrypt.gensalt())
                tempFile.writeText("a:1:{s:4:\"hash\";s:60:\"$hashed\";}")
                val inputStream = FileInputStream(tempFile)
                val bytes = inputStream.readBytes()

                val pstmt = Backend.getInstance().prepareStatement("UPDATE xf_user_authenticate SET data=? WHERE user_id=?", null)!!
                pstmt.setBytes(1, bytes)
                pstmt.setObject(2, getId())
                pstmt.executeUpdate()
                pstmt.close()
            } finally {
                tempFile.delete()
            }
        }

        override fun getHashedPass(): String {
            val query = Backend.getInstance().query("SELECT * FROM xf_user_authenticate WHERE user_id=?", listOf(getId()))!!
            val binaryData = query.getBytes("data")
            return String(binaryData, Charsets.UTF_8)
                .replace("a:1:{s:4:\"hash\";s:60:\"", "")
                .replace("\";}", "")
        }

        override fun getGroups(): List<Group> {
            val groups = ArrayList<Group>()
            val primary = cachedrowset()!!.getInt("user_group_id")
            groups.add(XFApi.getApi().getGroupById(primary)!!)
            val secundaries = String(cachedrowset()!!.getBytes("secondary_group_ids"), Charsets.UTF_8).split(",")
            for(a in secundaries) groups.add(XFApi.getApi().getGroupById(a.toInt())!!)

            return groups
        }

        override fun setFirstGroup(group: Group) {
            Backend.getInstance().execute("UPDATE xf_user SET user_group_id=? WHERE user_id=?", listOf(group.getId(), getId()))
        }

        override fun setSecondGroup(vararg group: Group, rmothers: Boolean) {
            var byte = ""
            if(!rmothers) byte = String(cachedrowset()!!.getBytes("secondary_group_ids"), Charsets.UTF_8)
            for(g in group) {
                if (byte.isEmpty()) byte = g.getId().toString()
                else if(!byte.contains(g.getId().toString())) byte += ",${g.getId()}"
            }
            Backend.getInstance().execute("UPDATE xf_user SET secondary_group_ids=? WHERE user_id=?", listOf(byte, getId()))
        }
    }

}