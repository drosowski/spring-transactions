package de.smartsquare.academy.transactions

import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class InterestCalculator(
    private val repo: BankAccountRepository,
) {
    // @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    fun calculate() {
        repo.findAll().forEach { bankAccount ->
            // this should happen in a transaction
            val interest = bankAccount.balance.times(BigDecimal.valueOf(0.03))
            repo.save(bankAccount.copy(balance = bankAccount.balance + interest))
        }
    }
}
