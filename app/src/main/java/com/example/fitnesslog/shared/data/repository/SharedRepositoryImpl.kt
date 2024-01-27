package com.example.fitnesslog.shared.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.withTransaction
import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.core.utils.IS_SEEDED
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.isSeeded
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.program.data.dao.ProgramDao
import com.example.fitnesslog.program.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.program.data.entity.Program
import com.example.fitnesslog.program.data.entity.WorkoutTemplate
import com.example.fitnesslog.shared.domain.repository.SharedRepository

class SharedRepositoryImpl(
    private val db: FitnessLogDatabase,
    private val programDao: ProgramDao,
    private val workoutTemplateDao: WorkoutTemplateDao,
//    private val exerciseDao: ExerciseTemplateDao,
    private val dataStore: DataStore<Preferences>
) : SharedRepository {


    override suspend fun seedDatabaseIfFirstRun(): Resource<Unit> {
        return safeCall {
            db.withTransaction {
                if (!isSeeded()) {
                    val defaultProgram = Program(
                        name = "Sample Push Pull Legs Program",
                        isSelected = true,
                        restDurationSeconds = 90,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )

                    // Runs sequentially because suspend functions
                    val programId = programDao.insertProgram(defaultProgram).toInt()
                    // TODO: Insert Workout with program ID
                    val defaultWorkoutTemplate = WorkoutTemplate(
                        name = "Push Day (Chest Focused)",
                        programId = programId,
                        position = 0,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val defaultWorkoutTemplate2 = WorkoutTemplate(
                        name = "Push Day (Shoulder Focused)",
                        programId = programId,
                        position = 1,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val defaultWorkoutTemplate3 = WorkoutTemplate(
                        name = "Back Day",
                        programId = programId,
                        position = 2,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val workoutTemplateId =
                        workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate).toInt()
                    workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate2).toInt()
                    workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate3).toInt()

                    // TODO: Insert Exercise Templates

                    dataStore.edit { settings ->
                        settings[IS_SEEDED] = true
                    }
                }
            }
        }
    }

}