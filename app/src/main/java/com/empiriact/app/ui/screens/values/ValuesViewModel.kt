package com.empiriact.app.ui.screens.values

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.empiriact.app.data.UserValue
import com.empiriact.app.data.UserValueRepository
import com.empiriact.app.data.ValueCategory
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ValuesViewModel(private val repository: UserValueRepository) : ViewModel() {

    private val initialCategories = getInitialValueCategories()

    val categories: StateFlow<List<ValueCategory>> = repository.getAll().map { dbValues ->
        val dbValuesMap = dbValues.associateBy { it.name }
        initialCategories.map { category ->
            category.copy(values = category.values.map { value ->
                dbValuesMap[value.name] ?: value
            })
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialCategories
    )

    init {
        viewModelScope.launch {
            if (repository.getAll().first().isEmpty()) {
                initialCategories.flatMap { it.values }.forEach {
                    repository.upsert(it)
                }
            }
        }
    }

    fun updateImportance(valueName: String, newImportance: Int) {
        viewModelScope.launch {
            val currentValue = repository.getAll().first().find { it.name == valueName }
            currentValue?.let {
                repository.upsert(it.copy(importance = newImportance))
            }
        }
    }

    fun updateImplementation(valueName: String, newImplementation: Int) {
        viewModelScope.launch {
            val currentValue = repository.getAll().first().find { it.name == valueName }
            currentValue?.let {
                repository.upsert(it.copy(implementation = newImplementation))
            }
        }
    }
}

fun getInitialValueCategories(): List<ValueCategory> {
    return listOf(
        ValueCategory(
            name = "Familiäre Beziehungen",
            values = listOf(
                UserValue("origin_family", "Gute Beziehungen zu Ursprungsfamilie pflegen", 5, 5),
                UserValue("own_family", "Vertrauter Umgang in eigener Familie (Partner/in, Kinder)", 5, 5),
                UserValue("support_children", "Meine Kinder unterstützen", 5, 5)
            )
        ),
        ValueCategory(
            name = "Partnerschaft (Liebe, Sexualität)",
            values = listOf(
                UserValue("loving_relationship", "Liebevolle Beziehung zu Partner/in führen", 5, 5),
                UserValue("intimacy", "Intimität/Sexualität erfüllend leben", 5, 5)
            )
        ),
        ValueCategory(
            name = "Freundschaften und soziale Beziehungen",
            values = listOf(
                UserValue("friendships", "Unterstützende Freundschaften erleben", 5, 5),
                UserValue("social_exchange", "Sich mit anderen austauschen", 5, 5),
                UserValue("help_others", "Anderen Menschen helfen", 5, 5)
            )
        )
    )
}
