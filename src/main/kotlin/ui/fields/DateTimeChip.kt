package ui.fields

import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import ui.dialogs.DatePickerDialog
import ui.dialogs.TimePickerDialog
import utils.DateTimeConverter
import utils.withDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DateTimeChip(dateTime: LocalDateTime?, label: String, onDateTimeChanged: (LocalDateTime) -> Unit) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    Chip(onClick = {
        showDatePicker = true
        //show date/time picker
    }) {
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
        Text(target)
    }

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