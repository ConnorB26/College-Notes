package com.connorb26.notesapp.feature_note.presentation.add_edit_class

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.BlackTextField
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.BlackTextFieldIcon
import com.connorb26.notesapp.feature_note.presentation.add_edit_class.component.ClassTimeItem
import com.connorb26.notesapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.connorb26.notesapp.feature_note.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddEditClassScreen(
    navController: NavController,
    viewModel: AddEditClassViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val nameState = viewModel.classNameField.value
    val locationState = viewModel.location.value

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                viewModel.onEvent(AddEditClassEvent.SaveClass)
            }
            else -> {}
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditClassViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditClassViewModel.UiEvent.NavigateUp -> {
                    navController.navigate(Screen.ClassScreen.route)
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditClassEvent.CancelClass)
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackTextField(
                    text = nameState.text,
                    hint = "Class Name",
                    onValueChange = {
                        viewModel.onEvent(AddEditClassEvent.EnteredName(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BlackTextFieldIcon(
                    text = locationState.text,
                    hint = "Location",
                    onValueChange = {
                        viewModel.onEvent(AddEditClassEvent.EnteredLocation(it))
                    },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    icon = Icons.Default.MyLocation,
                    iconDesc = "Location"
                )
            }

            ClassTimeItem()

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .weight(1f)
                        .clickable { viewModel.onEvent(AddEditClassEvent.CancelClass) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Cancel",
                        color = MaterialTheme.colors.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .weight(1f)
                        .clickable { viewModel.onEvent(AddEditClassEvent.SaveClass) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Save",
                        color = MaterialTheme.colors.primary
                    )
                }
            }
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