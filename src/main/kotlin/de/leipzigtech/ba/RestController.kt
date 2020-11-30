package de.leipzigtech.ba

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.repository.Companies_Repository
import de.leipzigtech.ba.service.CompaniesService
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController

import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired



@RestController
class TaskResource(private val comService: CompaniesService) {

    @Autowired
    private val questionRepository: Companies_Repository? = null

    @GetMapping("/companies")
    fun getCompanies(): List<Companies> =
            comService.getCompanies()

    @GetMapping("/companies/{name}")
    fun getCompaniesbyName(@PathVariable(value="name") name:String): ResponseEntity<Companies> =
        comService.getCompaniesbyName(name)

    @GetMapping("/companies/fuzzy/{name}")
    fun getCompaniesbyName_fuzzy(@PathVariable(value="name") name:String): ResponseEntity<List<Companies>> =
            comService.getCompaniesbyName_fuzzy(name)

    @PostMapping("/companies")
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
}
