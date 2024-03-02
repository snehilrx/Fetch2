import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import api.KickassAnimeApi
import kotlinx.coroutines.launch
import model.RecentApiResponse
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        val (frontPageList, setFrontPageList) = remember { mutableStateOf<RecentApiResponse?>(null) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = { coroutineScope.launch {
                setFrontPageList(KickassAnimeApi.getFrontPageAnimeList(0))
            } }) {
                Text("Click me!")
            }
            frontPageList?.result?.let {
                LazyColumn {
                    items(it) { recent ->
                        Text(recent.title ?: "")
                    }
                }
            }
        }
    }
}