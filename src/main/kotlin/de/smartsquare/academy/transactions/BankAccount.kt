package de.smartsquare.academy.transactions

import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.math.BigDecimal
import java.util.UUID

@Entity
data class BankAccount(
    @Id
    val id: UUID = UUID.randomUUID(),
    val accountNumber: Int,
    val balance: BigDecimal,
    val ownerId: String = "maxmuster",
)
