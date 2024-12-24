package org.lamysia.christmasgrapes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
