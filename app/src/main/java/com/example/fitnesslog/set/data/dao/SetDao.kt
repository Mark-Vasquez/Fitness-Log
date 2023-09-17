package com.example.fitnesslog.set.data.dao

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.set.data.model.SetTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

interface SetDao {

    // **SetTemplate**
    @Insert
    suspend fun insertSetTemplate(setTemplate: SetTemplate): Long

    @Query("SELECT COUNT(*) FROM set_template WHERE exercise_template_id = :exerciseTemplateId")
    suspend fun getPositionForInsert(exerciseTemplateId: Int): Int

    @Query("SELECT * FROM set_template WHERE exercise_template_id = :exerciseTemplateId ORDER BY position")
    fun getSetsForExerciseTemplateOrderedByPosition(exerciseTemplateId: Int): Flow<List<SetTemplate>>

    @Update
    suspend fun updateSetTemplate(setTemplate: SetTemplate)

    @Transaction
    suspend fun updateSetTemplatePositions(
        setTemplates: List<SetTemplate>,
        exerciseTemplateId: Int
    ) {
        var invalidPosition: Int = -1
        setTemplates.forEach { setTemplate ->
            if (setTemplate.exerciseTemplateId == exerciseTemplateId) {
                setTemplate.id?.let { updateSetTemplatePosition(it, invalidPosition) }
            }
            invalidPosition--
        }

        setTemplates.forEachIndexed { index, setTemplate ->
            setTemplate.id?.let { updateSetTemplatePosition(it, index) }
        }
    }

    @Query("UPDATE set_template SET position = :newPosition WHERE id = :setTemplateId")
    suspend fun updateSetTemplatePosition(setTemplateId: Int, newPosition: Int)

    // Rearranges after every save
    @Transaction
    suspend fun deleteSetTemplatesAndRearrange(
        setTemplates: List<SetTemplate>,
        exerciseTemplateId: Int
    ) {
        setTemplates.forEach { it.id?.let { setTemplateId -> deleteSetTemplateById(setTemplateId) } }

        val remainingSetTemplates =
            getSetsForExerciseTemplateOrderedByPosition(exerciseTemplateId).firstOrNull()
                ?: listOf()

        if (remainingSetTemplates.isNotEmpty()) {
            updateSetTemplatePositions(remainingSetTemplates, exerciseTemplateId)
        }


    }

    @Query("DELETE FROM set_template WHERE id = :setTemplateId")
    suspend fun deleteSetTemplateById(setTemplateId: Int)
}