package de.leipzigtech.ba

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.repository.Companies_Repository
import de.leipzigtech.ba.service.CompaniesService
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody



@RestController
class TaskResource(private val comService: CompaniesService) {

    @Autowired
    private val comRepository: Companies_Repository? = null


    @GetMapping("/companies/{id}")
    fun getCompaniesById(@PathVariable(value="id") id:Long): ResponseEntity<Companies> {

       return comService.getCompanieById(id)
    }
    @GetMapping("/companies")
    fun getCompanies(@RequestParam(value="name",required = false) name: String?,@RequestParam(value="fuzzy",required = false,defaultValue = "true")fuzzy:Boolean,@RequestParam(value= "orderBy",required = false,defaultValue = "name") orderBy: String,@RequestParam(value="sort",required = false,defaultValue = "ASC")sort:String): ResponseEntity<List<Companies>> {

        if (name != null) {
            if(!name.isBlank()) return name.let { comService.getCompaniesbyName_fuzzy(it,fuzzy) }
        }
        return  comService.getallCompanies(sort,orderBy)



    }

    @PostMapping("/companies")
    fun addCompanies(@RequestBody com: Companies): ResponseEntity<Companies> {

        try {
            comService.getLongLat(com)
        }catch (e:Exception){
            return ResponseEntity.badRequest().build()
        }


        return comService.addCompanies(com)
    }

    /*
    @PutMapping("/{id}")
    fun updateCompanieById(
            @PathVariable(value = "id") comId: Long,
            @RequestBody newCom: Companies): ResponseEntity<Companies> =
            comService.putCompanie(comId, newCom)
    */
    @DeleteMapping("companies/{id}")
    fun deleteCompanie(@PathVariable(value = "id") comId: Long): ResponseEntity<Void> =
            comService.deleteCompanie(comId)





}
