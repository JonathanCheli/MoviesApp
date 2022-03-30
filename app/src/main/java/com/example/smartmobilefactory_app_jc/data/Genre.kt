package com.example.smartmobilefactory_app_jc.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.smartmobilefactory_app_jc.R
import timber.log.Timber

enum class Genre(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
) {


    ACTION(R.drawable.ic_genre_action, R.string.genre_title_action),
    ADVENTURE(R.drawable.ic_genre_adventure, R.string.genre_title_adventure),
    ANIME(R.drawable.ic_genre_anime, R.string.genre_title_anime),
    COMEDY(R.drawable.ic_genre_comedy, R.string.genre_title_comedy),
    CRIME(R.drawable.ic_genre_crime, R.string.genre_title_crime),
    DRAMA(R.drawable.ic_genre_drama, R.string.genre_title_drama),
    ESPIONAGE(R.drawable.ic_genre_espionage, R.string.genre_title_espionage),
    FAMILY(R.drawable.ic_genre_family, R.string.genre_title_family),
    FANTASY(R.drawable.ic_genre_fantasy, R.string.genre_title_fantasy),
    HISTORY(R.drawable.ic_genre_history, R.string.genre_title_history),
    HORROR(R.drawable.ic_genre_horror, R.string.genre_title_horror),
    LEGAL(R.drawable.ic_genre_legal, R.string.genre_title_legal),
    MEDICAL(R.drawable.ic_genre_medical, R.string.genre_title_medical),
    MUSIC(R.drawable.ic_genre_action, R.string.genre_title_music),
    MYSTERY(R.drawable.ic_genre_mystery, R.string.genre_title_mystery),
    ROMANCE(R.drawable.ic_genre_romance, R.string.genre_title_romance),
    SCIENCE_FICTION(R.drawable.ic_genre_science_fiction, R.string.genre_title_science_fiction),
    SPORTS(R.drawable.ic_genre_sports, R.string.genre_title_sports),
    SUPERNATURAL(R.drawable.ic_genre_supernatural, R.string.genre_title_supernatural),
    THRILLER(R.drawable.ic_genre_thriller, R.string.genre_title_thriller),
    WAR(R.drawable.ic_genre_war, R.string.genre_title_war),
    WESTERN(R.drawable.ic_genre_western, R.string.genre_title_western);




    companion object {

        fun fromKeyword(keyword: String?): Genre? {
            return when (keyword?.lowercase()) {
                "action" -> ACTION
                "adventure" -> ADVENTURE
                "anime" -> ANIME
                "comedy" -> COMEDY
                "crime" -> CRIME
                "drama" -> DRAMA
                "espionage" -> ESPIONAGE
                "family" -> FAMILY
                "fantasy" -> FANTASY
                "history" -> HISTORY
                "horror" -> HORROR
                "legal" -> LEGAL
                "medical" -> MEDICAL
                "music" -> MUSIC
                "mystery" -> MYSTERY
                "romance" -> ROMANCE
                "science-fiction" -> SCIENCE_FICTION
                "sports" -> SPORTS
                "supernatural" -> SUPERNATURAL
                "thriller" -> THRILLER
                "war" -> WAR
                "western" -> WESTERN
                else -> {
                    Timber.w("Unknown genre keyword $keyword")
                    null
                }
            }
        }

    }



}
