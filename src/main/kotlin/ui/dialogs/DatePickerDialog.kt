package ui.dialogs

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.akhris.domain.core.utils.log
import ui.theme.AppTheme
import ui.theme.DialogSettings
import utils.DateTimeConverter
import utils.getDates
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.WeekFields
import java.util.*


/**
 * Material date picker dialog as described here:
 * https://material.io/components/date-pickers#specs
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DatePickerDialog(onDismiss: () -> Unit) {
    AlertDialog(
//        modifier = Modifier.size(
//            width = DialogSettings.DatePickerSettings.defaultPickerWidth,
//            height = DialogSettings.DatePickerSettings.defaultPickerHeight
//        ),
        onDismissRequest = onDismiss,
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        text = {
            DatePickerDialogContent(onDayClick = {

            })
        },
        buttons = {}
    )
}


@Composable
fun DatePickerDialogContent(onDayClick: ((LocalDate) -> Unit)? = null) {

    var yearMonth by remember { mutableStateOf(YearMonth.now()) }


    Column(modifier = Modifier.size(
        width = DialogSettings.DatePickerSettings.defaultPickerWidth,
        height = DialogSettings.DatePickerSettings.defaultPickerHeight
    )) {
        //title
        Box(
            modifier = Modifier.height(DialogSettings.DatePickerSettings.defaultPickerTitleHeight)
                .width(DialogSettings.DatePickerSettings.defaultPickerWidth)
                .background(color = MaterialTheme.colors.primarySurface)
        ) {

        }

        //main content
        Box(modifier = Modifier.weight(1f).width(DialogSettings.DatePickerSettings.defaultPickerWidth)) {
            MainContent(
                yearMonth = yearMonth,
                onYearMonthChange = {
                    yearMonth = it
                },
                onDayClick = onDayClick
            )
        }
    }

}

/**
 * Main content of date picker:
 * yearmonth dropdown and previous/next buttons at the top
 * month view at the bottom
 */
@Composable
private fun MainContent(
    yearMonth: YearMonth,
    onYearMonthChange: (YearMonth) -> Unit,
    onDayClick: ((LocalDate) -> Unit)?
) {


    Column {
        Row {
            TextField(value = DateTimeConverter.MMMMyyyy.format(yearMonth), onValueChange = {
                try {
                    YearMonth.parse(it, DateTimeConverter.MMMMyyyy)
                } catch (e: Exception) {
                    log(e.localizedMessage)
                    null
                }?.let { ym ->
                    onYearMonthChange(ym)
                }
            })

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = {
                onYearMonthChange(yearMonth.minusMonths(1L))
            }) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowLeft, contentDescription = "previous month")
            }

            IconButton(onClick = {
                onYearMonthChange(yearMonth.plusMonths(1L))
            }) {
                Icon(imageVector = Icons.Rounded.KeyboardArrowRight, contentDescription = "next month")
            }
        }


        MonthContent(yearMonth, onDayClick)

    }
}

@Composable
private fun MonthContent(ym: YearMonth, onDayClick: ((LocalDate) -> Unit)?) {
    val datesLines: List<List<LocalDate?>> =
        remember(ym) { ym.getDates(withPreviousNextDays = false).windowed(size = 7, step = 7) }
    Column {
        HeaderRow()
        datesLines.forEach { weekLine ->
            WeekRow(dates = weekLine, onDayClick = onDayClick)
        }
    }
}


@Composable
fun HeaderRow() {
    val firstDayOfWeek: DayOfWeek = remember { WeekFields.of(Locale.getDefault()).firstDayOfWeek }
    Row(horizontalArrangement = Arrangement.Center) {
        for (i in 0 until 7) {
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(8.dp),
                    text = DateTimeConverter.dayOfWeekShort.format(firstDayOfWeek.plus(i.toLong()))
                        .lowercase(),
                    style = MaterialTheme.typography.overline
                )
            }
        }
    }
}

@Composable
fun WeekRow(
    dates: List<LocalDate?>,
    onDayClick: ((LocalDate) -> Unit)? = null
) {
    Row(horizontalArrangement = Arrangement.Center) {
        dates.forEach { date ->
            Box(modifier = Modifier
                .weight(1f)
                .clickable { date?.let { onDayClick?.invoke(it) } }) {
                if (date != null) {
                    DayCell(date)
                }
            }

        }
    }
}

@Composable
fun BoxScope.DayCell(
    date: LocalDate
) {
    Text(
        modifier = Modifier
            .align(Alignment.Center)
            .padding(8.dp),
        text = date.dayOfMonth.toString(),
        fontWeight = if (date == LocalDate.now()) {
            FontWeight.Bold
        } else null
    )
}

@Preview
@Composable
fun DatePickerTest_light() {
    AppTheme(darkTheme = false) {
        DatePickerDialogContent()
    }
}

@Preview
@Composable
fun DatePickerTest_dark() {
    AppTheme(darkTheme = true) {
        DatePickerDialogContent()
    }
}