package com.leodeleon.hikepix.data

import com.flickr4java.flickr.Flickr
import com.flickr4java.flickr.photos.Extras
import com.flickr4java.flickr.photos.SearchParameters
import com.leodeleon.hikepix.domain.LatLng
import timber.log.Timber

class PhotoRepository(private val flickr: Flickr) {

    fun getPhotoForLocation(latLng: LatLng): Result<String> {
        return flickr.photosInterface.search(
            SearchParameters().apply {
                latitude = latLng.latitude.toString()
                longitude = latLng.longitude.toString()
                extras = Extras.ALL_EXTRAS
                media = "photos"
            }, 0, 0
        ).randomOrNull()?.let {
            try {
                val url = it.getSmallUrl()
                Timber.d(url)
                Result.success(url)
            } catch (t: Throwable) {
                Timber.e(t)
                Result.failure(Exception("Error getting URL for photo ${it.id}"))
            }

        }?: Result.failure(Exception("No photo for location $latLng"))
    }
}