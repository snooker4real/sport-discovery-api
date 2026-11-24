package fr.cindano.discovery.catalog

import org.springframework.data.jpa.repository.JpaRepository

// Derived queries instead of a nullable JPQL parameter, which PostgreSQL can reject (untyped null).
interface ExperienceRepository : JpaRepository<ExperienceEntity, Long> {
    fun findAllByOrderByIdAsc(): List<ExperienceEntity>
    fun findBySportIgnoreCaseOrderByIdAsc(sport: String): List<ExperienceEntity>
    fun findByCityIgnoreCaseOrderByIdAsc(city: String): List<ExperienceEntity>
    fun findBySportIgnoreCaseAndCityIgnoreCaseOrderByIdAsc(sport: String, city: String): List<ExperienceEntity>
}
