package com.netmarble.bn.core.audio

import android.content.Context
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.RawResourceDataSource
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BgmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ALL
        volume = 0.7f
    }

    @OptIn(UnstableApi::class)
    fun play(@RawRes resId: Int) {
        val uri = RawResourceDataSource.buildRawResourceUri(resId)
        val item = MediaItem.fromUri(uri)
        if (player.currentMediaItem == null || player.currentMediaItem?.localConfiguration?.uri != uri) {
            player.setMediaItem(item)
            player.prepare()
        }
        player.playWhenReady = true
    }

    fun pause() { player.playWhenReady = false }


    fun release() { player.release() }
}