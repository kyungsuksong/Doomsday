package com.kyungsuksong.doomsday.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kyungsuksong.doomsday.compose.Screen.Companion.DETAIL_SCREEN_NAV_ARG
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import com.kyungsuksong.doomsday.repo.AsteroidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    asteroidRepository: AsteroidRepository,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    companion object {
        const val NO_ASTEROID_ID: Long = -1L
    }

    // the key should be the same as NavArgument in Screen
    private var asteroidId: Long = savedStateHandle.get<Long>(DETAIL_SCREEN_NAV_ARG) ?: NO_ASTEROID_ID

    val asteroid: SharedFlow<AsteroidEntity>? =
        when(asteroidId) {
            NO_ASTEROID_ID -> null
            else -> {
                asteroidRepository
                    .getAsteroidById(asteroidId)
                    .shareIn(
                        scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(),
                        replay = 1
                    )
            }
        }
}