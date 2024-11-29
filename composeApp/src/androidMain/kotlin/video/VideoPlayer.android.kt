package video

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier

@Composable
actual fun VideoPlayerImpl(
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
) {
}