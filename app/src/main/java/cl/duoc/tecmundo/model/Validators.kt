package cl.duoc.tecmundo.model

// aca viven todas las reglas de validacion, sin nada de android para testear facil
object Validators {

    // patrones que usamos para revisar cada campo
    private val NAME_REGEX = Regex("^[\\p{L}]+( [\\p{L}]+)*$")
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")
    // celular chileno: +56 9 XXXX XXXX (tambien vale 56912345678 o 912345678)
    private val PHONE_REGEX = Regex("^(\\+?56)?9\\d{8}$")
    private val RUT_REGEX = Regex("^\\d{7,8}[0-9K]$")

    // nombre: solo letras y espacios, nada de numeros ni simbolos
    fun nameError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa tu nombre completo"
            trimmed.length < 3 -> "El nombre es demasiado corto"
            !NAME_REGEX.matches(trimmed) -> "Usa solo letras, sin números ni símbolos"
            else -> null
        }
    }

    // rut: revisa el formato y que el digito verificador cuadre
    fun rutError(value: String): String? {
        if (value.isBlank()) return "Ingresa tu RUT"
        // sacamos puntos y guion para revisar solo los datos
        val cleanValue = cleanRut(value)
        if (!RUT_REGEX.matches(cleanValue)) return "Formato inválido. Ej: 12.345.678-9"
        val bodyDigits = cleanValue.dropLast(1)
        val verifier = cleanValue.last()
        return if (computeDv(bodyDigits) == verifier) null else "El RUT no es válido"
    }

    // correo: tiene que verse como usuario@dominio.cl
    fun emailError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa tu correo electrónico"
            !EMAIL_REGEX.matches(trimmed) -> "Ingresa un correo válido. Ej: nombre@correo.com"
            else -> null
        }
    }

    // telefono: solo celulares chilenos
    fun phoneError(value: String): String? {
        if (value.isBlank()) return "Ingresa tu teléfono"
        val digitsOnly = value.replace(" ", "")
        return if (PHONE_REGEX.matches(digitsOnly)) null else "Formato inválido. Ej: +56 9 1234 5678"
    }

    // finca: no puede quedar vacia
    fun farmError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa el nombre de tu finca"
            trimmed.length < 2 -> "El nombre es demasiado corto"
            else -> null
        }
    }

    // clave: minimo 8 caracteres
    fun passwordError(value: String): String? =
        if (value.length >= 8) null else "Mínimo 8 caracteres"

    // nombre de la parcela: no puede quedar vacio (acepta numeros, ej: "Parcela 01")
    fun parcelNameError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa el nombre de la parcela"
            trimmed.length < 2 -> "El nombre es demasiado corto"
            else -> null
        }
    }

    // zona: texto referencial, sin ubicacion precisa
    fun zoneError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa la zona de la parcela"
            trimmed.length < 3 -> "La zona es demasiado corta"
            else -> null
        }
    }

    // cultivo: solo letras y espacios (ej: "Maíz", "Trigo candeal")
    fun cropError(value: String): String? {
        val trimmed = value.trim()
        return when {
            trimmed.isEmpty() -> "Ingresa el tipo de cultivo"
            trimmed.length < 3 -> "El cultivo es demasiado corto"
            !NAME_REGEX.matches(trimmed) -> "Usa solo letras, sin números ni símbolos"
            else -> null
        }
    }

    // superficie: numero entero de hectareas, mayor que 0
    fun hectaresError(value: String): String? {
        if (value.isBlank()) return "Ingresa la superficie en hectáreas"
        val number = value.toIntOrNull() ?: return "Ingresa solo números"
        return when {
            number <= 0 -> "La superficie debe ser mayor que 0"
            number > 10_000 -> "La superficie es demasiado grande"
            else -> null
        }
    }

    // ayudas del rut: primero dejamos solo numeros y la K, sin puntos ni guion
    fun cleanRut(value: String): String =
        value.uppercase().filter { it.isDigit() || it == 'K' }

    // calcula el digito verificador con modulo 11
    private fun computeDv(bodyDigits: String): Char {
        var sum = 0
        var factor = 2
        // multiplicamos de derecha a izquierda por 2,3,4,5,6,7 y vuelve a empezar
        for (digit in bodyDigits.reversed()) {
            sum += (digit - '0') * factor
            factor = if (factor == 7) 2 else factor + 1
        }
        // si da 11 el digito es 0, si da 10 es K
        return when (val remainder = 11 - (sum % 11)) {
            11 -> '0'
            10 -> 'K'
            else -> ('0' + remainder)
        }
    }

    // va poniendo puntos y guion mientras la persona escribe
    fun formatRut(input: String): String {
        val digits = cleanRut(input).take(9)
        if (digits.length <= 1) return digits
        val bodyDigits = digits.dropLast(1)
        val verifier = digits.last()
        val withDots = bodyDigits.reversed().chunked(3).joinToString(".").reversed()
        return "$withDots-$verifier"
    }
}
