package de.leipzigtech.ba.service

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.repository.Companies_Repository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.util.ArrayList



@Service
class CompaniesService(private val comRepository: Companies_Repository) {


    fun getCompanies(): List<Companies> =
            comRepository.findAll()

    fun getCompaniesbyName(name:String): ResponseEntity<Companies> {
        val com =comRepository.findByName(name)
        return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(com.get(0))

        }
    fun getCompaniesbyName_fuzzy(name:String): ResponseEntity<List<Companies>> {
        val com =comRepository.findByName_fuzzy(name)
        return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(com)

    }
    fun addCompanies(companies: Companies): ResponseEntity<Companies> =
            ResponseEntity.ok(comRepository.save(companies))

    fun getCompanieById(comId: Long): ResponseEntity<Companies> =
            comRepository.findById(comId).map { companies ->
                ResponseEntity.ok(companies)
            }.orElse(ResponseEntity.notFound().build())
}