package com.leodeleon.hikepix.ui

import android.Manifest
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.leodeleon.hikepix.MainViewModel
import com.leodeleon.hikepix.R
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import splitties.resources.appStr

@Composable
fun HikeScreen(
    viewModel: MainViewModel,
    onStartHike: () -> Unit,
    onStopHike: () -> Unit
) {
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    var isHiking by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    if (permissionState.allPermissionsGranted) {
                        TextButton(onClick = {
                            isHiking = if (isHiking) {
                                onStopHike()
                                viewModel.dispose()
                                false
                            } else {
                                onStartHike()
                                true
                            }
                        }) {
                            val buttonText =
                                if (isHiking) appStr(R.string.stop) else appStr(R.string.start)
                            Text(
                                text = buttonText.uppercase(),
                                color = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                })
        }) {

        LocationPermission(permissionState) {
            val state = viewModel.photos.observeAsState()
            val photos = state.value.orEmpty()
            val lazyListState = rememberLazyListState()
            val scope = rememberCoroutineScope()

            if (photos.isNotEmpty()) {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            }

            LazyColumn(
                state = lazyListState,
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxSize()
            ) {
                items(
                    count = photos.size,
                    key = { if (photos.isNotEmpty()) photos[it] else "" },
                    itemContent = {
                        Surface(elevation = 4.dp, modifier = Modifier.padding(bottom = 8.dp)) {
                            GlideImage(
                                imageModel = photos[it],
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .height(240.dp)
                                    .fillMaxWidth()
                            )
                        }
                    })
            }
        }
    }
}
