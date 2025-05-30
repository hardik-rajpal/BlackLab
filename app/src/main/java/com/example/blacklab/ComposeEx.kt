package com.example.blacklab

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import com.example.blacklab.ui.theme.BlackLabTheme
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class ComposeEx : ComponentActivity() {
    val topSingles: List<Song> =
        listOf(
            Song("Take it easy", "FMA3lIeqV8M"),
            Song("These Days", "bc81nqDXzVs"),
            Song("Late for the sky", "n3SJz9jujEA")
        )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackLabTheme {
                Scaffold { paddingValues ->
                    JacksonBrowneWiki(paddingValues)
                }
            }

        }
    }

    @Composable
    private fun JacksonBrowneWiki(paddingValues: PaddingValues) {
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(10.dp)
                .fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(shape = MaterialTheme.shapes.extraSmall, shadowElevation = 1.dp) {
                Text(
                    "Jackson Browne",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            AvatarImage(R.drawable.jackson_browne, "Jackson Browne Headshot")
            TopSingles(topSingles)
        }
    }

    @Composable
    private fun Accordion(title: String, content: @Composable () -> Unit) {
        val viewModel = remember {
                AccordionViewModel()
        }
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            shadowElevation = 1.dp,
            modifier = Modifier.animateContentSize()
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = title, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = { viewModel.toggle() }) {
                        if (viewModel.getIsExpanded()) {
                            Icon(Icons.Default.KeyboardArrowUp, "")
                        } else {
                            Icon(Icons.Default.KeyboardArrowDown, "")
                        }
                    }
                }
                if(isSystemInDarkTheme()){
                    print("We're in the dark theme.")
                }
                if (viewModel.getIsExpanded()) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        content()
                    }
                }
            }
        }
    }

    @Composable
    private fun TopSingles(songs: List<Song>) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Accordion(title = "Top Singles") {
                LazyColumn() {
                    items(songs) { song ->
                        SongView(song)
                    }
                }
            }
        }
    }

    private @Composable
    fun SongView(song: Song) {

        Accordion(title = song.title) {
            YoutubeScreen(videoId = song.videoId)
        }

    }

    class Song(val title: String, public val videoId: String) {

    }

    @Composable
    private fun AvatarImage(drawableId: Int, description: String = "") {
        Image(
            painter = painterResource(drawableId),
            contentDescription = description,
            modifier = Modifier
                .clip(
                    CircleShape
                )
                .size(100.dp)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
        )
    }

    @Composable
    fun YoutubeScreen(
        videoId: String,
        modifier: Modifier? = null
    ) {
        val ctx = LocalContext.current
        AndroidView(factory = {
            var view = YouTubePlayerView(it)
            val fragment = view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            )
            view
        })
    }
}
class AccordionViewModel:ViewModel(){
    private var isExpanded = mutableStateOf(false)
     fun  getIsExpanded():Boolean{
        return isExpanded.value
    }
    fun  toggle(){
        isExpanded.value = !isExpanded.value
    }


}