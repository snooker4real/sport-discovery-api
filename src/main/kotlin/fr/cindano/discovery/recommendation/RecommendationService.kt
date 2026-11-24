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
    private val matchRules = listOf(likedSportRule, sharedTagsRule)

    // Content-based scoring: simple and explainable, a baseline before any ML model.
    fun recommend(profile: Profile, limit: Int = 5): List<Recommendation> =
        catalog.list()
            .filter { profile.maxPriceEur == null || it.priceEur <= profile.maxPriceEur }
            .mapNotNull { score(it, profile) }
            .sortedByDescending { it.score }
            .take(limit)

    // Proximity only boosts an experience the user already matches on sport or interests.
    private fun score(e: Experience, p: Profile): Recommendation? {
        val matches = matchRules.mapNotNull { it.evaluate(e, p) }
        if (matches.isEmpty()) return null
        val all = matches + listOfNotNull(proximityRule.evaluate(e, p))
        return Recommendation(e, all.sumOf { it.points }, all.map { it.reason })
    }
}
