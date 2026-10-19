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

    fun emailError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa tu correo electronico"
            !EMAIL_REGEX.matches(trimmed) -> "Ingresa un correo válido. Ej: nombre@correo.com"
            else -> null
        }
    }

    fun phoneError(value: String): String? {
        if (value.isBlank()) return "Ingresa tu telefono"
        val digitsOnly = value.replace(" ", "")
        return if (PHONE_REGEX.matches(digitsOnly)) null else "Formato inválido. Ej: +56 9 1234 5678"
    }

    fun  farmError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa el numero de tu campo-hectarea"
            trimmed.length < 2 -> "El nombre de tu campo es demasiado corto"
            else-> null
        }
    }

    fun passwordError(value: String): String? =
        if (value.length >= 8) null else "La contraseña debe tener una 8 caracteres"

    fun cleanRut(value: String): String =
        value.uppercase().filter { it.isDigit() || it == 'K' }

    private  fun computeDv(bodyDigits: String): Char {
        var sum = 0
        var factor = 2

        for (digit in bodyDigits.reversed()) {
            sum += (digit - '0') * factor
            factor = if (factor == 7) 2 else factor + 1
        }
        return  when (val remainder = 11 - (sum % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> ('0' + remainder)

        }
    }

    fun formatoRut(input: String): String{
        val digits = cleanRut(input).take(9)
        if (digits.length <= 1) return digits
        val bodyDigits = digits.dropLast(1)
        val verifier = digits.last()
        val withDots = bodyDigits.reversed().chunked(3).joinToString(".").reversed()
        return  "$withDots-$verifier"

    }

}