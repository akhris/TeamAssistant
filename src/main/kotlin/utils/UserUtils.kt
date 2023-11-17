package utils

import java.net.InetAddress

object UserUtils {
    fun getUserName(): String {
        val domainName = InetAddress.getLocalHost().hostName
        val userName = System.getProperty("user.name")

        return "$domainName.$userName"
    }
}