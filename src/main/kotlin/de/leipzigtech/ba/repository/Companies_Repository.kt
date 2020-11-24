package de.leipzigtech.ba.repository

import de.leipzigtech.ba.model.Companies
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

@Repository
@Transactional(Transactional.TxType.MANDATORY)
interface Companies_Repository : JpaRepository<Companies, Long>