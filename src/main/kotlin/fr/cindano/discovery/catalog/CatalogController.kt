package fr.cindano.discovery.catalog

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/experiences")
class CatalogController(private val service: CatalogService) {

    @GetMapping
    fun list(@RequestParam sport: String?, @RequestParam city: String?) = service.list(sport, city)

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long) = service.get(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: ExperienceRequest) = service.create(request)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = service.delete(id)

    @ExceptionHandler(ExperienceNotFound::class)
    fun notFound(e: ExperienceNotFound): ProblemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message ?: "Not found")
}
