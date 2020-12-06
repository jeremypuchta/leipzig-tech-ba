package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param


@Repository
interface CompanyRepository : JpaRepository<Company, Long> {

    //fuzzy_searching
    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where LEVENSHTEIN(c.name,:name)< 4 ORDER BY LEVENSHTEIN(c.name,:name) ASC LIMIT 3 ", nativeQuery = true)
    fun findByName_fuzzy(@Param("name") name: kotlin.String): List<Company>
    fun findByNameOrderByNameAsc(name: kotlin.String):List<Company>
    fun findByNameOrderByNameDesc(name: kotlin.String):List<Company>




}