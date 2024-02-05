package com.example.fitnesslog.program.di

import com.example.fitnesslog.core.data.database.FitnessLogDatabase
import com.example.fitnesslog.program.data.dao.ExerciseTemplateDao
import com.example.fitnesslog.program.data.repository.ExerciseRepositoryImpl
import com.example.fitnesslog.program.domain.repository.ExerciseRepository

interface ExerciseTemplateModule {
    val exerciseTemplateDao: ExerciseTemplateDao
    val exerciseRepository: ExerciseRepository
}

class ExerciseTemplateModuleImpl(private val db: FitnessLogDatabase) : ExerciseTemplateModule {
    override val exerciseTemplateDao: ExerciseTemplateDao by lazy {
        db.exerciseDao()
    }
    override val exerciseRepository: ExerciseRepository by lazy {
        ExerciseRepositoryImpl(exerciseTemplateDao)
    }

}