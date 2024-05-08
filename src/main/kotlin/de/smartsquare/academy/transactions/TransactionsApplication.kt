package de.smartsquare.academy.transactions

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
class TransactionsApplication

fun main(args: Array<String>) {
    runApplication<TransactionsApplication>(*args)
}
