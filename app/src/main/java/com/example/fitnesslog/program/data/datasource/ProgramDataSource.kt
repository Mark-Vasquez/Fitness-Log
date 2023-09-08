package com.example.fitnesslog.program.data.datasource

import com.example.fitnesslog.program.data.model.Program

class ProgramDataSource {
    fun loadPrograms(): List<Program> {
        return listOf(
            Program("Push Pull Legs", 6),
            Program("Arnold Bodybuilding Suck My Hoopla", 5),
            Program("5x5 StrongLift", 3),
        )
    }
}