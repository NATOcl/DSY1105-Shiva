package cl.duoc.tecmundo.model

// Los dos tipos de perfiles posibles al crear una cuenta
enum class ProfileType(val label: String){
    PRODUCTOR("Soy productor."),
    APLICADOR("Soy aplicador.")
}