package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


@Repository
interface CompanyRepository : JpaRepository<Company, Long> {

    //Searching Querys
    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where name LIKE  CONCAT('%',:name,'%')  ORDER BY LEVENSHTEIN(c.name,:name) ASC ", nativeQuery = true)
    fun findByName(@Param("name") name: kotlin.String): List<Company>


    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c WHERE c.name like CONCAT('%', :name,'%') ORDER BY LEVENSHTEIN(c.name,:name) ASC ",nativeQuery = true)
    fun findByNameAllIgnoreCase(@Param("name") name: String): List<Company>



    // sector ,district , case = false
    @Query("SELECT c FROM Company c WHERE lower(c.name) like lower(concat('%', :name,'%'))AND (c.sector IN (:sector)) AND (c.district IN (:district)) ")     // 2. Spring JPA In cause using @Query
    fun findByNameAllIgnoreCaseSectorInAndDistrictIn(@Param("name") name: String,@Param("sector") sector: List<String>,@Param("district") district: List<String>): List<Company>
    // sector , district , case = true
    @Query("SELECT  c FROM Company c Where c.name LIKE  CONCAT('%',:name,'%') AND (c.sector IN (:sector)) AND (c.district IN (:district)) ")
    fun findByNameSectorInAndDistrictIn(@Param("name") name: kotlin.String,@Param("sector")sector: List<String>,@Param("district")district: List<String>): List<Company>
    //only Sector, district empty , case=true
    fun findAllByNameContainingAndSectorIn(name: kotlin.String,sector: List<String>):List<Company>
    //only Sector, district empty , case=false
    fun findAllByNameContainingIgnoreCaseAndSectorIn(name: kotlin.String,sector: List<String>):List<Company>

    //only district , sector empty, case = false
    fun findAllByNameContainingIgnoreCaseAndDistrictIn(name: kotlin.String,district: List<String>):List<Company>
    //only district , sector empty, case = true
    fun findAllByNameContainingAndDistrictIn(name: kotlin.String,district: List<String>):List<Company>

    // Get Companies without Name
    //only sector , district empty, ASC
    @Query("SELECT c FROM Company c WHERE  (c.sector IN (:sector)) order by :orderBy ASC ")
    fun findAllBySectorInASC(@Param("sector") sector: List<String>, @Param("orderBy")orderBy:String): List<Company>
    //only sector , district empty, DESC
    @Query("SELECT c FROM Company c WHERE  (c.sector IN (:sector)) order by :orderBy DESC ")
    fun findAllBySectorInDesc(@Param("sector") sector: List<String>, @Param("orderBy")orderBy:String): List<Company>

    //only district , sector empty, ASC
    @Query("SELECT c FROM Company c WHERE  (c.district IN (:district)) order by :orderBy ASC ")
    fun findAllByDistrictInOrderByASC(@Param("district") district: List<String>, @Param("orderBy")orderBy:String): List<Company>
    //only district , sector empty, DESC
    @Query("SELECT c FROM Company c WHERE  (c.district IN (:district)) order by :orderBy DESC ")
    fun findAllByDistrictInOrderByDESC(@Param("district") district: List<String>, @Param("orderBy")orderBy:String): List<Company>

    // district , sector , ASC
    @Query("SELECT c FROM Company c WHERE (c.sector IN (:sector)) AND (c.district IN (:district)) order by :orderBy ASC ")
    fun findAllByDistrictInAndSectorInOrderByASC(@Param("sector") sector: List<String>,@Param("district") district: List<String>, @Param("orderBy")orderBy:String): List<Company>

    // district , sector , DESC
    @Query("SELECT c FROM Company c WHERE (c.sector IN (:sector)) AND (c.district IN (:district)) order by :orderBy DESC ")
    fun findAllByDistrictInAndSectorInOrderByDESC(@Param("sector") sector: List<String>,@Param("district") district: List<String>, @Param("orderBy")orderBy:String): List<Company>

    // Ref-API
    @Query("SELECT * From companies c ",nativeQuery = true)
    fun getAllRef():List<Company>


    //Statistic
    @Query("SELECT sector ,COUNT(*) From companies c Group by sector",nativeQuery = true)
    fun countBySector():List<String>
    @Query("SELECT district ,COUNT(*) From companies c Group by district",nativeQuery = true)
    fun countByDistrict():List<String>
    @Query("SELECT COUNT(*) From companies c Where create_at > current_date - interval '7 days'",nativeQuery = true)
    fun countCompaniesLast7Days():List<String>
    @Query("SELECT COUNT(*) From companies c Where create_at > current_date - interval '30 days'",nativeQuery = true)
    fun countCompaniesLast30Days():List<String>

}