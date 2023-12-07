package ui.fields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import ui.dialogs.DatePickerDialog
import ui.dialogs.TimePickerDialog
import utils.DateTimeConverter
import utils.withDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun DateTimeChip(
    dateTime: LocalDateTime?,
    label: String,
    isEditable: Boolean,
    onDateTimeChanged: (LocalDateTime?) -> Unit,
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isHovered by remember { mutableStateOf(false) }

    Chip(modifier = Modifier
        .onPointerEvent(PointerEventType.Enter) { isHovered = true }
        .onPointerEvent(PointerEventType.Exit) { isHovered = false },
        onClick = {
            showDatePicker = true
            //show date/time picker
        }, content = {
            val target = remember(dateTime) {
                dateTime?.let {
                    if (it.toLocalTime().run {
                            hour == 0 && minute == 0 && second == 0
                        })
                        it.format(DateTimeConverter.dateFormatter)
                    else
                        DateTimeConverter.dateTimeToString(it)
                } ?: label
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(target)
                if (isEditable && dateTime != null && isHovered) {
                    Icon(
                        modifier = Modifier.clickable { onDateTimeChanged(null) },
                        imageVector = Icons.Rounded.Clear,
                        contentDescription = "remove date/time"
                    )
                }
            }
        }
    )

    //show target date picker
    if (showDatePicker) {
        DatePickerDialog(
            initialSelection = dateTime?.toLocalDate(),
            onDismiss = {
                showDatePicker = false
                showTimePicker = true
            },
            onDateSelected = { newDate ->
                onDateTimeChanged(dateTime?.withDate(newDate) ?: newDate.atTime(0, 0, 0))
            }

        )
    }

    if (showTimePicker) {
        TimePickerDialog(
            initialTime = dateTime,
            onDismiss = { showTimePicker = false },
            onTimeSelected = { newTime ->
                onDateTimeChanged(newTime)
            }
        )
    }
}