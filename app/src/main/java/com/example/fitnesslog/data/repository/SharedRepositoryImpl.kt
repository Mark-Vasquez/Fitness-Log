package com.example.fitnesslog.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.room.withTransaction
import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.constants.IS_SEEDED
import com.example.fitnesslog.core.utils.helpers.isSeeded
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.data.dao.ProgramDao
import com.example.fitnesslog.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.data.dao.WorkoutTemplateExerciseDao
import com.example.fitnesslog.data.database.FitnessLogDatabase
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.data.entity.Program
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.domain.repository.SharedRepository

class SharedRepositoryImpl(
    private val db: FitnessLogDatabase,
    private val programDao: ProgramDao,
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val workoutTemplateExerciseDao: WorkoutTemplateExerciseDao,
    private val exerciseTemplateDao: ExerciseTemplateDao,
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
                        exerciseMuscle = ExerciseMuscle.SHOULDERS,
                        exerciseResistance = ExerciseResistance.DUMBBELL,
                        isDefault = true,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    val exerciseTemplateId1 =
                        exerciseTemplateDao.insertExerciseTemplate(exerciseTemplate1).toInt()
                    val exerciseTemplateId2 =
                        exerciseTemplateDao.insertExerciseTemplate(exerciseTemplate2).toInt()
                    val exerciseTemplateId3 =
                        exerciseTemplateDao.insertExerciseTemplate(exerciseTemplate3).toInt()
                    val exerciseTemplateId4 =
                        exerciseTemplateDao.insertExerciseTemplate(exerciseTemplate4).toInt()


                    addExercisesToWorkoutTemplate(
                        listOf(
                            exerciseTemplateId1,
                            exerciseTemplateId2,
                            exerciseTemplateId3,
                            exerciseTemplateId4
                        ), workoutTemplateId
                    )

                    dataStore.edit { settings ->
                        settings[IS_SEEDED] = true
                    }
                }
            }
        }
    }

    private suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ) {
        val lastPosition = workoutTemplateExerciseDao.getPositionForInsert(workoutTemplateId)
        val selectedExerciseTemplates = exerciseTemplateDao.getExercisesByIds(exerciseTemplateIds)

        val newWorkoutTemplateExercises =
            selectedExerciseTemplates.mapIndexed { index, exerciseTemplate ->
                WorkoutTemplateExercise(
                    workoutTemplateId = workoutTemplateId,
                    name = exerciseTemplate.name,
                    position = lastPosition + index,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }

        workoutTemplateExerciseDao.insertWorkoutTemplateExercises(
            newWorkoutTemplateExercises
        )

    }

}