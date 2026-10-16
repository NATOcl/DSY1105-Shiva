# Shiva – Terrana (mockup)

Maqueta de una app de agricultura hecha con **Kotlin** y **Jetpack Compose**, organizada en **MVC**.
Es solo frontend: los formularios no envían nada. El único botón que funciona es **"Comenzar ahora"**, que lleva al registro.

## Pantallas

1. **Bienvenida:** foto de fondo, título y botón "Comenzar ahora".
2. **Registro:** formulario con checkbox de términos. El botón "Crear mi cuenta" se activa solo al marcar el check.

## Estructura de carpetas

```
app/src/main/
├── AndroidManifest.xml
├── java/cl/duoc/tecmundo/
│   ├── controller/
│   ├── model/
│   ├── view/
│   ├── ui/theme/
│   └── MainActivity.kt
└── res/
    ├── drawable/
    ├── drawable-nodpi/
    ├── mipmap-*/
    ├── values/
    └── xml/
```

### Código (`java/cl/duoc/tecmundo/`)

| Carpeta / archivo | Qué va aquí |
|---|---|
| `controller/` | Las pantallas como Activities. Deciden qué se muestra y qué pasa al tocar un botón. Aquí están `WelcomeActivity` (la que abre primero) y `RegisterActivity`. |
| `model/` | Los datos y el estado. `RegisterFormState` guarda lo que se escribe en el formulario y si se aceptaron los términos. |
| `view/` | Lo que se ve en pantalla, hecho con Compose. `WelcomeScreen`, `RegisterScreen` y `TerranaComponents` (logo, botón con flecha y campo de texto reutilizables). |
| `ui/theme/` | Colores, tipografía y tema. Los colores de Terrana están en `Color.kt`. |
| `MainActivity.kt` | Ejercicio anterior (formulario simple). Ya no es la pantalla de inicio. |

### Recursos (`res/`)

| Carpeta | Qué va aquí |
|---|---|
| `drawable/` | Íconos en vector: logo (`ic_logo`), flecha, volver y ojo de contraseña. |
| `drawable-nodpi/` | La foto de fondo de la portada: `bg_campo.jpg`. |
| `mipmap-*/` | Íconos de la app (launcher) en distintos tamaños. |
| `values/` | `strings.xml` (todos los textos), `colors.xml` y `themes.xml`. |
| `xml/` | Reglas de respaldo de Android. No se tocan. |

## Cosas que se cambian seguido

- **Textos de las pantallas:** `res/values/strings.xml`.
- **Foto de la portada:** reemplazar `res/drawable-nodpi/bg_campo.jpg` (mismo nombre, en minúsculas).
- **Colores:** `ui/theme/Color.kt`.
- **Texto de términos y condiciones:** en `strings.xml`, los tres textos que empiezan con `terms_`.

## Cómo abrir el proyecto

```bash
git clone -b feature/shiva-mockup https://github.com/NATOcl/DSY1105-Shiva.git
```

1. Abrir la carpeta en **Android Studio** (File → Open).
2. Esperar a que Gradle sincronice.
3. Ejecutar en un emulador o dispositivo Android.

## Ramas

- `main`: versión base del proyecto.
- `feature/shiva-mockup`: pantallas de bienvenida y registro de Terrana.
