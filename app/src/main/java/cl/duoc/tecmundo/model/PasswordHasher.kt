package cl.duoc.tecmundo.model

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

// cifra la clave para no guardarla en texto plano (PBKDF2 con sal, funciona desde Android 7)
object PasswordHasher {

    private const val ALGORITHM = "PBKDF2WithHmacSHA1"
    private const val ITERATIONS = 10_000
    private const val KEY_BITS = 256

    // sal aleatoria: hace que dos claves iguales no den el mismo resultado
    fun newSalt(): String {
        val bytes = ByteArray(16)
        SecureRandom().nextBytes(bytes)
        return toHex(bytes)
    }

    // convierte la clave en un texto cifrado usando la sal
    fun hash(password: String, saltHex: String): String {
        val spec = PBEKeySpec(password.toCharArray(), fromHex(saltHex), ITERATIONS, KEY_BITS)
        return toHex(SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).encoded)
    }

    // revisa si la clave escrita coincide con la guardada
    fun verify(password: String, saltHex: String, expectedHash: String): Boolean =
        MessageDigest.isEqual(hash(password, saltHex).toByteArray(), expectedHash.toByteArray())

    private fun toHex(bytes: ByteArray) = bytes.joinToString("") { "%02x".format(it) }

    private fun fromHex(hex: String) =
        ByteArray(hex.length / 2) { hex.substring(it * 2, it * 2 + 2).toInt(16).toByte() }
}
