CREATE DATABASE tienda_jdbc;


CREATE TABLE IF NOT EXISTS ventas (
    id_venta VARCHAR(50) PRIMARY KEY,
    fecha DATE NOT NULL,
    sucursal VARCHAR(100) NOT NULL,
    vendedor VARCHAR(100) NOT NULL,
    producto VARCHAR(100) NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario NUMERIC(10, 2) NOT NULL,
    total NUMERIC(10, 2) NOT NULL
);


CREATE TABLE IF NOT EXISTS errores_procesamiento (
    id SERIAL PRIMARY KEY,
    archivo_origen VARCHAR(100),
    id_registro VARCHAR(50),
    motivo VARCHAR(255),
    fecha_error TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);