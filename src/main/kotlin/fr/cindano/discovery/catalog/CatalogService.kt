package fr.cindano.discovery.catalog

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

class ExperienceNotFound(id: Long) : RuntimeException("Experience $id not found")

@Service
@Transactional
class CatalogService(private val repository: ExperienceRepository) {

    @Transactional(readOnly = true)
    fun list(sport: String? = null, city: String? = null): List<Experience> =
        when {
            sport != null && city != null -> repository.findBySportIgnoreCaseAndCityIgnoreCaseOrderByIdAsc(sport, city)
            sport != null -> repository.findBySportIgnoreCaseOrderByIdAsc(sport)
            city != null -> repository.findByCityIgnoreCaseOrderByIdAsc(city)
            else -> repository.findAllByOrderByIdAsc()
        }.map { it.toDomain() }

    @Transactional(readOnly = true)
    fun get(id: Long): Experience = (repository.findByIdOrNull(id) ?: throw ExperienceNotFound(id)).toDomain()

    fun create(r: ExperienceRequest): Experience =
        repository.save(
            ExperienceEntity(r.title, r.sport.lowercase(), r.city, r.level, r.priceEur, r.tags.map { it.lowercase() }.toMutableSet()),
        ).toDomain()

    fun delete(id: Long) {
        if (!repository.existsById(id)) throw ExperienceNotFound(id)
        repository.deleteById(id)
    }
}
