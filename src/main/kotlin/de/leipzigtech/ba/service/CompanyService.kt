package de.leipzigtech.ba.service

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import de.leipzigtech.ba.model.Company
import de.leipzigtech.ba.repository.CompanyRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service


@Service
class CompanyService(private val comRepository: CompanyRepository) {


    fun getallCompanies(sort:String,orderBy:String,sector: String,district: String): ResponseEntity<List<Company>> {
        //Validate Filter-Strings
        val (sector_list,district_list) =validateFilter(sector,district)

        if(district_list.isEmpty() && sector_list.isEmpty()){
            if(sort =="ASC"){
                val com =comRepository.findAll((Sort.by(Sort.Direction.ASC, orderBy)))
                return ResponseEntity.ok(com)
            }
            val com =comRepository.findAll((Sort.by(Sort.Direction.DESC, orderBy)))
            return ResponseEntity.ok(com)
        }
        if(sector_list.isEmpty()){
            if(sort =="ASC"){
                val  com = comRepository.findAllByDistrictInOrderByASC(district_list,orderBy)
                return ResponseEntity.ok(com)
            }
            val  com = comRepository.findAllByDistrictInOrderByDESC(district_list,orderBy)
            return ResponseEntity.ok(com)
        }
        if(district_list.isEmpty()){
            if(sort =="ASC"){
                val  com = comRepository.findAllBySectorInASC(sector_list,orderBy)
                return ResponseEntity.ok(com)
            }
            val  com = comRepository.findAllBySectorInDesc(sector_list,orderBy)
            return ResponseEntity.ok(com)
        }
        if(sort =="ASC"){
            val com =comRepository.findAllByDistrictInAndSectorInOrderByASC(sector_list,district_list,orderBy)
            return ResponseEntity.ok(com)
        }
        val com =comRepository.findAllByDistrictInAndSectorInOrderByDESC(sector_list,district_list,orderBy)
        return  ResponseEntity.ok(com)


    }

    fun getCompaniesbyName_case(name:String, sector:String, district:String, case:Boolean): ResponseEntity<List<Company>> {

        //Validate Filter-Strings
        val (sector_list, district_list) = validateFilter(sector, district)


        if(sector_list.isEmpty() && district_list.isEmpty()) {
            if (case) {
                val com = comRepository.findByName(name)
                return ResponseEntity.ok(com)
            }
            val com = comRepository.findByNameAllIgnoreCase(name)
            return ResponseEntity.ok(com)
        }
        if(sector_list.isEmpty()){
            if(case){
                val  com = comRepository.findAllByNameContainingAndDistrictIn(name,district_list)
                return ResponseEntity.ok(com)
            }
            val  com = comRepository.findAllByNameContainingIgnoreCaseAndDistrictIn(name,district_list)
            return ResponseEntity.ok(com)
        }

        if(district_list.isEmpty()){
            if(case){
                val  com = comRepository.findAllByNameContainingAndSectorIn(name,sector_list)
                return ResponseEntity.ok(com)
            }
            val  com = comRepository.findAllByNameContainingIgnoreCaseAndSectorIn(name,sector_list)
            return ResponseEntity.ok(com)
        }
        if(case){
            val com =comRepository.findByNameSectorInAndDistrictIn(name,sector_list,district_list)
            return  ResponseEntity.ok(com)
        }

        val com =comRepository.findByNameAllIgnoreCaseSectorInAndDistrictIn(name,sector_list,district_list)
        return  ResponseEntity.ok(com)


    }
    private fun validateFilter(sector: String, district: String): Pair<MutableList<String>, MutableList<String>> {


        val sectorList = sector.split(",").toMutableList()
        val districtList = district.split(",").toMutableList()

        if (sectorList[0] == "") {
            sectorList.clear()
        }
        if (districtList[0] == "") {
            districtList.clear()

        }
        return Pair(sectorList, districtList)
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

    fun getStats(): ResponseEntity<String>{

        val numDis = comRepository.countByDistrict()
        val numSector = comRepository.countBySector()
        val num7days= comRepository.countCompaniesLast7Days()
        val num30days= comRepository.countCompaniesLast30Days()
        //Number of companies
        val number = comRepository.count()


        val sector = JsonObject()
        val district = JsonObject()

        for (item in numSector){
            val parts=item.split(",")
            sector.addProperty(parts[0],parts[1])
        }
        for (item in numDis){

            val parts=item.split(",")
            district.addProperty(parts[0],parts[1])

        }

        val json = JsonObject()

        json.addProperty("number",number.toString())
        json.add("numberBySector",sector)
        json.add("numberByDistrict",district)
        json.addProperty("joinedSince7",num7days[0])
        json.addProperty("joinedSince30",num30days[0])


        return if(json.toString().isNullOrEmpty()) ResponseEntity.notFound().build()
        else ResponseEntity.ok(json.toString())

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

    fun setSector(com:Company){

        val hashmap: HashMap<String,String> = HashMap<String,String>()


        //noch vervollständigen
        hashmap.put("medicine","Medicine")
        hashmap.put("education","Education")
        hashmap.put("services","Services")
        hashmap.put("software Engineering","Software Engineering")
        hashmap.put("consulting","Consulting")
        hashmap.put("environment","Environment")
        hashmap.put("engineering","Engineering")
        hashmap.put("news","News")
        hashmap.put("legal","Legal")
        hashmap.put("insurance","Insurance")
        hashmap.put("accounting","Accounting")
        hashmap.put("design","Design")
        hashmap.put("accounting","Accounting")
        hashmap.put("news","News")
        hashmap.put("design","Design")
        hashmap.put("advertising","Advertising")
        hashmap.put("augenoptik","Medicine")
        hashmap.put("bildungseinrichtungen ","Education")
        hashmap.put("bürobedarf","Services")
        hashmap.put("coaching","Education")
        hashmap.put("computer","Services")
        hashmap.put("computer-dienstleistungen","Services")
        hashmap.put("computerreparaturen ","Services")
        hashmap.put("detekteien ","Legal")
        hashmap.put("edv","Software Engineering")
        hashmap.put("edv-beratungen","Consulting")
        hashmap.put("edv-dienstleistungen","Software Engineering")
        hashmap.put("geografische informationssysteme","Environment")
        hashmap.put("it-beratung","Consulting")
        hashmap.put("it-beratungen","Consulting")
        hashmap.put("it-dienstleistungen","Software Engineering")
        hashmap.put("ingenieurbüros","Engineering")
        hashmap.put("internetservice","Software Engineering")
        hashmap.put("nachrichtenagenturen","News")
        hashmap.put("netzwerke","(Computer) Networking")
        hashmap.put("notare","Legal")
        hashmap.put("personalberatung","Human Resources")
        hashmap.put("personaldienstleistungen","Human Resources")
        hashmap.put("rechtsanwälte","Legal")
        hashmap.put("software","Software Engineering")
        hashmap.put("unternehmensberatung","Consulting")
        hashmap.put("versicherungen","Insurance")
        hashmap.put("steuerberatung","Accounting")
        hashmap.put("webdesign","Design")
        hashmap.put("werbefotografie","Advertising")

        val oldsector = com.sector.toString().toLowerCase().filter { !it.isWhitespace() }
        //if null and empty break

        val newsector= hashmap[oldsector]
        if (newsector.isNullOrEmpty()){
            throw Exception()
        }
        com.sector=newsector
    }
}