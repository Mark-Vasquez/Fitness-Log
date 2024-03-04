package com.example.fitnesslog.data.repository

import com.example.fitnesslog.core.enums.ExerciseMuscle
import com.example.fitnesslog.core.enums.ExerciseResistance
import com.example.fitnesslog.core.utils.Resource
import com.example.fitnesslog.core.utils.extensions.toErrorMessage
import com.example.fitnesslog.core.utils.safeCall
import com.example.fitnesslog.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.data.dao.WorkoutTemplateExerciseDao
import com.example.fitnesslog.data.dao.WorkoutTemplateExerciseSetDao
import com.example.fitnesslog.data.entity.ExerciseTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplate
import com.example.fitnesslog.data.entity.WorkoutTemplateExercise
import com.example.fitnesslog.domain.repository.TemplateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class TemplateRepositoryImpl(
    private val exerciseTemplateDao: ExerciseTemplateDao,
    private val workoutTemplateDao: WorkoutTemplateDao,
    private val workoutTemplateExerciseDao: WorkoutTemplateExerciseDao,
    private val workoutTemplateExerciseSetDao: WorkoutTemplateExerciseSetDao
) :
    TemplateRepository {
    // **Workout Template**
    override suspend fun createWorkoutTemplateForProgram(programId: Int): Resource<Long> {
        val lastPosition = workoutTemplateDao.getPositionForInsert(programId)
        val newWorkoutTemplate = WorkoutTemplate(
            name = "New Workout",
            programId = programId,
            position = lastPosition,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return safeCall { workoutTemplateDao.insertWorkoutTemplate(newWorkoutTemplate) }
    }

    override fun getWorkoutTemplateById(workoutTemplateId: Int): Flow<Resource<WorkoutTemplate>> {
        return workoutTemplateDao.getWorkoutTemplateById(workoutTemplateId)
            .map { Resource.Success(it) as Resource<WorkoutTemplate> }
            .catch { e -> emit(Resource.Error(e.toErrorMessage())) }
    }

    override fun getWorkoutTemplatesForProgramOrderedByPosition(programId: Int): Flow<Resource<List<WorkoutTemplate>>> {
        return workoutTemplateDao.getWorkoutTemplatesForProgramOrderedByPosition(programId)
            .map { Resource.Success(it) as Resource<List<WorkoutTemplate>> }
            .catch { e ->
                emit(
                    Resource.Error(e.toErrorMessage())
                )
            }
    }

    override suspend fun updateWorkoutTemplate(workoutTemplate: WorkoutTemplate): Resource<Int> {
        return safeCall { workoutTemplateDao.updateWorkoutTemplate(workoutTemplate) }
    }

    override suspend fun updateWorkoutTemplatePositionsForProgram(
        workoutTemplates: List<WorkoutTemplate>
    ): Resource<Unit> {
        return safeCall {
            workoutTemplateDao.updateAllWorkoutTemplatePositionsForProgram(workoutTemplates)
        }
    }

    override suspend fun deleteWorkoutTemplateAndRearrange(
        workoutTemplateId: Int,
        programId: Int
    ): Resource<Unit> {
        return safeCall {
            workoutTemplateDao.deleteWorkoutTemplateInProgramAndRearrange(
                workoutTemplateId,
                programId
            )
        }
    }

    //**Workout Template Exercise**
    override suspend fun addExercisesToWorkoutTemplate(
        exerciseTemplateIds: List<Int>,
        workoutTemplateId: Int
    ): Resource<Unit> {
        val lastPosition = workoutTemplateExerciseDao.getPositionForInsert(workoutTemplateId)
        val selectedExerciseTemplates = exerciseTemplateDao.getExercisesByIds(exerciseTemplateIds)

        val newWorkoutTemplateExercises =
            selectedExerciseTemplates.mapIndexed { index, exerciseTemplate ->
                WorkoutTemplateExercise(
                    workoutTemplateId = workoutTemplateId,
                    name = exerciseTemplate.name,
                    exerciseResistance = exerciseTemplate.exerciseResistance,
                    position = lastPosition + index,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            }
        return safeCall {
            workoutTemplateExerciseDao.insertWorkoutTemplateExercises(
                newWorkoutTemplateExercises
            )
        }
    }

    override fun getWorkoutTemplateExercisesOrderedByPosition(workoutTemplateId: Int): Flow<Resource<List<WorkoutTemplateExercise>>> {
        return workoutTemplateExerciseDao.getWorkoutTemplateExercisesOrderedByPosition(
            workoutTemplateId
        )
            .map { Resource.Success(it) as Resource<List<WorkoutTemplateExercise>> }
            .catch { emit(Resource.Error(it.toErrorMessage())) }

    }

    override suspend fun updateAllExercisePositionsForWorkoutTemplate(workoutTemplateExercises: List<WorkoutTemplateExercise>): Resource<Unit> {
        return safeCall {
            workoutTemplateExerciseDao.updateAllExercisePositionsForWorkoutTemplate(
                workoutTemplateExercises
            )
        }
    }

    override suspend fun deleteExerciseInWorkoutTemplateAndRearrange(
        workoutTemplateExerciseId: Int,
        workoutTemplateId: Int
    ): Resource<Unit> {
        return safeCall {
            workoutTemplateExerciseDao.deleteExerciseInWorkoutTemplateAndRearrange(
                workoutTemplateExerciseId,
                workoutTemplateId
            )
        }
    }

    // **Exercise Template**
    override suspend fun insertExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Long> {
        return safeCall { exerciseTemplateDao.insertExerciseTemplate(exerciseTemplate) }
    }

    override suspend fun initializeExerciseTemplate(): Resource<Long> {
        val newExerciseTemplate = ExerciseTemplate(
            name = "",
            exerciseMuscle = ExerciseMuscle.CHEST,
            exerciseResistance = ExerciseResistance.BARBELL,
            isDefault = false,
            createdAt = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        return safeCall { exerciseTemplateDao.insertExerciseTemplate(newExerciseTemplate) }
    }

    override fun getAllExercisesOrderedByName(): Flow<Resource<List<ExerciseTemplate>>> {
        return exerciseTemplateDao.getAllExercisesOrderedByName()
            .map { Resource.Success(it) as Resource<List<ExerciseTemplate>> }
            .catch { emit(Resource.Error(it.toErrorMessage())) }
    }

    override suspend fun updateExerciseTemplate(exerciseTemplate: ExerciseTemplate): Resource<Int> {
        return safeCall { exerciseTemplateDao.updateExerciseTemplate(exerciseTemplate) }
    }

    override fun getExerciseTemplateById(exerciseTemplateId: Int): Flow<Resource<ExerciseTemplate>> {
        return exerciseTemplateDao.getExerciseTemplateById(exerciseTemplateId)
            .map { Resource.Success(it) as Resource<ExerciseTemplate> }
            .catch { emit(Resource.Error(it.toErrorMessage())) }
    }

    override suspend fun deleteExerciseTemplate(exerciseTemplateId: Int): Resource<Unit> {
        return safeCall { exerciseTemplateDao.deleteExerciseTemplate(exerciseTemplateId) }
    }

}