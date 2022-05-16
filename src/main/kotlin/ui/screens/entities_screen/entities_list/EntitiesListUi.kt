package ui.screens.entities_screen.entities_list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.entities.IEntity
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import kotlinx.coroutines.delay
import ui.entity_renderers.EntityScreenContent
import ui.screens.entities_screen.entities_view_settings.ItemRepresentationType
import kotlin.math.sign

@Composable
fun <T : IEntity<*>> EntitiesListUi(component: IEntitiesList<T>, itemRepresentationType: ItemRepresentationType) {

    val state by remember(component) { component.state }.subscribeAsState()

    val listState = rememberLazyListState()

    var scrollToItem by remember { mutableStateOf<Int?>(null) }

    var bottomPadding by remember { mutableStateOf(0.dp) }


    Box(modifier = Modifier.fillMaxHeight()) {

        EntityScreenContent(
            itemRepresentationType = itemRepresentationType,
            items = state.entities,
            listState = listState,
            bottomPadding = bottomPadding,
            onEntityRemoved = component.onEntityRemovedCallback?.let { rc -> { rc(it) } },
            onEntityUpdated = component::onEntityUpdated,
            onEntityCopied = component.onEntityCopiedCallback?.let { cc -> { cc(it) } }
        )

        Column(
            modifier = Modifier
                .onSizeChanged {
                    bottomPadding = it.height.dp
                }
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (listState.firstVisibleItemIndex > 0) {
                //show scroll up button
                ScrollUpButton(
                    onScrollUpClicked = {
                        scrollToItem = 0
                    }
                )
            }
            state.pagingParameters?.let { pagingParams ->
                if (pagingParams.totalPages > 1) {
                    //show paging control:
                    PagingControl(
                        currentPage = pagingParams.currentPage,
                        maxPages = pagingParams.totalPages,
                        onPageChanged = {
                            component.setCurrentPage(it)
                        })
                }
            }


        }


    }


    LaunchedEffect(scrollToItem) {
        scrollToItem?.let {
            listState.animateScrollToItem(it)
            scrollToItem = null
        }
    }

}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun ScrollUpButton(
    modifier: Modifier = Modifier,
    onScrollUpClicked: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    val alpha by animateFloatAsState(
        when (isHovered) {
            true -> 1f
            false -> 0.1f
        }
    )

    ExtendedFloatingActionButton(modifier = modifier
        .alpha(alpha)
        .onPointerEvent(PointerEventType.Enter) { isHovered = true }
        .onPointerEvent(PointerEventType.Exit) { isHovered = false },
        text = { Text("scroll to the top") },
        onClick = onScrollUpClicked,
        icon = { Icon(imageVector = Icons.Rounded.KeyboardArrowUp, contentDescription = "scroll to the top") })

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun PagingControl(
    modifier: Modifier = Modifier,
    currentPage: Long = 1L,
    maxPages: Long,
    onPageChanged: (Long) -> Unit
) {

    var isPageControllerHover by remember { mutableStateOf(false) }

    val pagesRange = remember(maxPages) { 1L..maxPages }

    var page by remember { mutableStateOf(currentPage) }


    val bgAlpha by animateFloatAsState(
        when (isPageControllerHover) {
            true -> 1f
            false -> 0.1f
        }
    )

    Surface(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) { isPageControllerHover = true }
            .onPointerEvent(PointerEventType.Exit) { isPageControllerHover = false },
        shape = RoundedCornerShape(size = 24.dp),
        color = MaterialTheme.colors.surface.copy(alpha = bgAlpha),
        elevation = when (isPageControllerHover) {
            true -> 1.dp
            false -> 0.dp
        }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            var isPageNumberHover by remember { mutableStateOf(false) }


            IconButton(
                modifier = Modifier.alpha(bgAlpha),
                onClick = {
                    page = (page - 1).coerceIn(pagesRange)
                }, enabled = page > 1, content = {
                    Icon(Icons.Rounded.KeyboardArrowLeft, contentDescription = "previous page")
                })

            BasicTextField(
                modifier = Modifier
                    .alpha(bgAlpha)
                    .onPointerEvent(PointerEventType.Enter) { isPageNumberHover = true }
                    .onPointerEvent(PointerEventType.Exit) { isPageNumberHover = false }
                    .border(
                        width = 2.dp,
                        color = if (isPageNumberHover) MaterialTheme.colors.primary else Color.Unspecified,
                        shape = MaterialTheme.shapes.small
                    ).pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                if (event.type == PointerEventType.Scroll) {
                                    val delta = event.changes.first().scrollDelta
                                    page = (page - sign(delta.y).toInt()).coerceIn(pagesRange)
                                    event.changes.first().consume()
                                }
                            }
                        }
                    }
                    .padding(vertical = 8.dp),
                value = page.toString(),
                textStyle = TextStyle.Default.copy(textAlign = TextAlign.Center),
                onValueChange = {
                    it.toLongOrNull()?.let { v ->
                        page = v.coerceIn(pagesRange)
                    }
                },
                visualTransformation = { text ->
                    TransformedText(
                        AnnotatedString("${text.text} of $maxPages"),
                        OffsetMapping.Identity
                    )
                })

            IconButton(
                modifier = Modifier.alpha(bgAlpha),
                onClick = {
                    page = (page + 1).coerceIn(pagesRange)
                }, enabled = page < maxPages, content = {
                    Icon(Icons.Rounded.KeyboardArrowRight, contentDescription = "next page")
                })
        }

        //callback with debounce:
        LaunchedEffect(page) {
            if (page == currentPage) {
                return@LaunchedEffect
            }
            //when count changes:
            delay(500L)
            onPageChanged(page)
        }
    }
}

