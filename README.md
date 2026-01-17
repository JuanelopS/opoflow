
*Este proyecto es un ejercicio continuo de refactorización y mejora hacia un código más limpio.*

# 🚀 OpoFlow

**OpoFlow** es un motor de gestión de exámenes para oposiciones desarrollado en Java. Este proyecto nace con el objetivo de aplicar principios de **Clean Architecture** y **SOLID**, priorizando el desacoplamiento y la mantenibilidad del código.

## 🎯 Filosofía del Proyecto

A diferencia de las aplicaciones monolíticas tradicionales, OpoFlow se ha diseñado para que el "núcleo de negocio" (la lógica de los exámenes) sea totalmente **agnóstico** de la infraestructura:

* **Independencia de la UI**: La lógica no sabe si el usuario está en una consola, una app de escritorio o un navegador web.
* **Persistencia Flexible**: Gracias al **Patrón Repository**, el origen de los datos es intercambiable. Podemos usar archivos planos (`.txt`, `.csv`) o migrar a bases de datos relacionales sin tocar la lógica de servicio.
* **Diseño Robusto**: Uso de copias defensivas, encapsulamiento y validación de tipos mediante Enums.

## 🛠️ Stack Tecnológico y Arquitectura

* **Lenguaje**: Java 17+
* **Arquitectura**: Desacoplada por capas (Model, Repository, Service, UI).
* **Patrones de Diseño**:
    * **Repository Pattern**: Abstracción total de la fuente de datos.
    * **Service Layer**: Centralización de reglas de negocio (corrección, barajado aleatorio, filtrado jerárquico).
    * **Dependency Injection (Manual)**: Para facilitar el testing y el intercambio de componentes.

## 📋 Características Implementadas

- [x] Carga dinámica de preguntas desde ficheros.
- [x] Filtrado inteligente por temas y bloques (Jerarquía en Enums).
- [x] Aleatoriedad de preguntas y opciones (Shuffle adaptativo).
- [x] Interfaz de consola desacoplada.
- [x] Validación de respuestas mediante índices dinámicos.
- [ ] Histórico de progreso y estadísticas (En desarrollo...).

