package de.leipzigtech.ba.service

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.repository.Companies_Repository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable
import com.google.gson.*
import com.google.gson.JsonObject
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder.json

@Service
class CompaniesService(private val comRepository: Companies_Repository) {


    fun getallCompanies(sort:String,orderBy:String): ResponseEntity<List<Companies>> {

        if (sort == "ASC"){

            val com =comRepository.findAll((Sort.by(Sort.Direction.ASC, orderBy)))

            return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
            else ResponseEntity.ok(com)
        }

        val com =comRepository.findAll((Sort.by(Sort.Direction.DESC, orderBy)))

        return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(com)

    }

    fun getCompaniesbyName_fuzzy(name:String,fuzzy:Boolean): ResponseEntity<List<Companies>> {


        if(fuzzy){
           val  com = comRepository.findByName_fuzzy(name)
             return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
             else ResponseEntity.ok(com)
        }

        /*
        exact match name -> order by ->Name
         */
        val com = comRepository.findByNameOrderByNameAsc(name)

        return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(com)

    }

    fun deleteCompanie(comId: Long): ResponseEntity<Void> =
            comRepository?.findById(comId)?.map { com ->
                comRepository.delete(com)
                ResponseEntity<Void>(HttpStatus.ACCEPTED)
            }?.orElse(ResponseEntity.notFound().build())!!


    fun addCompanies(companies: Companies): ResponseEntity<Companies> =
            ResponseEntity.ok(comRepository.save(companies))

    fun getCompanieById(comId: Long): ResponseEntity<Companies> =
            comRepository.findById(comId).map { companies ->
                ResponseEntity.ok(companies)
            }.orElse(ResponseEntity.notFound().build())

    fun getLongLat(com:Companies) {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url("https://nominatim.openstreetmap.org/search?format=json&q=" + com.address + " " + com.plz + " " + com.city)
                .build()
        val response =okHttpClient.newCall(request).execute()

        if(response.isSuccessful){

            val obj = JsonParser.parseString(response.body()?.string()).asJsonArray

            com.longitude= obj.get(0).asJsonObject.get("lon").asFloat
            com.latitude= obj.get(0).asJsonObject.get("lat").asFloat

        }else{

            throw Exception()
        }

    }
}