package com.example.fitnesslog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.fitnesslog.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.data.dao.ProgramDao
import com.example.fitnesslog.data.dao.WorkoutTemplateDao
import com.example.fitnesslog.data.dao.WorkoutTemplateExerciseDao
import com.example.fitnesslog.data.dao.WorkoutTemplateExerciseSetDao
import com.example.fitnesslog.data.database.FitnessLogDatabase
import com.example.fitnesslog.data.repository.ProgramRepositoryImpl
import com.example.fitnesslog.data.repository.SharedRepositoryImpl
import com.example.fitnesslog.data.repository.TemplateRepositoryImpl
import com.example.fitnesslog.domain.repository.ProgramRepository
import com.example.fitnesslog.domain.repository.SharedRepository
import com.example.fitnesslog.domain.repository.TemplateRepository
import com.example.fitnesslog.domain.use_case.exercise_template.CreateExerciseTemplate
import com.example.fitnesslog.domain.use_case.exercise_template.DeleteExerciseTemplate
import com.example.fitnesslog.domain.use_case.exercise_template.DeleteExerciseTemplates
import com.example.fitnesslog.domain.use_case.exercise_template.DiscardInitializedTemplate
import com.example.fitnesslog.domain.use_case.exercise_template.EditExerciseTemplate
import com.example.fitnesslog.domain.use_case.exercise_template.ExerciseTemplateUseCases
import com.example.fitnesslog.domain.use_case.exercise_template.GetExerciseTemplateById
import com.example.fitnesslog.domain.use_case.exercise_template.GetExerciseTemplates
import com.example.fitnesslog.domain.use_case.exercise_template.InitializeExerciseTemplate
import com.example.fitnesslog.domain.use_case.program.CheckIfDeletable
import com.example.fitnesslog.domain.use_case.program.DeleteProgram
import com.example.fitnesslog.domain.use_case.program.EditProgram
import com.example.fitnesslog.domain.use_case.program.GetProgram
import com.example.fitnesslog.domain.use_case.program.GetPrograms
import com.example.fitnesslog.domain.use_case.program.InitializeProgram
import com.example.fitnesslog.domain.use_case.program.ProgramUseCases
import com.example.fitnesslog.domain.use_case.program.SelectProgram
import com.example.fitnesslog.domain.use_case.shared.GetSelectedProgram
import com.example.fitnesslog.domain.use_case.shared.SeedInitialApplication
import com.example.fitnesslog.domain.use_case.shared.SharedUseCases
import com.example.fitnesslog.domain.use_case.workout_template.AddExercisesToWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.CreateWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.DeleteExerciseFromWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.DeleteWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.EditWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.GetExercisesForWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.GetWorkoutTemplates
import com.example.fitnesslog.domain.use_case.workout_template.ReorderExercisesForWorkoutTemplate
import com.example.fitnesslog.domain.use_case.workout_template.ReorderWorkoutTemplates
import com.example.fitnesslog.domain.use_case.workout_template.WorkoutTemplateUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


// The interface declares the contracts for the dependencies that will be provided by this module.
interface AppModule {
    val db: FitnessLogDatabase
    val dataStore: DataStore<Preferences>
    val programUseCases: ProgramUseCases
    val exerciseTemplateUseCases: ExerciseTemplateUseCases
    val workoutTemplateUseCases: WorkoutTemplateUseCases
    val sharedUseCases: SharedUseCases
}

private const val USER_SETTINGS = "user_settings"

/**
 * Provides single instances of dependencies for application wide injection.
 * When instantiated, it is injected with an appContext dependency to construct and supply
 * db and dataStore as singletons.
 */
