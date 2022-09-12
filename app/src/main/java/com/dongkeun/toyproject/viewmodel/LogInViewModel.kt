package com.dongkeun.toyproject.viewmodel

import android.app.Application
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dongkeun.toyproject.BaseApplication
import com.dongkeun.toyproject.R
import com.dongkeun.toyproject.model.Diary
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.util.*

class LogInViewModel(application: Application) : AndroidViewModel(application) {
    private var job: Job? = null
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private val db = getApplication<BaseApplication>().getFireStoreDb()

    val _idInput = MutableLiveData<String>()
    val idInput: LiveData<String> get() = _idInput
    val _pwInput = MutableLiveData<String>()
    val pwInput: LiveData<String> get() = _pwInput

    private val _toSignUp = MutableLiveData<Boolean>()
    val toSignUp: LiveData<Boolean> get() = _toSignUp

    private val _logInFlag = MutableLiveData<Boolean>()
    val logInFlag: LiveData<Boolean> get() = _logInFlag

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String> get() = _toast

    init {
        _idInput.value = ""
        _pwInput.value = ""
        _logInFlag.value = false
        _toast.value = ""
        _toSignUp.value = false
    }

    fun logIn() {
        _logInFlag.value = false
        if (!validateInputs()) {
            _toast.value = "계정 또는 비밀번호를 입력해주세요."
            return
        }
        if (!validatePassword()) {
            _toast.value = "비밀번호는 최소 6자리 이상입니다."
            return
        }

        job = ioScope.launch {
            val process = async { asyncTestFirebase() }
            process.await()
            _logInFlag.postValue(true)
        }
    }

    private fun validateInputs() = idInput.value!!.trim().isNotEmpty() && pwInput.value!!.trim().isNotEmpty()

    private fun validatePassword() = pwInput.value!!.trim().length > 5

    private fun asyncTestFirebase() {
        val data1 = Diary(
            id = UUID.randomUUID().toString(),
            account = idInput.value!!.trim(),
            title = "My second data",
            content = "test data.",
            createdTime = Calendar.getInstance(Locale.getDefault()).time.time,
        )
            .mappingForFireStore()

        db.collection("user_diary")
            .add(data1)
            .addOnSuccessListener { documentReference ->
                Log.d("Haha", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { error ->
                Log.d("OMG", error.stackTraceToString())
            }
    }

    fun toSignUpPage() {
        _toSignUp.value = true
    }

    fun initToast() {
        _toast.value = ""
    }

    // extension function to convert bitmap to byte array
    fun Bitmap.toByteArray(): ByteArray{
        ByteArrayOutputStream().apply {
            compress(Bitmap.CompressFormat.JPEG,10,this)
            return toByteArray()
        }
    }

    private fun saveImgToStorage(imgId: Int) {
        val imgUri = Uri.parse("android.resource://" + getApplication<BaseApplication>().applicationContext.packageName + "$imgId")
        if (imgUri.path == null) {
            _toast.value = "shit!"
            return
        }
        val file = imgUri.path?.let { File(it) }

//        val stream = FileInputStream(file)

        val storageRef = FirebaseStorage.getInstance().reference
        val mountainRef= storageRef.child(imgUri.path!!)

        val stream = FileInputStream(File("diary/image/sample01.jpg"))


        val uploadTask = storageRef.putStream(stream)
            .apply {
                addOnSuccessListener {

                }
                addOnFailureListener {
                    try {
                        _toast.value = "shit"
                    } catch (e: IllegalStateException) {
                        _toast.postValue("shit")
                    }
                }
            }
    }

    private fun bitmapToString(img: Bitmap): String {
        val baos = ByteArrayOutputStream()
        img.compress(Bitmap.CompressFormat.PNG, 1, baos)

        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}