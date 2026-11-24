package fr.cindano.discovery.recommendation

import fr.cindano.discovery.catalog.CatalogService
import fr.cindano.discovery.catalog.Experience
import org.springframework.stereotype.Service

data class Profile(
    val likedSports: Set<String> = emptySet(),
    val interests: Set<String> = emptySet(),
    val city: String? = null,
    val maxPriceEur: Double? = null,
)

data class Recommendation(val experience: Experience, val score: Double, val reasons: List<String>)

@Service
class RecommendationService(private val catalog: CatalogService) {

    // Content-based scoring: simple and explainable, a baseline before any ML model.
    fun recommend(profile: Profile, limit: Int = 5): List<Recommendation> =
        catalog.list()
            .filter { profile.maxPriceEur == null || it.priceEur <= profile.maxPriceEur }
            .map { score(it, profile) }
            .filter { it.score > 0 }
            .sortedByDescending { it.score }
            .take(limit)

    private fun score(e: Experience, p: Profile): Recommendation {
        val reasons = mutableListOf<String>()
        var s = 0.0
        if (e.sport in p.likedSports.map { it.lowercase() }) { s += 3.0; reasons += "sport apprécié : ${e.sport}" }
        val common = e.tags intersect p.interests.map { it.lowercase() }.toSet()
        if (common.isNotEmpty()) { s += common.size; reasons += "centres d'intérêt : ${common.sorted().joinToString()}" }
        if (s > 0 && p.city != null && e.city.equals(p.city, true)) { s += 2.0; reasons += "proche de ${e.city}" }
        return Recommendation(e, s, reasons)
    }
}
