package fr.cindano.discovery.recommendation

import fr.cindano.discovery.catalog.Experience

data class Contribution(val points: Double, val reason: String)

/** A rule looks at one experience for one profile and may contribute points with a human-readable reason. */
fun interface ScoringRule {
    fun evaluate(experience: Experience, profile: Profile): Contribution?
}

val likedSportRule = ScoringRule { e, p ->
    if (e.sport in p.likedSports.lowerCased()) Contribution(3.0, "sport apprécié : ${e.sport}") else null
}

val sharedTagsRule = ScoringRule { e, p ->
    val common = e.tags intersect p.interests.lowerCased()
    if (common.isEmpty()) null else Contribution(common.size.toDouble(), "centres d'intérêt : ${common.sorted().joinToString()}")
}

val proximityRule = ScoringRule { e, p ->
    if (p.city != null && e.city.equals(p.city, ignoreCase = true)) Contribution(2.0, "proche de ${e.city}") else null
}

private fun Set<String>.lowerCased() = mapTo(mutableSetOf()) { it.lowercase() }
