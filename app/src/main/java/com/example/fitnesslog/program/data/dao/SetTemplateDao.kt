package com.example.fitnesslog.program.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.fitnesslog.program.data.entity.SetTemplate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@Dao
interface SetTemplateDao {

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
    suspend fun updateAllSetPositionsForExerciseTemplate(
        setTemplates: List<SetTemplate>
    ) {
        var invalidPosition: Int = -1
        setTemplates.forEach { setTemplate ->
            setTemplate.id?.let { updateSetTemplatePosition(it, invalidPosition) }
            invalidPosition--
        }

        setTemplates.forEachIndexed { index, setTemplate ->
            setTemplate.id?.let { updateSetTemplatePosition(it, index) }
        }
    }

    @Query("UPDATE set_template SET position = :newPosition WHERE id = :setTemplateId")
    suspend fun updateSetTemplatePosition(setTemplateId: Int, newPosition: Int)

    // Rearranges after every delete, not save
    @Transaction
    suspend fun deleteSetInExerciseTemplateAndRearrange(
        setTemplateId: Int,
        exerciseTemplateId: Int
    ) {
        deleteSetTemplateById(setTemplateId)

        val remainingSetTemplates =
            getSetsForExerciseTemplateOrderedByPosition(exerciseTemplateId).first()

        if (remainingSetTemplates.isNotEmpty()) {
            updateAllSetPositionsForExerciseTemplate(remainingSetTemplates)
        }

    }

    @Query("DELETE FROM set_template WHERE id = :setTemplateId")
    suspend fun deleteSetTemplateById(setTemplateId: Int)
}