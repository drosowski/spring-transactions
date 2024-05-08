package de.smartsquare.academy.transactions

import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BankService(
    private val repo: BankAccountRepository,
    private val userNotificationService: UserNotificationService,
) {
    fun createAccount(
        accountNumber: Int,
        initialBalance: BigDecimal = BigDecimal.ZERO,
    ): BankAccount {
        return repo.save(BankAccount(accountNumber = accountNumber, balance = initialBalance))
    }

    fun getAccount(accountNumber: Int): BankAccount? {
        return repo.findBankAccountByAccountNumber(accountNumber)
    }

    fun depositFunds(
        accountNumber: Int,
        amount: BigDecimal,
    ) {
        val account =
            repo.findBankAccountByAccountNumber(accountNumber)
                ?: throw IllegalArgumentException("No such account with number $accountNumber")

        repo.save(account.copy(balance = account.balance + amount))
    }

    fun transferFunds(
        sourceAccountNumber: Int,
        receivingAccountNumber: Int,
        funds: BigDecimal,
    ) {
        val sourceAccount =
            repo.findBankAccountByAccountNumber(sourceAccountNumber)
                ?: throw IllegalArgumentException("No such account with number $sourceAccountNumber")
        val receivingAccount =
            repo.findBankAccountByAccountNumber(receivingAccountNumber)
                ?: throw IllegalArgumentException("No such account with number $receivingAccountNumber")

        repo.save(sourceAccount.copy(balance = sourceAccount.balance - funds))
        repo.save(receivingAccount.copy(balance = receivingAccount.balance + funds))

        userNotificationService.notifyUser(sourceAccount.ownerId, "$funds sent")
        userNotificationService.notifyUser(receivingAccount.ownerId, "$funds received")
    }

    // Ohne @Transactional!
    fun transferWithEscrow(
        sourceAccountNumber: Int,
        receivingAccountNumber: Int,
        funds: BigDecimal,
        escrowAccountNumber: Int,
    ) {
        transferFunds(sourceAccountNumber, escrowAccountNumber, funds)
        transferFunds(escrowAccountNumber, receivingAccountNumber, funds)
    }
}
