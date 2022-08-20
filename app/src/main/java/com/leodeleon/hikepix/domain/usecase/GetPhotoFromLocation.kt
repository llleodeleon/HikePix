package com.leodeleon.hikepix.domain.usecase

import com.leodeleon.hikepix.service.LocationManager
import com.leodeleon.hikepix.data.PhotoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull

class GetPhotoFromLocation(private val manager: LocationManager, private val repository: PhotoRepository) {

    operator fun invoke(): Flow<String> {
        return manager.locationFlow.mapNotNull {
           repository.getPhotoForLocation(it).getOrNull()
        }
    }
}