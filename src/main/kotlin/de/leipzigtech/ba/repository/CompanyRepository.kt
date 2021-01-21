package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


@Repository
interface CompanyRepository : JpaRepository<Company, Long> {

    //fuzzy_searching
    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where name LIKE  CONCAT('%',:name,'%')  ORDER BY LEVENSHTEIN(c.name,:name) ASC ", nativeQuery = true)
    fun findByName_fuzzy(@Param("name") name: kotlin.String): List<Company>

    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where name ILIKE  CONCAT('%',:name,'%')  ORDER BY LEVENSHTEIN(c.name,:name) ASC ", nativeQuery = true)
    fun findByName_case(@Param("name") name: kotlin.String): List<Company>

    fun findByNameOrderByNameAsc(name: kotlin.String):List<Company>
    @Query("SELECT * From companies c ",nativeQuery = true)
    fun getAllRef():List<Company>
    fun findByNameOrderByNameDesc(name: kotlin.String):List<Company>





}