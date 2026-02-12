package com.empiriact.app.data

import android.content.Context
import com.empiriact.app.data.model.Course
import com.empiriact.app.data.model.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import java.io.IOException

class CourseRepository(private val context: Context) {

    private val json = Json { ignoreUnknownKeys = true }

    fun getFullCourse(): Flow<Course> = flow {
        try {
            val jsonString = readAsset("courses/course.json")
            emit(json.decodeFromString<Course>(jsonString))
        } catch (e: IOException) {
            e.printStackTrace()
            // Emit an empty course or handle the error as needed
        }
    }

    fun getModule(courseId: String, moduleId: String): Flow<Module?> = flow {
        try {
            val jsonString = readAsset("courses/$courseId.json")
            val course = json.decodeFromString<Course>(jsonString)
            emit(course.modules.find { it.id == moduleId })
        } catch (e: IOException) {
            e.printStackTrace()
            emit(null)
        }
    }

    private fun readAsset(fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }
}
