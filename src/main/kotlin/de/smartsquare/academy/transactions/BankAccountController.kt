package de.smartsquare.academy.transactions

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/bankaccount")
class BankAccountController(
    private val service: BankService,
) {
    @PostMapping("/deposit/{accountNumber}/{amount}")
    fun deposit(
        @PathVariable accountNumber: Int,
        @PathVariable amount: BigDecimal,
    ) {
        service.depositFunds(accountNumber, amount)
    }
}
