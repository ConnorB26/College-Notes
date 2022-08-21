package com.connorb26.notesapp.feature_note.presentation.add_edit_note

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.domain.model.Note
import com.connorb26.notesapp.feature_note.presentation.util.VariableColor
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.ColorPickerDialog
import com.connorb26.notesapp.feature_note.presentation.util.CustomTextField
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import com.connorb26.notesapp.ui.theme.DarkGray
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNoteViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val colorSection = remember { mutableStateOf(false) }
    val colorPickerEnabled = remember { mutableStateOf(false) }
    val complementColor = viewModel.complementColor
    val defaultColor = rememberSaveable { mutableStateOf(noteColor) }

    val noteBackgroundAnimate = remember {
        Animatable(
            Color(if(noteColor != -1) defaultColor.value else viewModel.noteColor.value)
        )
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                viewModel.onEvent(AddEditNoteEvent.SaveNote)
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditNoteViewModel.UiEvent.NavigateUp -> {
                    navController.navigate(Screen.NotesScreen.route)
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditNoteEvent.SaveNoteAndNavigate)
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimate.value)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.onEvent(AddEditNoteEvent.Navigate)
                    },
                    modifier = Modifier.offset((-15).dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(complementColor.value),
                        modifier = Modifier.size(30.dp)
                    )
                }
                CustomTextField(
                    text = titleState.text,
                    hint = titleState.hint,
                    onValueChange = {
                        viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(0.8f),
                    selectionColor = Color(complementColor.value),
                    textColor = Color(complementColor.value)
                )

                IconButton(
                    onClick = {
                        colorSection.value = !colorSection.value
                    },
                    modifier = Modifier.offset(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Palette,
                        contentDescription = "Background color",
                        tint = Color(complementColor.value),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            AnimatedVisibility(
                visible = colorSection.value,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Note.noteColors.forEach { color ->
                        val colorInt = color.toArgb()
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .shadow(9.dp, CircleShape)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = 2.dp,
                                    color = if (viewModel.noteColor.value == colorInt) {
                                        Color(
                                            ColorUtils.blendARGB(
                                                viewModel.noteColor.value,
                                                Color.Black.toArgb(),
                                                0.5f
                                            )
                                        )
                                    } else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    defaultColor.value = colorInt
                                    scope.launch {
                                        noteBackgroundAnimate.animateTo(
                                            targetValue = Color(colorInt),
                                            animationSpec = tween(
                                                durationMillis = 500
                                            )
                                        )
                                    }
                                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                                }
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(30.dp)
                            .shadow(9.dp, CircleShape)
                            .clip(CircleShape)
                            .background(
                                if (!Note.noteColors.contains(Color(viewModel.noteColor.value))) {
                                    Color(viewModel.noteColor.value)
                                } else DarkGray
                            )
                            .border(
                                width = 2.dp,
                                color = if (!Note.noteColors.contains(Color(viewModel.noteColor.value))) {
                                    Color(
                                        ColorUtils.blendARGB(
                                            viewModel.noteColor.value,
                                            Color.Black.toArgb(),
                                            0.5f
                                        )
                                    )
                                } else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                colorPickerEnabled.value = true
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Custom Color",
                            tint = if(!Note.noteColors.contains(Color(viewModel.noteColor.value)))
                                Color(complementColor.value) else Color.White
                        )
                    }
                }
            }

            if(colorPickerEnabled.value) {
                ColorPickerDialog(
                    onSave = {
                        val colorInt = it.toArgb()
                        colorPickerEnabled.value = false
                        defaultColor.value = colorInt
                        scope.launch {
                            noteBackgroundAnimate.animateTo(
                                targetValue = Color(colorInt),
                                animationSpec = tween(
                                    durationMillis = 500
                                )
                            )
                        }
                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                    },
                    onDismiss = { colorPickerEnabled.value = false }
                )
            }

            CustomTextField(
                text = contentState.text,
                hint = contentState.hint,
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxHeight(),
                selectionColor = VariableColor.getColor(Color(viewModel.noteColor.value)),
                textColor = VariableColor.getColor(Color(viewModel.noteColor.value))
            )
        }
    }
}

@Composable
private fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {
    val eventHandler = rememberUpdatedState(onEvent)
    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { owner, event ->
            eventHandler.value(owner, event)
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}