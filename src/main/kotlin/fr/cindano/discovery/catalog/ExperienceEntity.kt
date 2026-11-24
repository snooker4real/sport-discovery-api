package fr.cindano.discovery.catalog

import jakarta.persistence.*

@Entity
@Table(name = "experience")
class ExperienceEntity(
    var title: String,
    var sport: String,
    var city: String,
    @Enumerated(EnumType.STRING) var level: Level,
    var priceEur: Double,
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "experience_tag", joinColumns = [JoinColumn(name = "experience_id")])
    @Column(name = "tag")
    var tags: MutableSet<String> = mutableSetOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
) {
    fun toDomain() = Experience(id!!, title, sport, city, level, priceEur, tags.toSet())
}
