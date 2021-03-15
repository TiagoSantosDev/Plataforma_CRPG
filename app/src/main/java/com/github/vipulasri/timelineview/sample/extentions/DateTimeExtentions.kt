package com.github.vipulasri.timelineview.sample.extentions

import org.threeten.bp.LocalDate
//import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

fun String.formatDateTime(originalFormat: String, outputFormat: String): String {
    val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(originalFormat, Locale.ENGLISH))
    //val date = LocalDate.parse(this, DateTimeFormatter.ofPattern(originalFormat, Locale.ENGLISH))
    return date.format(DateTimeFormatter.ofPattern(outputFormat, Locale.ENGLISH))
}