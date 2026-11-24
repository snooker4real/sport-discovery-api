package fr.cindano.discovery.catalog

import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

class ExperienceNotFound(id: Long) : RuntimeException("Experience $id not found")

@Service
class CatalogService {
    private val store = ConcurrentHashMap<Long, Experience>()
    private val seq = AtomicLong()

    init {
        listOf(
            ExperienceRequest("Initiation escalade en salle", "escalade", "Lyon", Level.BEGINNER, 25.0, setOf("indoor", "groupe", "découverte")),
            ExperienceRequest("Sortie trail dans les Monts d'Or", "trail", "Lyon", Level.INTERMEDIATE, 15.0, setOf("outdoor", "nature", "endurance")),
            ExperienceRequest("Stage de surf à Hossegor", "surf", "Hossegor", Level.BEGINNER, 120.0, setOf("outdoor", "océan", "découverte")),
            ExperienceRequest("Match de padel entre amis", "padel", "Lyon", Level.INTERMEDIATE, 12.0, setOf("indoor", "groupe", "raquette")),
            ExperienceRequest("Via ferrata en Chartreuse", "escalade", "Grenoble", Level.ADVANCED, 60.0, setOf("outdoor", "montagne", "adrénaline")),
        ).forEach(::create)
    }

    fun list(sport: String? = null, city: String? = null): List<Experience> =
        store.values.filter { sport == null || it.sport.equals(sport, true) }
            .filter { city == null || it.city.equals(city, true) }
            .sortedBy { it.id }

    fun get(id: Long): Experience = store[id] ?: throw ExperienceNotFound(id)

    fun create(r: ExperienceRequest): Experience {
        val e = Experience(seq.incrementAndGet(), r.title, r.sport.lowercase(), r.city, r.level, r.priceEur, r.tags.map { it.lowercase() }.toSet())
        store[e.id] = e
        return e
    }

    fun delete(id: Long) {
        store.remove(id) ?: throw ExperienceNotFound(id)
    }
}
