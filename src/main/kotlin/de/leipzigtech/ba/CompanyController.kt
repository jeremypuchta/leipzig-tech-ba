package de.leipzigtech.ba

import de.leipzigtech.ba.model.Company
import de.leipzigtech.ba.repository.CompanyRepository
import de.leipzigtech.ba.service.CompanyService
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody



@RestController
class CompanyController(private val comService: CompanyService) {

    @Autowired
    private val comRepository: CompanyRepository? = null


    @GetMapping("/companies/ref")
    fun getRef(): ResponseEntity<List<String>> {

        return comService.getAllRef()
    }
    @GetMapping("/companies/{id}")
    fun getCompaniesById(@PathVariable(value="id") id:Long): ResponseEntity<Company> {

       return comService.getCompanieById(id)
    }
    @GetMapping("/companies")
    fun getCompanies(@RequestParam(value="name",required = false) name: String?,@RequestParam(value="fuzzy",required = false,defaultValue = "true")fuzzy:Boolean,@RequestParam(value= "orderBy",required = false,defaultValue = "name") orderBy: String,@RequestParam(value="sort",required = false,defaultValue = "ASC")sort:String): ResponseEntity<List<Company>> {

        if (name != null) {
            if(!name.isBlank()) return name.let { comService.getCompaniesbyName_fuzzy(it,fuzzy) }
        }
        return  comService.getallCompanies(sort,orderBy)



    }

    @PostMapping("/companies")
    fun addCompanies(@RequestBody com: Company): ResponseEntity<Company> {

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
