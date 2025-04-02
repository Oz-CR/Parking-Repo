package com.example.parking.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

object JWTUtils {
    private const val secretKey = "lkadjgñdsgnasjdgndngldiflkkkkkkklavgfvhjmggtñhy7u"

    fun getUserIdFromToken(token: String): String {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body
            claims["id"].toString()
        } catch (e: Exception) {
            ""
        }
    }
}
