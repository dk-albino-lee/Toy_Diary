package com.dongkeun.toyproject.model

data class User(
    val account: String,
    val password: String,
) {
    fun mappingForFirestore(): HashMap<String, String> {
        return hashMapOf(
            "account" to account,
            "password" to password
        )
    }
}