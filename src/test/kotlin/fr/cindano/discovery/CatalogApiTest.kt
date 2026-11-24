package fr.cindano.discovery

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
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
}
