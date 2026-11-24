package fr.cindano.discovery

import fr.cindano.discovery.catalog.Experience
import fr.cindano.discovery.catalog.Level
import fr.cindano.discovery.recommendation.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ScoringRuleTest {
    private val climbing = Experience(1, "Escalade", "escalade", "Lyon", Level.BEGINNER, 25.0, setOf("indoor", "groupe"))

    @Test
    fun `liked sport gives 3 points and a reason`() {
        val c = assertNotNull(likedSportRule.evaluate(climbing, Profile(likedSports = setOf("Escalade"))))
        assertEquals(3.0, c.points)
        assertEquals("sport apprécié : escalade", c.reason)
    }

    @Test
    fun `shared tags give one point each`() {
        val c = assertNotNull(sharedTagsRule.evaluate(climbing, Profile(interests = setOf("INDOOR", "groupe", "mer"))))
        assertEquals(2.0, c.points)
    }

    @Test
    fun `proximity gives 2 points for the same city, ignoring case`() {
        assertEquals(2.0, assertNotNull(proximityRule.evaluate(climbing, Profile(city = "lyon"))).points)
    }

    @Test
    fun `no match gives no contribution`() {
        assertNull(likedSportRule.evaluate(climbing, Profile(likedSports = setOf("surf"))))
        assertNull(proximityRule.evaluate(climbing, Profile()))
    }
}
