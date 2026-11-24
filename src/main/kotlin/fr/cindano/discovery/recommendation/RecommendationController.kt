package fr.cindano.discovery.recommendation

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recommendations")
class RecommendationController(private val service: RecommendationService) {

    @PostMapping
    fun recommend(@RequestBody profile: Profile, @RequestParam(defaultValue = "5") limit: Int) =
        service.recommend(profile, limit.coerceIn(1, 20))
}
