package video


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi

data class Progress(
    val fraction: Float,
    val timeMillis: Long
)


@Composable
fun Video(url: String, slug: String, modifier: Modifier) {
    val state = rememberVideoPlayerState()
    Column {
        VideoPlayer(
            url = url,
            state = state,
            onFinish = state::stopPlayback,
            slug = slug,
            modifier = modifier
        )
        Slider(
            value = state.progress.value.fraction,
            onValueChange = { state.seek = it },
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Timestamp: ${state.progress.value.timeMillis} ms", modifier = Modifier.width(180.dp))
            IconButton(onClick = state::toggleResume) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Check),
                    contentDescription = "Play/Pause",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = state::toggleFullscreen) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Call),
                    contentDescription = "Toggle fullscreen",
                    modifier = Modifier.size(32.dp)
                )
            }
            Speed(
                initialValue = state.speed,
                modifier = Modifier.width(104.dp)
            ) {
                state.speed = it ?: state.speed
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = rememberVectorPainter(Icons.Default.Build),
                    contentDescription = "Volume",
                    modifier = Modifier.size(32.dp)
                )
                // TODO: Make the slider change volume in logarithmic manner
                //  See https://www.dr-lex.be/info-stuff/volumecontrols.html
                //  and https://ux.stackexchange.com/q/79672/117386
                //  and https://dcordero.me/posts/logarithmic_volume_control.html
                Slider(
                    value = state.volume,
                    onValueChange = { state.volume = it },
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }


}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Speed(
    initialValue: Float,
    modifier: Modifier = Modifier,
    onChange: (Float?) -> Unit
) {
    var input by remember { mutableStateOf(initialValue.toString()) }
    OutlinedTextField(
        value = input,
        modifier = modifier,
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = rememberVectorPainter(Icons.Default.ThumbUp),
                contentDescription = "Speed",
                modifier = Modifier.size(28.dp)
            )
        },
        onValueChange = {
            input = if (it.isEmpty()) {
                it
            } else if (it.toFloatOrNull() == null) {
                input // Old value
            } else {
                it // New value
            }
            onChange(input.toFloatOrNull())
        }
    )
}

@Composable
private fun VideoPlayer(
    url: String,
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    onFinish: (() -> Unit)? = null,
    slug: String
) = VideoPlayerImpl(
    url = url,
    isResumed = state.isResumed,
    volume = state.volume,
    speed = state.speed,
    seek = state.seek,
    isFullscreen = state.isFullscreen,
    progressState = state.iProgress,
    slug = slug,
    modifier = modifier,
    onFinish = onFinish
)

@Composable
expect fun VideoPlayerImpl(
    url: String,
    isResumed: Boolean,
    volume: Float,
    speed: Float,
    seek: Float,
    isFullscreen: Boolean,
    progressState: MutableState<Progress>,
    modifier: Modifier,
    onFinish: (() -> Unit)?,
    slug: String
)

@Composable
fun rememberVideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false
): VideoPlayerState = rememberSaveable(saver = VideoPlayerState.Saver()) {
    VideoPlayerState(
        seek,
        speed,
        volume,
        isResumed,
        isFullscreen,
        Progress(0f, 0)
    )
}

class VideoPlayerState(
    seek: Float = 0f,
    speed: Float = 1f,
    volume: Float = 1f,
    isResumed: Boolean = true,
    isFullscreen: Boolean = false,
    progress: Progress
) {

    var seek by mutableStateOf(seek)
    var speed by mutableStateOf(speed)
    var volume by mutableStateOf(volume)
    var isResumed by mutableStateOf(isResumed)
    var isFullscreen by mutableStateOf(isFullscreen)
    internal val iProgress = mutableStateOf(progress)
    val progress: State<Progress> = iProgress

    fun toggleResume() {
        isResumed = !isResumed
    }

    fun toggleFullscreen() {
        isFullscreen = !isFullscreen
    }

    fun stopPlayback() {
        isResumed = false
    }

    companion object {
        /**
         * The default [Saver] implementation for [VideoPlayerState].
         */
        fun Saver() = listSaver<VideoPlayerState, Any>(
            save = {
                listOf(
                    it.seek,
                    it.speed,
                    it.volume,
                    it.isResumed,
                    it.isFullscreen,
                    it.progress.value
                )
            },
            restore = {
                VideoPlayerState(
                    seek = it[0] as Float,
                    speed = it[1] as Float,
                    volume = it[2] as Float,
                    isResumed = it[3] as Boolean,
                    isFullscreen = it[3] as Boolean,
                    progress = it[4] as Progress,
                )
            }
        )
    }
}