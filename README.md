# MiPerfil

Aplicación Android desarrollada en **Kotlin** que simula un formulario de registro de perfil de usuario. El proyecto está compuesto por tres pantallas (Activities) que se comunican entre sí mediante `Intent`.

## ¿Cómo funciona?

### 1. Pantalla de Bienvenida (`MainActivity`)
Es la pantalla inicial de la app. Su única función es mostrar un mensaje de bienvenida y un botón **"Iniciar"** que lleva al usuario hacia el formulario de registro.

### 2. Registro de Perfil (`RegistroPerfilActivity`)
Formulario donde el usuario ingresa sus datos personales:

- Nombres
- Correo electrónico
- Teléfono
- Fecha de nacimiento
- Dirección

Además incluye:

- **Solicitud de permiso de cámara en tiempo de ejecución**: al presionar el botón "Tomar Foto", la app verifica si el permiso de cámara ya fue otorgado; si no, lo solicita al usuario usando la API `ActivityResultContracts.RequestPermission()`. El estado del permiso (concedido/denegado) se muestra en pantalla y se conserva aunque el usuario rote el dispositivo.
- **Validación de datos** antes de continuar, al presionar "Guardar":
  - Ningún campo puede estar vacío.
  - El correo debe tener un formato válido (`Patterns.EMAIL_ADDRESS`).
  - El teléfono debe contener solo dígitos (entre 8 y 15).
  - La fecha de nacimiento debe tener el formato `dd/MM/yyyy` y ser una fecha real (por ejemplo, rechaza `31/02/2020`).

Si algún dato es inválido, se muestra un `Toast` con el error correspondiente y no se avanza de pantalla. Si todos los datos son válidos, se envían a la siguiente Activity mediante un `Intent` explícito.

### 3. Perfil Guardado (`PerfilGuardadoActivity`)
Muestra un resumen ordenado con todos los datos ingresados por el usuario, incluyendo el estado del permiso de cámara. Desde aquí el usuario puede:

- **Regresar al inicio**: vuelve a la pantalla de bienvenida, limpiando el historial de navegación.
- **Registrar nuevo perfil**: vuelve al formulario para cargar otro perfil.


## Permisos utilizados

- `android.permission.CAMERA`: solicitado en tiempo de ejecución para simular la toma de una foto de perfil (declarado como `android:required="false"` en el manifiesto, por lo que la app funciona incluso en dispositivos sin cámara).

## Tecnologías

- **Lenguaje:** Kotlin
- **UI:** Views (XML) + View Binding manual (`findViewById`)
- **Arquitectura:** 3 Activities independientes comunicadas por `Intent`
- **Librerías principales:** AndroidX Core-KTX, AppCompat, Material Components, ConstraintLayout, Activity

## Requisitos

- Android Studio (con Android Gradle Plugin 9.0.1 o compatible)
- compileSdk / targetSdk: 36
- minSdk: 24

## Cómo ejecutar el proyecto

1. Clona este repositorio.
2. Ábrelo con Android Studio.
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la app en un emulador o dispositivo físico con Android 7.0 (API 24) o superior.

## Estudiante

- Jorge Luis Lugo Gonzalez #LG242867
