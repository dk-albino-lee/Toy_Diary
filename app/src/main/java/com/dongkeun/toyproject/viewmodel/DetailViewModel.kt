package com.dongkeun.toyproject.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongkeun.toyproject.model.Diary

class DetailViewModel : ViewModel() {

    val _diary = MutableLiveData<Diary>()
    val diary: LiveData<Diary> get() = _diary

    val _commentEdit = MutableLiveData<String>()
    val commentEdit: LiveData<String> get() = _commentEdit

    init {
        _diary.value = Diary.EMPTY()
        _commentEdit.value = ""
    }


    override fun onCleared() {
        super.onCleared()
    }
}