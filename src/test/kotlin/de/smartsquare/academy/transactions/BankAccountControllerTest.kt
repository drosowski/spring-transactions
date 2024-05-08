package de.smartsquare.academy.transactions

import org.amshove.kluent.`should be`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import java.math.BigDecimal

@AutoConfigureMockMvc
@SpringBootTest
class BankAccountControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repo: BankAccountRepository

    @BeforeEach
    fun setUp() {
        repo.save(BankAccount(accountNumber = 4711, balance = BigDecimal.ZERO))
    }

    @Test
    fun `should deposit funds`() {
        mockMvc
            .post("/api/bankaccount/deposit/4711/10")
            .andExpect {
                status { isOk() }
            }

        val bankAccount = repo.findBankAccountByAccountNumber(4711)

        bankAccount?.balance `should be` BigDecimal.valueOf(10)
    }
}
