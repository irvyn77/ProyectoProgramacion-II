## Requisitos y Configuración

1. Clonar el repositorio.
2. Ejecutar el script `script_base_datos.sql` incluido en la raíz del repositorio para crear las tablas `ventas` y `errores_procesamiento`.
3. Ajustar credenciales en la clase de conexión JDBC si es necesario:
   - **URL:** `jdbc:postgresql://localhost:5432/tienda_jdbc`
   - **Usuario:** `postgres`

## Compilación y Ejecución

1. Limpiar y compilar el proyecto:

```bash
mvn clean package