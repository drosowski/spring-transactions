package de.smartsquare.academy.transactions

import com.ninjasquad.springmockk.SpykBean
import io.mockk.clearAllMocks
import io.mockk.every
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal

@SpringBootTest
class BankServiceTest {
    @Autowired
    private lateinit var service: BankService

    @SpykBean
    private lateinit var repo: BankAccountRepository

    @SpykBean
    private lateinit var userNotificationService: UserNotificationService

    @AfterEach
    fun tearDown() {
        repo.deleteAll()
        clearAllMocks()
    }

    @Test
    fun `service and repo are working 👍`() {
        val bankAccount = service.createAccount(4711, initialBalance = BigDecimal.TEN)

        bankAccount.accountNumber `should be equal to` 4711
        bankAccount.balance `should be` BigDecimal.TEN
    }

    @Test
    @Disabled
    fun `simple transactional annotation works`() {
        // given
        service.createAccount(4711, initialBalance = BigDecimal.TEN)
        service.createAccount(4712)

        // when
        service.transferFunds(4711, 4712, BigDecimal.valueOf(5))

        // then
        service.getAccount(4711)?.balance `should be` BigDecimal.valueOf(5)
        service.getAccount(4712)?.balance `should be` BigDecimal.valueOf(5)
    }

    @Test
    @Disabled
    fun `transaction should roll back`() {
        // given
        service.createAccount(4711, initialBalance = BigDecimal.TEN)
        service.createAccount(4712)

        // when
        simulateErrorForAccount()
        runCatching { service.transferFunds(4711, 4712, BigDecimal.valueOf(5)) }

        // then
        service.getAccount(4711)?.balance `should be` BigDecimal.valueOf(10)
        service.getAccount(4712)?.balance `should be` BigDecimal.valueOf(0)
    }

    @Test
    @Disabled
    fun `transaction should still roll back`() {
        // given
        service.createAccount(4711, initialBalance = BigDecimal.TEN)
        service.createAccount(4712)
        service.createAccount(4713)

        // when
        simulateErrorForAccount()
        runCatching { service.transferWithEscrow(4711, 4712, BigDecimal.valueOf(5), 4713) }

        // then
        service.getAccount(4711)?.balance `should be` BigDecimal.valueOf(10)
        service.getAccount(4712)?.balance `should be` BigDecimal.valueOf(0)
    }

    @Test
    @Disabled
    fun `transaction should not roll back for NotificationException`() {
        // given
        service.createAccount(4711, initialBalance = BigDecimal.TEN)
        service.createAccount(4712)

        // when
        every {
            userNotificationService.notifyUser(
                any(),
                any(),
            )
        } throws (NotificationException("Could not send notification! 🤡"))
        runCatching { service.transferFunds(4711, 4712, BigDecimal.valueOf(5)) }

        // then
        service.getAccount(4711)?.balance `should be` BigDecimal.valueOf(5)
        service.getAccount(4712)?.balance `should be` BigDecimal.valueOf(5)
    }

    private fun simulateErrorForAccount() {
        every {
            repo.save(
                match { account ->
                    account.accountNumber == 4712
                },
            )
        } throws (RuntimeException("Something bad happened, uh oh! 😱"))
    }
}