class AppModuleImpl(
    private val appContext: Context
) : AppModule {

    override val db: FitnessLogDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            FitnessLogDatabase::class.java,
            FitnessLogDatabase.DATABASE_NAME
        ).createFromAsset("database/fitness_log.db")
            .build()
    }

    override val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(
            produceNewData = { emptyPreferences() }
        ),
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        produceFile = { appContext.preferencesDataStoreFile(USER_SETTINGS) }
    )

    override val programUseCases: ProgramUseCases by lazy {
        ProgramUseCases(
            initializeProgram = InitializeProgram(programRepository),
            getPrograms = GetPrograms(programRepository),
            getProgram = GetProgram(programRepository),
            editProgram = EditProgram(programRepository),
            deleteProgram = DeleteProgram(programRepository),
            selectProgram = SelectProgram(programRepository),
            checkIfDeletable = CheckIfDeletable(programRepository)
        )
    }

    override val exerciseTemplateUseCases: ExerciseTemplateUseCases by lazy {
        ExerciseTemplateUseCases(
            createExerciseTemplate = CreateExerciseTemplate(templateRepository),
            getExerciseTemplates = GetExerciseTemplates(templateRepository),
            editExerciseTemplate = EditExerciseTemplate(templateRepository),
            getExerciseTemplateById = GetExerciseTemplateById(templateRepository),
            initializeExerciseTemplate = InitializeExerciseTemplate(templateRepository),
            discardInitializedTemplate = DiscardInitializedTemplate(templateRepository),
            deleteExerciseTemplate = DeleteExerciseTemplate(templateRepository),
            deleteExerciseTemplates = DeleteExerciseTemplates(templateRepository)
        )
    }

    override val workoutTemplateUseCases: WorkoutTemplateUseCases by lazy {
        WorkoutTemplateUseCases(
            createWorkoutTemplate = CreateWorkoutTemplate(templateRepository),
            getWorkoutTemplate = GetWorkoutTemplate(templateRepository),
            getWorkoutTemplates = GetWorkoutTemplates(templateRepository),
            editWorkoutTemplate = EditWorkoutTemplate(templateRepository),
            reorderWorkoutTemplates = ReorderWorkoutTemplates(templateRepository),
            deleteWorkoutTemplate = DeleteWorkoutTemplate(templateRepository),
            addExercisesToWorkoutTemplate = AddExercisesToWorkoutTemplate(templateRepository),
            getExercisesForWorkoutTemplate = GetExercisesForWorkoutTemplate(templateRepository),
            reorderExercisesForWorkoutTemplate = ReorderExercisesForWorkoutTemplate(
                templateRepository
            ),
            deleteExerciseFromWorkoutTemplate = DeleteExerciseFromWorkoutTemplate(templateRepository)
        )
    }

    override val sharedUseCases: SharedUseCases by lazy {
        SharedUseCases(
            getSelectedProgram = GetSelectedProgram(programRepository),
            seedInitialApplication = SeedInitialApplication(sharedRepository)
        )
    }

    private val templateRepository: TemplateRepository by lazy {
        TemplateRepositoryImpl(
            exerciseTemplateDao,
            workoutTemplateDao,
            workoutTemplateExerciseDao,
            workoutTemplateExerciseSetDao
        )
    }
    private val sharedRepository: SharedRepository by lazy {
        SharedRepositoryImpl(
            db,
            programDao,
            workoutTemplateDao,
            workoutTemplateExerciseDao,
            exerciseTemplateDao,
            dataStore
        )
    }

    private val programDao: ProgramDao by lazy {
        db.programDao()
    }
    private val exerciseTemplateDao: ExerciseTemplateDao by lazy {
        db.exerciseTemplateDao()
    }
    private val workoutTemplateDao: WorkoutTemplateDao by lazy {
        db.workoutTemplateDao()
    }
    private val workoutTemplateExerciseDao: WorkoutTemplateExerciseDao by lazy {
        db.workoutTemplateExerciseDao()
    }
    private val workoutTemplateExerciseSetDao: WorkoutTemplateExerciseSetDao by lazy {
        db.workoutTemplateExerciseSetDao()
    }
    private val programRepository: ProgramRepository by lazy {
        ProgramRepositoryImpl(programDao)
    }
}