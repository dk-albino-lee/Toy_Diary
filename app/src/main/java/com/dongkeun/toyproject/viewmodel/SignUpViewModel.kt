package com.dongkeun.toyproject.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.dongkeun.toyproject.BaseApplication
import com.dongkeun.toyproject.model.User
import kotlinx.coroutines.*

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    private var job: Job? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val db = getApplication<BaseApplication>().getFireStoreDb()

    val _idInput = MutableLiveData<String>()
    val idInput: LiveData<String> get() = _idInput
    val _pwInput = MutableLiveData<String>()
    val pwInput: LiveData<String> get() = _pwInput
    val _pwConfirmInput = MutableLiveData<String>()
    val pwConfirmInput: LiveData<String> get() = _pwConfirmInput

    private val _toLogIn = MutableLiveData<Boolean>()
    val toLogIn: LiveData<Boolean> get() = _toLogIn

    val _toast = MutableLiveData<String>()
    val toast: LiveData<String> get() = _toast

    val isPwConfirmed: LiveData<Boolean> = Transformations.switchMap(pwConfirmInput) {
        MutableLiveData<Boolean>()
            .apply {
                value = isPasswordDoubleChecked()
            }
    }

    init {
        _idInput.value = ""
        _pwInput.value = ""
        _pwConfirmInput.value = ""
        _toLogIn.value = false
    }

    fun confirmSignUp() {
        if (!isValidInputs()) {
            _toast.value = "계정과 비밀번호를 모두 입력해주세요."
            return
        }
        if (!isValidPassword()) {
            _toast.value = "비밀번호는 최소 6자리여야 합니다."
            return
        }
        if (!isPasswordDoubleChecked()) {
            _toast.value = "비밀번호를 확인해주세요."
            return
        }

        job = ioScope.launch {
            asyncSignUp()
        }
    }

    private fun asyncSignUp() {
        val userData = User(account = idInput.value!!, password = pwInput.value!!)
            .mappingForFirestore()

        db.collection("users")
            .add(userData)
            .addOnSuccessListener {
                try {
                    _toast.value = "회원가입이 완료되었습니다."
                } catch (e: IllegalStateException) {
                    _toast.postValue("회원가입이 완료되었습니다.")
                }
                setToLogIn()
            }
            .addOnFailureListener { error ->
                Log.d("OMG", error.stackTraceToString())
            }
    }

    private fun isValidInputs()= idInput.value!!.isNotEmpty() && pwInput.value!!.isNotEmpty()

    private fun isValidPassword() = pwInput.value!!.length > 5

    private fun isPasswordDoubleChecked() = _pwInput.value == _pwConfirmInput.value

    private fun setToLogIn() {
        try {
            _toLogIn.value = true
        } catch (e: IllegalStateException) {
            _toLogIn.postValue(true)
        }
    }

    fun initToast() {
        _toast.value = ""
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}