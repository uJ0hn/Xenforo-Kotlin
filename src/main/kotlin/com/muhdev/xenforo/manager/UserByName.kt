package com.muhdev.xenforo.manager

import com.muhdev.xenforo.backend.Backend
import com.muhdev.xenforo.interfaces.User
import com.muhdev.xenforo.utils.BCrypt
import java.io.File
import java.io.FileInputStream
import javax.sql.rowset.CachedRowSet

class UserByName(val username: String) : User {
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
        TODO("Not yet implemented")
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


}