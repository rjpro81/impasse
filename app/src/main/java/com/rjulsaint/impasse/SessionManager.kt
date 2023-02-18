package com.rjulsaint.impasse

class SessionManager() {
    var sessionUserName: String? = null
    var sessionMasterPassword: String? = null

    companion object{
        val instance = SessionManager()
    }

    fun setUserName(userName:String){
        instance.sessionUserName = userName
    }

    fun setMasterPassword(masterPassword:String){
        instance.sessionMasterPassword = masterPassword
    }
}