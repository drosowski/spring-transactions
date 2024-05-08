package de.smartsquare.academy.transactions

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface BankAccountRepository : JpaRepository<BankAccount, UUID> {
    fun findBankAccountByAccountNumber(accountNumber: Int): BankAccount?
}
