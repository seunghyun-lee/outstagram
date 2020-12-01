package com.kerberos.outstagram.navigation.util

import com.kerberos.outstagram.navigation.model.PushDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class FcmPush {
    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
    val url = "https://fcm.googleapos.com/fcm/send"
    val serverKey = "AAAAghGfB0E:APA91bHop0NRGkIGNX6wSfaipbw0CEquYxkBHNUp8igv7BngjEPyewrm9lE5Cg73XFnqH2nBC4BdmyuIQAtZsXaW6IiDfOV5OT5UpuaFVQciS0NLfQcfSvn1DeMgxQZip_ViF_NfIfcT"

    var okHttpClient: OkHttpClient? = null
    var gson: Gson? = null
    companion object {
        var instance = FcmPush()
    }

    init {
        gson = Gson()
        okHttpClient = OkHttpClient()
    }

    fun sendMessage(destinationUid: String, title: String, message: String) {
        FirebaseFirestore.getInstance().collection("pushtokens").document(destinationUid).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var token = task.result?.get("pushtoken").toString()
                    println(token)
                    var pushDTO = PushDTO()
                    pushDTO.to = token
                    pushDTO.notification?.title = title
                    pushDTO.notification?.body = message

                    var body = RequestBody.create(JSON, gson?.toJson(pushDTO)!!)
                    var request = Request.Builder().addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "key=" + serverKey).url(url).post(body).build()
                    okHttpClient?.newCall(request)?.enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                            println(response?.body?.string())
                        }

                    })
                }
            }
    }
}