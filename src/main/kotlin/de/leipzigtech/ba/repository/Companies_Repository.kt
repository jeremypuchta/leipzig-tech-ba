package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Companies
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import kotlin.String as String1


@Repository
interface Companies_Repository : JpaRepository<Companies, Long> {

    //fuzzy_searching
    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where LEVENSHTEIN(c.name,:name)< 4 ORDER BY LEVENSHTEIN(c.name,:name) ASC LIMIT 3 ", nativeQuery = true)
    fun findByName_fuzzy(@Param("name") name: kotlin.String): List<Companies>
    fun findByNameOrderByNameAsc(name: kotlin.String):List<Companies>
    fun findByNameOrderByNameDesc(name: kotlin.String):List<Companies>




}