package de.smartsquare.academy.transactions

import org.springframework.stereotype.Service

@Service
class UserNotificationService {
    fun notifyUser(
        userId: String,
        message: String,
    ) {
        // imagine this would send a message to the user
    }
}
