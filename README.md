<h1 align="center">DSY1105 – Desarrollo de Aplicaciones Móviles</h1>

<p align="center">
  Repositorio oficial de la asignatura <b>DSY1105 – Desarrollo de Aplicaciones Móviles</b>
</p>

<p align="center">
  <a href="https://developer.android.com/studio" target="_blank">
    <img src="https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white" alt="Android Studio">
  </a>
  <a href="https://kotlinlang.org" target="_blank">
    <img src="https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin">
  </a>
  <a href="https://developer.android.com/jetpack/compose" target="_blank">
    <img src="https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose">
  </a>
  <a href="https://m3.material.io" target="_blank">
    <img src="https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material Design 3">
  </a>
  <a href="https://gradle.org" target="_blank">
    <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white" alt="Gradle">
  </a>
  <a href="https://git-scm.com" target="_blank">
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git">
  </a>
  <a href="https://github.com/NATOcl" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
</p>

---

## Proyecto: Shiva

**Shiva** es una aplicación móvil de agricultura desarrollada como parte de las evaluaciones del curso.
Está hecha con **Kotlin** y **Jetpack Compose**, organizada en **MVC**.

Por ahora es solo frontend: los formularios no envían nada. El único botón que funciona es **"Comenzar ahora"**, que lleva al registro.

### Pantallas

1. **Bienvenida:** foto de fondo, título y botón "Comenzar ahora".
2. **Registro:** formulario con checkbox de términos. El botón "Crear mi cuenta" se activa solo al marcar el check.

## Tecnologías utilizadas

| Categoría | Tecnología |
|-----------|------------|
| Entorno de desarrollo | Android Studio |
| Lenguaje | Kotlin |
| Interfaz de usuario | Jetpack Compose + Material Design 3 |
| Arquitectura | MVC |
| Compilación | Gradle (Kotlin DSL) |
| Control de versiones | Git & GitHub |

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
| `ui/theme/` | Colores, tipografía y tema. Los colores de la app están en `Color.kt`. |
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

## Cómo clonar e importar el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/NATOcl/DSY1105-Shiva
```

Para traer directamente la rama del mockup:

```bash
git clone -b feature/shiva-mockup https://github.com/NATOcl/DSY1105-Shiva
```

### 2. Abrir en Android Studio

1. Abre **Android Studio**.
2. Selecciona **Open** y navega hasta la carpeta descargada (`DSY1105-Shiva`).
3. Espera a que **Gradle** sincronice todas las dependencias del proyecto.
4. Ejecuta la aplicación en un **emulador** o **dispositivo físico** Android.

## Ramas

- `main`: versión base del proyecto.
- `feature/shiva-mockup`: pantallas de bienvenida y registro.

---

<p align="center">Hecho con ❤️ para DSY1105</p>
