package com.example.fitnesslog.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.withTransaction
import com.example.fitnesslog.core.database.FitnessLogDatabase
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.constants.IS_SEEDED
import com.example.fitnesslog.core.utils.helpers.isSeeded
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.data.dao.ProgramDao
import com.example.fitnesslog.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.data.entity.Program
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.domain.repository.SharedRepository

class SharedRepositoryImpl(
    private val db: FitnessLogDatabase,
    private val programDao: ProgramDao,
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val exerciseDao: ExerciseTemplateDao,
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

                    val defaultWorkoutTemplate1 = WorkoutTemplate(
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
                        workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate1).toInt()
                    workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate2).toInt()
                    workoutTemplateDao.insertWorkoutTemplate(defaultWorkoutTemplate3).toInt()

                    val exerciseTemplate1 = ExerciseTemplate(
                        name = "Bench Press",
                        exerciseMuscle = ExerciseMuscle.CHEST,
                        exerciseResistance = ExerciseResistance.BARBELL,
                        isDefault = true,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val exerciseTemplate2 = ExerciseTemplate(
                        name = "Overhead Press",
                        exerciseMuscle = ExerciseMuscle.SHOULDERS,
                        exerciseResistance = ExerciseResistance.BARBELL,
                        isDefault = true,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val exerciseTemplate3 = ExerciseTemplate(
                        name = "Dumbbell Bench Press",
                        exerciseMuscle = ExerciseMuscle.CHEST,
                        exerciseResistance = ExerciseResistance.DUMBBELL,
                        isDefault = true,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val exerciseTemplate4 = ExerciseTemplate(
                        name = "Lateral Raise",
                        exerciseMuscle = ExerciseMuscle.CHEST,
                        exerciseResistance = ExerciseResistance.DUMBBELL,
                        isDefault = true,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val exerciseTemplateId1 =
                        exerciseDao.insertExerciseTemplate(exerciseTemplate1).toInt()
                    val exerciseTemplateId2 =
                        exerciseDao.insertExerciseTemplate(exerciseTemplate2).toInt()
                    val exerciseTemplateId3 =
                        exerciseDao.insertExerciseTemplate(exerciseTemplate3).toInt()
                    val exerciseTemplateId4 =
                        exerciseDao.insertExerciseTemplate(exerciseTemplate4).toInt()

                    // TODO: Insert Exercise Templates
                    val defaultWorkoutTemplateExercise1 = WorkoutTemplateExercise(
                        workoutTemplateId = workoutTemplateId,
                        exerciseTemplateId = exerciseTemplateId1,
                        position = 0,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val defaultWorkoutTemplateExercise2 = WorkoutTemplateExercise(
                        workoutTemplateId = workoutTemplateId,
                        exerciseTemplateId = exerciseTemplateId2,
                        position = 1,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val defaultWorkoutTemplateExercise3 = WorkoutTemplateExercise(
                        workoutTemplateId = workoutTemplateId,
                        exerciseTemplateId = exerciseTemplateId3,
                        position = 2,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val defaultWorkoutTemplateExercise4 = WorkoutTemplateExercise(
                        workoutTemplateId = workoutTemplateId,
                        exerciseTemplateId = exerciseTemplateId4,
                        position = 3,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    exerciseDao.insertExercisesIntoWorkoutTemplate(
                        listOf(
                            defaultWorkoutTemplateExercise1, defaultWorkoutTemplateExercise2,
                            defaultWorkoutTemplateExercise3, defaultWorkoutTemplateExercise4
                        )
                    )
                    dataStore.edit { settings ->
                        settings[IS_SEEDED] = true
                    }
                }
            }
        }
    }

}