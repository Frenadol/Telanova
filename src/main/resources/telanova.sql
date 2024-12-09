-- Estructura de tabla para la tabla "almacen"
CREATE TABLE IF NOT EXISTS "almacen" (
  "id_almacen" INT NOT NULL,
  "nombre_almacen" VARCHAR(80) NOT NULL
);

-- Estructura de tabla para la tabla "cliente"
CREATE TABLE IF NOT EXISTS "cliente" (
  "id_cliente" INT NOT NULL,
  "cartera" DOUBLE NOT NULL
);

-- Estructura de tabla para la tabla "cliente_prenda"
CREATE TABLE IF NOT EXISTS "cliente_prenda" (
  "id_prenda" INT NOT NULL,
  "id_cliente" INT NOT NULL,
  "fecha_compra" VARCHAR(50) NOT NULL,
  "cantidad" INT DEFAULT NULL
);

-- Estructura de tabla para la tabla "prendas"
CREATE TABLE IF NOT EXISTS "prendas" (
  "id_prenda" INT NOT NULL,
  "nombre_prenda" VARCHAR(100) NOT NULL,
  "talla_prenda" VARCHAR(20) NOT NULL,
  "color_prenda" VARCHAR(50) NOT NULL,
  "descripcion" VARCHAR(200) NOT NULL,
  "precio" DOUBLE NOT NULL,
  "id_almacen" INT DEFAULT NULL,
  "imagen_prenda" BLOB NOT NULL,
  "cantidad" INT NOT NULL,
  "categoria" VARCHAR(200) NOT NULL
);

-- Estructura de tabla para la tabla "trabajador"
CREATE TABLE IF NOT EXISTS "trabajador" (
  "id_trabajador" INT NOT NULL,
  "estrabajador" BOOLEAN NOT NULL,
  "fechacontrato" VARCHAR(50) NOT NULL
);

-- Estructura de tabla para la tabla "trabajadores_prenda"
CREATE TABLE IF NOT EXISTS "trabajadores_prenda" (
  "id_trabajador" INT NOT NULL,
  "id_prenda" INT NOT NULL
);

-- Estructura de tabla para la tabla "usuario"
CREATE TABLE IF NOT EXISTS "usuario" (
  "id_usuario" INT NOT NULL,
  "nombre_usuario" VARCHAR(90) NOT NULL,
  "contraseña" TEXT NOT NULL,
  "gmail" VARCHAR(60) NOT NULL,
  "imagen_perfil" BLOB DEFAULT NULL
);