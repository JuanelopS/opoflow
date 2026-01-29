# 🚀 OpoFlow

**OpoFlow** es un ecosistema de gestión de exámenes para oposiciones desarrollado en Java. Este proyecto no es solo una herramienta de estudio; es un ejercicio de ingeniería de software centrado en la aplicación de principios **Clean Architecture** y **SOLID**, priorizando el desacoplamiento, la escalabilidad y la mantenibilidad.

## 🎯 Filosofía del Proyecto

El núcleo de **OpoFlow** se ha diseñado para ser totalmente **agnóstico** de la infraestructura. La lógica de negocio reside en un "Core" protegido que no depende de factores externos:

* **Independencia de la UI**: El motor de exámenes funciona igual en consola, escritorio o web.
* **Persistencia Enchufable**: Gracias al **Patrón Repository**, podemos alternar entre archivos planos, JSON o bases de datos SQL sin tocar una sola línea de lógica de servicio.
* **Diseño Robusto**: Uso intensivo de tipos fuertes (Enums), validaciones de seguridad y gestión de identificadores únicos (UUID).

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

### 📸 Vista Previa (Consola)
![Captura de pantalla de OpoFlow](https://i.imghippo.com/files/vTb6721pc.png)

---

## 🛤️ Roadmap: Futuros Añadidos y Evolución

OpoFlow está diseñado para crecer. Estos son los próximos hitos en el desarrollo, enfocados en la transición hacia un entorno profesional y distribuido:

### 1. Evolución de la Interfaz (Multi-UI)
- [ ] **Desktop Interface**: Implementación de una GUI moderna utilizando **JavaFX** o **Swing**.
- [ ] **Web API (Spring Boot)**: Migración a una arquitectura de microservicios exponiendo la lógica mediante una API REST.

### 2. Persistencia Profesional
- [ ] **SQL Migration**: Implementación de repositorios mediante **JDBC** o **Spring Data JPA** (PostgreSQL/H2).
- [ ] **NoSQL / JSON**: Soporte para almacenamiento documental mediante Jackson/Gson.
- [ ] **Cifrado de Seguridad**: Implementación de **BCrypt** para el hashing de contraseñas.

### 3. Inteligencia de Negocio (Opo-Logic)
- [ ] **Simulacro con Tiempo**: Modo de examen con cronómetro activo y gestión de hilos.
- [ ] **Algoritmo de Repaso Espaciado**: Priorización inteligente de preguntas basadas en la tasa de error histórica.
- [ ] **Exportación de Reportes**: Generación de informes en formato **PDF** o **Excel**.

### 4. Calidad y DevOps
- [ ] **Testing Unitario**: Cobertura completa de la capa de servicios mediante **JUnit 5** y **Mockito**.
- [ ] **Contenerización**: Despliegue del ecosistema mediante **Docker**.

---
*Este proyecto es un testimonio de cómo un desarrollo simple puede escalar a una arquitectura empresarial si se aplican los principios correctos desde la primera línea de código.*
