package com.dongkeun.toyproject

import android.app.Application
import com.dongkeun.toyproject.firestore.firestore
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BaseApplication : Application() {

    private val db: FirebaseFirestore by lazy {
        firestore.instance()
    }

    override fun onCreate() {
        super.onCreate()

    }

    fun getFireStoreDb() = db
}