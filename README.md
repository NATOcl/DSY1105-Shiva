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
  <a href="https://git-scm.com" target="_blank">
    <img src="https://img.shields.io/badge/Git-F05032?style=for-the-badge&logo=git&logoColor=white" alt="Git">
  </a>
  <a href="https://github.com/NATOcl" target="_blank">
    <img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white" alt="GitHub">
  </a>
</p>

---

## Proyecto: Shiva

**Shiva** es una aplicación móvil para el agro que conecta a **productores agrícolas** con **aplicadores de servicios** (fumigación, fertilización, monitoreo aéreo, entre otros).

La idea es simple: el productor publica lo que necesita en su parcela y el aplicador encuentra trabajos cercanos, revisa los detalles y envía una oferta.

### ¿Qué puedes hacer?

**Como productor**
- Crear una cuenta e iniciar sesión.
- Registrar tus parcelas con ubicación referencial, cultivo, superficie, disponibilidad de agua, obstáculos y una foto.
- Revisar los detalles de cada parcela y eliminarla cuando ya no la necesites.
- Publicar solicitudes de aplicación indicando producto, superficie, horario y comentarios.

**Como aplicador**
- Completar un perfil con los servicios y productos que aplicas, horarios, cultivos, zonas de cobertura, capacidad y experiencia.
- Activar o desactivar tu disponibilidad para recibir nuevas solicitudes.
- Revisar solicitudes cercanas y enviar una oferta con tu precio por hectárea.

**Como administrador**
- Ver un resumen de solo lectura con usuarios, aplicadores y solicitudes de ejemplo para revisar el funcionamiento del MVP.

### Características principales

- Registro con validación de datos (RUT, correo, teléfono y contraseña) y aceptación de términos.
- Acceso diferenciado según el tipo de perfil.
- Información guardada en el dispositivo y sincronizada con el servicio en la nube cuando hay conexión.
- Interfaz pensada para uso en terreno, con navegación simple y mensajes claros.

> Proyecto académico en etapa de MVP. Algunos datos del panel de administración son simulados y no corresponden a personas reales.

### Requisitos

- Android Studio.
- Android 7.0 o superior.
- Conexión a internet para sincronizar la información con el servicio en la nube.

### Cómo ejecutar

1. Clona el repositorio.
2. Abre el proyecto en Android Studio.
3. Espera a que se complete la sincronización de Gradle.
4. Ejecuta la app en un emulador o en un dispositivo Android.

### Pruebas

El proyecto incluye pruebas unitarias para las reglas de validación de los formularios.

### Ramas

- `main`: versión estable del proyecto.
- `feature/shiva-mockup`: sección experimental del proyecto.
- `backup`: respaldo de versiones anteriores.

---

<p align="center">Hecho con ❤️ para DSY1105</p>
