package com.netmarble.bn.core.audio

import android.content.ContentResolver
import android.content.Context
import androidx.annotation.OptIn
import androidx.annotation.RawRes
import androidx.core.net.toUri
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BgmPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) : DefaultLifecycleObserver {

    private val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ALL
        volume = 0.7f
    }

    private var wasPlayingBeforePause = false
    private var isMusicEnabled = true

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OptIn(UnstableApi::class)
    fun play(@RawRes resId: Int) {
        if (!isMusicEnabled) return

        val uri = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/$resId".toUri()
        val item = MediaItem.fromUri(uri)
        if (player.currentMediaItem == null || player.currentMediaItem?.localConfiguration?.uri != uri) {
            player.setMediaItem(item)
            player.prepare()
        }
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun setMusicEnabled(enabled: Boolean) {
        isMusicEnabled = enabled
        if (!enabled) {
            pause()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        // Додаток повернувся на передній план
        if (wasPlayingBeforePause && isMusicEnabled) {
            player.playWhenReady = true
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        // Додаток пішов у фон
        wasPlayingBeforePause = player.playWhenReady
        player.playWhenReady = false
    }

    fun release() {
        ProcessLifecycleOwner.get().lifecycle.removeObserver(this)
        player.release()
    }
}