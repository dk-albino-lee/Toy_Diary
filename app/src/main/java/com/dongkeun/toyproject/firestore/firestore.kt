package com.dongkeun.toyproject.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

interface firestore {
    companion object {
        fun instance() = Firebase.firestore
    }
}