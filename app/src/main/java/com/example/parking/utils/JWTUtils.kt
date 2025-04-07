package com.example.parking.utils

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.util.Date

object JWTUtils {
    private const val secretKey = "jddjkcascascovsdihrgvijveoijierjgvioerj"

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

    fun validarToken(token: String?): Boolean {
        return try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor("jddjkcascascovsdihrgvijveoijierjgvioerj".toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body

            val expiration = claims.expiration
            expiration.before(Date())
        } catch (e: Exception) {
            true
        }
    }
}
