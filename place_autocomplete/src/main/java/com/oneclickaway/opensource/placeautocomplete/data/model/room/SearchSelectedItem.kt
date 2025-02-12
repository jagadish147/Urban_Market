package com.oneclickaway.opensource.placeautocomplete.data.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 *@author Burhan ud din ---> item table
 */
@Entity(tableName = "SearchSelectedItem")
data class SearchSelectedItem(
    @PrimaryKey var placeId: String,
    var mainText: String,
    var secondaryText: String,
    var searchCurrentMilliseconds: Long
)