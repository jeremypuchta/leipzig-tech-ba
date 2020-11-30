package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Companies
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*


@Repository
interface Companies_Repository : JpaRepository<Companies, Long> {

    @Query("SELECT id,name ,branche,street,plz,city,website,phonenumber,email FROM companies c where c.name = :name ", nativeQuery = true)
    fun findByName(@Param("name") name: String): List<Companies>

    //fuzzy_searching
    @Query("SELECT *,LEVENSHTEIN(name,:name) FROM companies c Where LEVENSHTEIN(c.name,:name)< 4 ORDER BY LEVENSHTEIN(c.name,:name)ASC LIMIT 3 ", nativeQuery = true)
    fun findByName_fuzzy(@Param("name") name: String): List<Companies>

}