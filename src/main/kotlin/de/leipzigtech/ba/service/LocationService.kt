package de.leipzigtech.ba.service


import de.leipzigtech.ba.repository.Location_Repository
import de.leipzigtech.ba.model.Location
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class LocationService(private val locRepository: Location_Repository) {


    fun getLocation(): List<Location> =
            locRepository.findAll()

    fun addLocation(location: Location): ResponseEntity<Location> =
            ResponseEntity.ok(locRepository.save(location))

    fun getLocationById(comId: Long): ResponseEntity<Location> =
            locRepository.findById(comId).map { location ->
                ResponseEntity.ok(location)
            }.orElse(ResponseEntity.notFound().build())



}