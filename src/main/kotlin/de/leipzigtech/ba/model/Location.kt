package de.leipzigtech.ba.model

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "location")
data class Location(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        val street: String,
        val nummer: String,
        val plz: String,
        val city: String,


        @ManyToOne
        @JoinColumn(name ="companies_id", nullable = false)
        val companies: Companies




)