package com.muhdev.xenforo.interfaces

interface User {

    fun userExists() : Boolean
    fun getId() : Int
    fun getName() : String
    fun changePassword(newpass : String)
    fun getHashedPass(): String

    fun getGroups() : List<Group>
    fun setFirstGroup(group: Group)
    fun setSecondGroup(vararg group: Group, rmothers: Boolean = true)
}