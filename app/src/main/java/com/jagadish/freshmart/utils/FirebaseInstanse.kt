package com.jagadish.freshmart.utils

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.jagadish.freshmart.data.SharedPreferencesUtils

class FirebaseInstanse : FirebaseMessagingService(){

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        SharedPreferencesUtils.setAppStringPreference(SharedPreferencesUtils.PREF_APP_FCM_TOKEN,p0)
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
    }
}