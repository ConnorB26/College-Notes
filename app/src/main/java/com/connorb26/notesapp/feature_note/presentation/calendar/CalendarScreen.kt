package com.connorb26.notesapp.feature_note.presentation.calendar

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.view.ContextThemeWrapper
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.connorb26.notesapp.R
import com.connorb26.notesapp.feature_note.presentation.calendar.components.EventItem

@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun CalendarScreen(
    navController: NavController,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current

    var date by remember { mutableStateOf("") }
    val dateFormat = SimpleDateFormat("MM/dd/yyyy")
    val calendar = Calendar.getInstance()

    LaunchedEffect(key1 = true) {
        date = dateFormat.format(calendar.time)
    }

    OnLifecycleEvent { _, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                val dateInfo: Map<String, Int> = getDateVals(date)
                viewModel.onEvent(CalendarEvent.UpdateDayEvents(dateInfo["Year"]!!, dateInfo["Month"]!!, dateInfo["Day"]!!))
            }
            else -> { }
        }
    }

    LaunchedEffect(key1 = date) {
        val dateInfo: Map<String, Int> = getDateVals(date)
        viewModel.onEvent(CalendarEvent.UpdateDayEvents(dateInfo["Year"]!!, dateInfo["Month"]!!, dateInfo["Day"]!!))
    }

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colors.secondary)
            ) {
                Spacer(modifier = Modifier.height(1.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(30.dp)
                        )
                    }

                    Text(
                        text = "Calendar",
                        style = MaterialTheme.typography.h5
                    )

                    IconButton(
                        onClick = {
                            viewModel.onEvent(CalendarEvent.AddEvent(calendar.timeInMillis))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add event",
                            tint = MaterialTheme.colors.primary,
                            modifier = Modifier.size(35.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(1.dp))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
                    backgroundColor = MaterialTheme.colors.onSurface
                ) {
                    AndroidView(
                        factory = {
                            CalendarView(ContextThemeWrapper(context, R.style.Theme_NotesCalendar))
                        },
                        update = {
                            it.setOnDateChangeListener { _, year, month, day ->
                                calendar.set(year, month, day,0, 0, 0)
                                date = dateFormat.format(calendar.time)
                            }
                        }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Events On $date")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(state.events) { event ->
                    EventItem(
                        event = event,
                        onEditClick = {
                            viewModel.onEvent(CalendarEvent.EditEvent(event.id))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if(state.events.isEmpty()) {
                Text(text = "No events for this date")
            }
        }
    }
}

private fun getDateVals(date: String): Map<String, Int> {
    val year: Int = date.substring(6).toInt()
    val month: Int = date.substring(0,2).toInt()-1
    val day: Int = date.substring(3,5).toInt()
    return mapOf("Year" to year, "Month" to month, "Day" to day)
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