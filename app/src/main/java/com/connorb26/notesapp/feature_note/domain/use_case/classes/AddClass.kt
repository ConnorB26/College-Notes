package com.connorb26.notesapp.feature_note.domain.use_case.classes

import com.connorb26.notesapp.feature_note.domain.model.Class
import com.connorb26.notesapp.feature_note.domain.model.InvalidClassException
import com.connorb26.notesapp.feature_note.domain.repository.ClassRepository
import kotlin.jvm.Throws

class AddClass (
    private val repository: ClassRepository
) {
    @Throws(InvalidClassException::class)
    suspend operator fun invoke(classObj: Class) {
        if(classObj.name.isBlank()) {
            throw InvalidClassException("The name of the class can't be blank")
        }
        if(classObj.firstDay.toString() == classObj.firstDay.default) {
            throw InvalidClassException("The first day can't be blank")
        }
        if(classObj.lastDay.toString() == classObj.lastDay.default) {
            throw InvalidClassException("The last day can't be blank")
        }

        if(classObj.name.isNotBlank() && classObj.firstDay.toString() != classObj.firstDay.default && classObj.lastDay.toString() != classObj.lastDay.default) {
            repository.insertClass(classObj)
        }
    }
}