package com.netmarble.bn.ui.music

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.AudioAttributes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.netmarble.bn.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val player: ExoPlayer by lazy {
        ExoPlayer.Builder(appContext).build().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(androidx.media3.common.C.USAGE_MEDIA)
                    .setContentType(androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            repeatMode = Player.REPEAT_MODE_ALL
            volume = 0.7f
            val uri = "android.resource://${appContext.packageName}/${R.raw.music}".toUri()
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    fun playLoop() {
        viewModelScope.launch(Dispatchers.Main) {
            if (!player.playWhenReady) player.playWhenReady = true
        }
    }

    fun pause() {
        viewModelScope.launch(Dispatchers.Main) {
            player.playWhenReady = false
        }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}