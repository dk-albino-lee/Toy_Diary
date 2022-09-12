package com.dongkeun.toyproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongkeun.toyproject.BaseApplication
import com.dongkeun.toyproject.firestore.firestore
import com.dongkeun.toyproject.model.Diary
import kotlinx.coroutines.*

class ListViewModel(application: Application) : AndroidViewModel(application) {

    private var job: Job? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val db = getApplication<BaseApplication>().getFireStoreDb()

    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    init {
        _diaries.value = listOf()
    }

    private val dummyDiaries = listOf(
        Diary.TEST(), Diary.TEST(), Diary.TEST(), Diary.TEST(), Diary.TEST()
    )

    fun getDiaries() {
        job = CoroutineScope(Dispatchers.IO).launch {
            asyncGetDiaries()
        }
    }

    private fun asyncGetDiaries() {
        job = ioScope.launch {
            db.collection("diaries")
                .whereEqualTo("account", "myAccount01")
                .get()
                .addOnSuccessListener { result ->
                    if (result.isEmpty) {
                        _diaries.postValue(dummyDiaries)
                    } else {
                        val retList = mutableListOf<Diary>()
                        for (document in result) {
                            val obj = document.toObject(Diary::class.java)
                            retList.add(obj)
                        }
                        _diaries.postValue(retList)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("FireStore get failed", exception.stackTraceToString())
                }
        }
    }

    private suspend fun getDiariesSuspend() = withContext(Dispatchers.IO) {
        _diaries.postValue(dummyDiaries)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}