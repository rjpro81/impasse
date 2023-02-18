package com.rjulsaint.impasse

class SessionManager {
    var sessionUserName: String? = null
    var sessionMasterPassword: String? = null

    companion object {
        val instance = SessionManager()
    }

    fun setUserNameForSession(userName: String){
        sessionUserName = userName
    }

    fun setMasterPasswordForSession(password: String){
        sessionMasterPassword = password
    }
}