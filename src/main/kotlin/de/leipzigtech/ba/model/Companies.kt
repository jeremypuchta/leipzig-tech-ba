package de.leipzigtech.ba.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "companies")
data class Companies(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val name: String,
        val branche: String,
        val website: String,
        val email: String,
        @OneToMany(mappedBy = "companies")
        val location : List<Location> = arrayListOf<Location>()
)