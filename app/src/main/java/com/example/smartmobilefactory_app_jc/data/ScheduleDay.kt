package com.example.smartmobilefactory_app_jc.data

enum class ScheduleDay {

    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    companion object {

        fun fromDay(day: String?): ScheduleDay {
            return when (day?.lowercase()) {
                "monday" -> MONDAY
                "tuesday" -> TUESDAY
                "wednesday" -> WEDNESDAY
                "thursday" -> THURSDAY
                "friday" -> FRIDAY
                "saturday" -> SATURDAY
                "sunday" -> SUNDAY
                else -> throw IllegalArgumentException("Invalid day $day, are you using french revolutionary calendar?")
            }
        }

    }

}
