\# ProyectoProgramacion-II: Sistema de Procesamiento de Ventas



Aplicación en Java con arquitectura en capas y soporte Maven para la lectura, validación y persistencia de datos de ventas multiformato (JSON, XML), integrando PostgreSQL y generación automatizada de reportes.





\- \*\*JDK:\*\* Java 17 o superior

\- \*\*Gestor de dependencias:\*\* Apache Maven 3.x

\- \*\*Base de Datos:\*\* PostgreSQL 13+

\- \*\*IDE:\*\* NetBeans 17+ (o cualquier IDE Java compatible)



1\. Crear la base de datos `tienda\_jdbc` en PostgreSQL.

2\. Ejecutar el script `script\_base\_datos.sql` incluido en la raíz del repositorio para crear las tablas `ventas` y `errores\_procesamiento`.

3\. Ajustar credenciales en la clase de conexión JDBC si es necesario:

&#x20;  - \*\*URL:\*\* `jdbc:postgresql://localhost:5432/tienda\_jdbc`

&#x20;  - \*\*Usuario:\*\* `postgres`





1\. Limpiar y compilar el proyecto:

&#x20;  ```bash

&#x20;  mvn clean package

