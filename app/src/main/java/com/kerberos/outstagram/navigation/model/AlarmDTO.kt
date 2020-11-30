package com.kerberos.outstagram.navigation.model

data class AlarmDTO (
    var destinationUid : String? = null,
    var userId : String? = null,
    var uid : String? = null,
    var kind : Int = 0, // 0 like 1 message 2 follow
    var message : String? = null,
    var timestamp : Long? = null
)