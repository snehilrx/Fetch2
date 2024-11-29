package screens.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.TablerIcons
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.Settings
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.calender
import fetch2.composeapp.generated.resources.settings
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

object SettingsTab : Tab {
    @Composable
    override fun Content() {

    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.settings)
            val icon = rememberVectorPainter(TablerIcons.Settings)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}