package screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import compose.icons.TablerIcons
import compose.icons.tablericons.Calendar
import compose.icons.tablericons.Search
import fetch2.composeapp.generated.resources.Res
import fetch2.composeapp.generated.resources.calender
import fetch2.composeapp.generated.resources.search
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

object SearchTab : Tab {
    @Composable
    override fun Content() {
        Box (contentAlignment = Alignment.Center) {
            SearchBar {

            }
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun SearchBar(
        modifier: Modifier = Modifier,
        onSearch: (String) -> Unit
    ) {
        var textState by remember { mutableStateOf(TextFieldValue("")) }

        Row(
            modifier = modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Search...") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.surface
                ),
                leadingIcon = {
                    IconButton(
                        onClick = { onSearch(textState.text) }
                    ) {
                        Icon(
                            imageVector = TablerIcons.Search,
                            contentDescription = stringResource(Res.string.search),
                            tint = MaterialTheme.colors.primary
                        )
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalResourceApi::class)
    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(Res.string.search)
            val icon = rememberVectorPainter(TablerIcons.Search)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }
}