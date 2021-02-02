package de.leipzigtech.ba.model

import com.sun.istack.NotNull
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
//@EntityListeners(AuditingEntityListener.class)

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "companies")
data class Company (
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        @NotNull
        @Column(unique = true)
        var ref: String,
        val source: String,
        val name: String,
        var sector: String,
        val address :String,
        val plz :String,
        val city :String,
        val phonenumber :String,
        val website: String,
        val email: String,
        @CreatedDate
        @Column(name = "create_at", nullable = false, updatable = false)
        var create_at:LocalDateTime?= null,
        @LastModifiedDate
        @Column(name = "updated_at", nullable = false)
        var updated_at:LocalDateTime?= null,
        @Column(name="LATITUDE")
        var latitude:Float,
        @Column(name="LONGITUDE")
        var longitude:Float,
        @Column(name="district")
        var district:String?=""





)