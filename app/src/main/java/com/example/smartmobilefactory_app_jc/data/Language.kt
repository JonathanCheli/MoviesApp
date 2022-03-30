package com.example.smartmobilefactory_app_jc.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.smartmobilefactory_app_jc.R
import timber.log.Timber

enum class Language(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {

    CHINESE(R.drawable.lang_chinese, R.string.language_chinese),
    DUTCH(R.drawable.lang_dutch, R.string.language_dutch),
    ENGLISH(R.drawable.lang_english, R.string.language_english),
    FRENCH(R.drawable.lang_french, R.string.language_french),
    GERMAN(R.drawable.lang_german, R.string.language_german),
    JAPANESE(R.drawable.lang_japanese, R.string.language_japanese),
    KOREAN(R.drawable.lang_korean, R.string.language_korean),
    POLISH(R.drawable.lang_polish, R.string.language_polish),
    PORTUGUESE(R.drawable.lang_portuguese, R.string.language_portuguese),
    RUSSIAN(R.drawable.lang_russian, R.string.language_russian),
    SPANISH(R.drawable.lang_spanish, R.string.language_spanish),
    SWEDISH(R.drawable.lang_swedish, R.string.language_swedish),
    TURKISH(R.drawable.lang_turkish, R.string.language_turkish),
    UKRAINIAN(R.drawable.lang_ukrainian, R.string.language_ukrainian),
    OTHER(R.drawable.lang_other, R.string.language_other);

    companion object {

        fun fromKeyword(keyword: String?): Language {
            return when (keyword?.lowercase()) {
                "english" -> ENGLISH
                "japanese" -> JAPANESE
                "portuguese" -> PORTUGUESE
                "spanish" -> SPANISH
                "french" -> FRENCH
                "russian" -> RUSSIAN
                "turkish" -> TURKISH
                "korean" -> KOREAN
                "chinese" -> CHINESE
                "dutch" -> DUTCH
                "swedish" -> SWEDISH
                "german" -> GERMAN
                "ukrainian" -> UKRAINIAN
                "polish" -> POLISH
                else -> {
                    Timber.w("Unknown language keyword $keyword")
                    OTHER
                }
            }
        }

    }

}
