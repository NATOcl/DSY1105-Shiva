package cl.duoc.tecmundo.model

object Validators{

    private val NAME_REGEX = Regex("^[\\p{L}]+( [\\p{L}]+)*$")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")
    private val PHONE_REGEX = Regex("^(\\+?56)?9\\d{8}$")
    private val RUT_REGEX = Regex("^\\d{7,8}[0-9K]$")

    fun nameError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa tu nombre completo"
            trimmed.length < 3 -> "Nombre demasiado corto, debe tener mas de 3 caracteres"
            !NAME_REGEX.matches(trimmed) -> "Usa solo letras, sin números ni símbolos"
            else -> null
        }
    }

    fun rutError(value: String): String? {
        if (value.isBlank()) return "Ingresa tu RUT"
        val  cleanValue = cleanRut(value)
        if (!RUT_REGEX.matches(cleanValue)) return "Formato inválido. Ej: 12.345.678-9"
        val bodyDigits = cleanValue.dropLast(1)
        val verifier = cleanValue.last()
        return if (computeDv(bodyDigits) == verifier) null else "El RUT no es válido"
    }

}