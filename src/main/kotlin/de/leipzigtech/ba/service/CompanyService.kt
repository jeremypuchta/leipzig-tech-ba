package de.leipzigtech.ba.service

import de.leipzigtech.ba.model.Company
import de.leipzigtech.ba.repository.CompanyRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import com.google.gson.*
import kotlin.system.exitProcess

@Service
class CompanyService(private val comRepository: CompanyRepository) {


    fun getallCompanies(sort:String,orderBy:String): ResponseEntity<List<Company>> {

        if (sort == "ASC"){

            val com =comRepository.findAll((Sort.by(Sort.Direction.ASC, orderBy)))

            return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
            else ResponseEntity.ok(com)
        }

        val com =comRepository.findAll((Sort.by(Sort.Direction.DESC, orderBy)))

        return if(com.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(com)

    }

    fun getCompaniesbyName_case(name:String, case:Boolean): ResponseEntity<List<Company>> {


        if(case){
           val  com = comRepository.findByName_fuzzy(name)
             return if(com.isNullOrEmpty()) ResponseEntity.ok(com)
             else ResponseEntity.ok(com)
        }

        val com = comRepository.findByName_case(name)

        return if(com.isNullOrEmpty()) ResponseEntity.ok(com)
        else ResponseEntity.ok(com)

    }

    fun deleteCompanie(comId: Long): ResponseEntity<Void> =
            comRepository?.findById(comId)?.map { com ->
                comRepository.delete(com)
                ResponseEntity<Void>(HttpStatus.ACCEPTED)
            }?.orElse(ResponseEntity.notFound().build())!!


    fun addCompanies(companies: Company): ResponseEntity<Company> =
            ResponseEntity.ok(comRepository.save(companies))

    fun getCompanieById(comId: Long): ResponseEntity<Company> =
            comRepository.findById(comId).map { companies ->
                ResponseEntity.ok(companies)
            }.orElse(ResponseEntity.notFound().build())

    fun getAllRef(): ResponseEntity<List<String>>{
        val com = comRepository.getAllRef()

        val list=mutableListOf<String>()
        for (i in com){
            list.add(i.ref)
        }
        return if(list.isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(list)

    }
    fun getLongLat(com:Company) {

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

    fun getDistrict(com:Company) {

        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .url("https://www.postdirekt.de/plzserver/PlzAjaxServlet?nocache=1610556880371&format=json&plz_city=" + com.city + "&plz_plz="+com.plz+"&plz_city_clear=&plz_district=&finda=plz&plz_street=" +com.address+"&lang=de_DE ")
                .build()
        val response =okHttpClient.newCall(request).execute()

        if(response.isSuccessful){


            val obj = JsonParser.parseString(response.body()?.string()).asJsonObject["rows"].asJsonArray

            com.district=obj.get(0).asJsonObject.get("district").asString


        }else{
            throw Exception()
        }

    }
}