package com.leodeleon.hikepix

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.leodeleon.hikepix.data.PhotoRepository
import com.leodeleon.hikepix.domain.usecase.GetPhotoFromLocation
import com.leodeleon.hikepix.service.LocationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    locationManager: LocationManager,
    repository: PhotoRepository
    ) : ViewModel() {
    private val photoCache = mutableListOf<String>()

    private val getPhotoUseCase = GetPhotoFromLocation(locationManager, repository)
    private val _photos = MutableLiveData<List<String>>()
    val photos: LiveData<List<String>> = _photos

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getPhotoUseCase().collect {
                photoCache.add(0,it)
                _photos.postValue(ArrayList(photoCache))
            }
        }
    }

    fun dispose() {
        photoCache.clear()
        _photos.value = null
    }
}