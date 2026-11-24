package fr.cindano.discovery

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class CatalogApiTest(@Autowired val mvc: MockMvc) {

    @Test
    fun `filters the catalog by sport`() {
        mvc.get("/api/experiences?sport=escalade").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(2) }
        }
    }

    @Test
    fun `unknown experience returns 404`() {
        mvc.get("/api/experiences/999").andExpect { status { isNotFound() } }
    }

    @Test
    fun `invalid experience is rejected`() {
        mvc.post("/api/experiences") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"","sport":"yoga","city":"Lyon","level":"BEGINNER","priceEur":10}"""
        }.andExpect { status { isBadRequest() } }
    }

    @Test
    fun `recommendations favour liked sport in the user city`() {
        mvc.post("/api/recommendations") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"likedSports":["escalade"],"interests":["groupe"],"city":"Lyon"}"""
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].experience.title") { value("Initiation escalade en salle") }
        }
    }

    @Test
    fun `proximity alone never recommends an experience`() {
        mvc.post("/api/recommendations") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"city":"Lyon"}"""
        }.andExpect { jsonPath("$.length()") { value(0) } }
    }

    @Test
    fun `max price filters out expensive experiences`() {
        mvc.post("/api/recommendations") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"likedSports":["surf"],"maxPriceEur":50}"""
        }.andExpect { jsonPath("$.length()") { value(0) } }
    }

    @Test
    fun `created experience can be read then deleted`() {
        val created = mvc.post("/api/experiences") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"Yoga","sport":"Yoga","city":"Lyon","level":"BEGINNER","priceEur":10,"tags":["Calme"]}"""
        }.andExpect { status { isCreated() } }.andReturn().response.contentAsString
        val id = Regex(""""id":(\d+)""").find(created)!!.groupValues[1]
        mvc.get("/api/experiences/$id").andExpect { jsonPath("$.sport") { value("yoga") } }
        mvc.delete("/api/experiences/$id").andExpect { status { isNoContent() } }
        mvc.get("/api/experiences/$id").andExpect { status { isNotFound() } }
    }

    @Test
    fun `too long title is a 400 not a 500`() {
        mvc.post("/api/experiences") {
            contentType = MediaType.APPLICATION_JSON
            content = """{"title":"${"x".repeat(201)}","sport":"yoga","city":"Lyon","level":"BEGINNER","priceEur":1}"""
        }.andExpect { status { isBadRequest() } }
    }
}
