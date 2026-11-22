package cl.duoc.tecmundo.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

// StringSpec: cada test se escribe como un texto entre comillas
class ValidatorsTest : StringSpec({

    "passwordError: 7 caracteres no alcanza" {
        // Ejecutar: llamamos a tu funcion con una clave corta
        val resultado = Validators.passwordError("1234567")

        // Verificar: si no es igual, el test falla (rojo)
        resultado shouldBe "Mínimo 8 caracteres"
    }

    "passwordError: 8 caracteres es lo minimo" {
        // null significa "sin error", o sea, clave valida
        Validators.passwordError("12345678") shouldBe null
    }

    "isAdminEmail: reconoce el correo de administrador" {
        Validators.isAdminEmail("jefe@adminshiva.com") shouldBe true
    }
})