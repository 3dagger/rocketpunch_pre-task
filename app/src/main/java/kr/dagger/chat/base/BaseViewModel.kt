package kr.dagger.chat.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

	private val _isProgress = MutableLiveData<Boolean>()
	val isProgress : LiveData<Boolean>
		get() = _isProgress

	private val _toastMessage = MutableLiveData<String>()
	val toastMessage : LiveData<String>
		get() = _toastMessage

	fun setProgress(value: Boolean) {
		_isProgress.value = value
	}

	fun setToast(value: String) {
		_toastMessage.value = value
	}
}