package com.connorb26.notesapp.feature_note.domain.use_case.classes

data class ClassUseCases(
    val getClasses: GetClasses,
    val deleteClass: DeleteClass,
    val addClass: AddClass,
    val getClass: GetClass
)
