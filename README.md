# 🚀 OpoFlow

**OpoFlow** es una aplicación de exámenes para oposiciones desarrollada en Java. Es un ejercicio de desarrollo siguiendo la **Clean Architecture** y **SOLID**, priorizando el desacoplamiento, la escalabilidad y la mantenibilidad.

## 🛠️ Stack Tecnológico Actual

* **Lenguaje**: Java 17+
* **Persistencia**: File-based system (CSV/Text) con filtrado mediante **Java Streams**.
* **Arquitectura**: Desacoplada por capas (Model, Repository, Service, UI).
* **Patrones clave**: Repository Pattern, Service Layer, Dependency Injection (Manual), y Functional Programming (Lambdas).

## 📋 Características Implementadas

- [x] **Gestión de Sesiones**: Sistema de Login y Registro de usuarios con persistencia.
- [x] **Motor de Exámenes**: Carga dinámica y filtrado jerárquico por temas y bloques.
- [x] **Algoritmo de Aleatoriedad**: Barajado adaptativo de preguntas y opciones de respuesta.
- [x] **Estadísticas en Tiempo Real**: Análisis de rendimiento por tema mediante *Collectors* y agrupamiento de datos.
- [x] **Gestión de Historial (CRUD)**: Sistema de mantenimiento de resultados con confirmación de borrado y validación de integridad.
- [x] **UI de Consola Robusta**: Interfaz numerada con manejo de excepciones y validación de entradas.

---

## ⚙ Configuración e Instalación

Para mantener el principio de **Separación de Configuración**, OpoFlow requiere un archivo de propiedades externo. Esto permite cambiar las rutas de los datos sin modificar el código fuente y protege la privacidad de tus rutas locales.

1. **Clona el repositorio**.
   ```bash 
   git clone https://github.com/JuanelopS/opoflow.git
   
2. **Preparar la configuración**:
    - Localiza el archivo `config.properties.default` en la raíz del proyecto.
    - Crea una copia y renómbrala simplemente como `config.properties`.
   
3. **Define tus rutas**: Edita el nuevo archivo con las rutas de tus archivos de datos locales. Ejemplo:
   ```properties
   repo.questions.path=questions.txt
   repo.results.path=results.txt
   repo.users.path=users.txt

4. Formato de las Preguntas
- Para que el motor de exámenes cargue correctamente los contenidos, el archivo de preguntas (repo.questions.path) debe seguir una estructura de **valores separados por puntos y coma ( ; )**.
- Cada línea representa una pregunta con el siguiente orden:
**Bloque;Tema;Enunciado;OpciónA;OpciónB;OpciónC;OpciónD;RespuestaCorrecta(en nº del 1 a X)**

- Ejemplo:
```txt
Bloque 1;TAI_ALL;¿Cuál es el puerto por defecto de HTTP?;80;443;21;8080;1
Bloque 2;ADVO_ALL;¿Qué principio SOLID se refiere a la segregación de interfaces?;S;O;L;I;4
```
*Si quieres añadir un tema deberás añadirlo a los Enums Opposition y OppositionTopic del paquete package **dev.jugapi.opoflow.model.exam** (pendiente de hacer dinámico, pero Gemini lo hace genial si le pasas un examen de oposición)*