package fr.cindano.discovery.catalog

import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

enum class Level { BEGINNER, INTERMEDIATE, ADVANCED }

data class Experience(
    val id: Long,
    val title: String,
    val sport: String,
    val city: String,
    val level: Level,
    val priceEur: Double,
    val tags: Set<String>,
)

data class ExperienceRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val sport: String,
    @field:NotBlank val city: String,
    val level: Level,
    @field:DecimalMin("0.0") val priceEur: Double,
    @field:Size(max = 10) val tags: Set<String> = emptySet(),
)
