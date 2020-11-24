package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Companies
import de.leipzigtech.ba.model.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface Location_Repository : JpaRepository<Location, Long>