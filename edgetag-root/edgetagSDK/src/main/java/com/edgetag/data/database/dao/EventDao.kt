package com.edgetag.data.database.dao

import androidx.room.*
import com.edgetag.data.database.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: EventEntity) : Long

    @Query("SELECT * from events")
    fun getEvents(): Flow<List<EventEntity>>

    @Delete
    fun deleteEvent(event: EventEntity)
}
