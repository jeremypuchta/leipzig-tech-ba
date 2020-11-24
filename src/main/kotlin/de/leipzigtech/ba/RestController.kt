package de.leipzigtech.ba

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.model.Location
import de.leipzigtech.ba.repository.Companies_Repository
import de.leipzigtech.ba.service.CompaniesService
import de.leipzigtech.ba.service.LocationService
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired



@RestController
class TaskResource(private val comService: CompaniesService,private val locService:LocationService) {

    @Autowired
    private val questionRepository: Companies_Repository? = null

    @GetMapping("/companies")
    fun getCompanies(): List<Companies> =
            comService.getCompanies()

    @PostMapping("/companie")
    fun addCompanies(@RequestBody com: Companies): ResponseEntity<Companies> =
            comService.addCompanies(com)

    @GetMapping("/{id}")
    fun getCompanieById(@PathVariable(value="id") comId: Long): ResponseEntity<Companies> =
            comService.getCompanieById(comId)

    /*
    @PutMapping("/{id}")
    fun updateCompanieById(
            @PathVariable(value = "id") comId: Long,
            @RequestBody newCom: Companies): ResponseEntity<Companies> =
            comService.putCompanie(comId, newCom)

    @DeleteMapping("/{id}")
    fun deleteCompanie(@PathVariable(value = "id") comId: Long): ResponseEntity<Void> =
            comService.deleteCompanie(comId)
            */


    //location functions
    @GetMapping("/locations")
    fun getLocations(): List<Location> =
            locService.getLocation()

    @PostMapping("/location")
    fun addLocation(@RequestBody loc: Location): ResponseEntity<Location> =
            locService.addLocation(loc)
}
