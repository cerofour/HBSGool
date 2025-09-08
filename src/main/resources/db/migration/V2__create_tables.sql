
CREATE TABLE usuario (
	id_usuario UUID PRIMARY KEY NOT NULL,
	nombre VARCHAR(255) NOT NULL,
	apellido_paterno VARCHAR(255) NOT NULL,
	apellido_materno VARCHAR(255) NOT NULL,
	dni dni UNIQUE NOT NULL,
	telefono CHAR(9) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	activo BOOLEAN NOT NULL,
	CONSTRAINT chk_usuario_email CHECK (email ~ '^[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}$'),
	CONSTRAINT chk_usuario_telefono CHECK (telefono ~ '^\d{9}$')

);


CREATE TABLE rol (
	id_rol UUID PRIMARY KEY NOT NULL,
	rol VARCHAR(100) UNIQUE NOT NULL

);


CREATE TABLE estado_reservacion (
	id_estado_reservacion UUID PRIMARY KEY NOT NULL,
	estado_reservacion VARCHAR(100) UNIQUE NOT NULL

);


CREATE TABLE estado_cancha (
	id_estado_cancha UUID PRIMARY KEY NOT NULL,
	estado_cancha VARCHAR(100) UNIQUE NOT NULL

);


CREATE TABLE caja (
	id_caja UUID PRIMARY KEY NOT NULL,
	usuario_id UUID NOT NULL,
	total NUMERIC(6, 2) NOT NULL,
	fecha DATE NOT NULL DEFAULT CURRENT_DATE,
	FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
	CONSTRAINT chk_caja_fecha CHECK (fecha >= CURRENT_DATE),
	CONSTRAINT chk_caja_total CHECK (total >= 0)

);

CREATE TABLE usuario_rol (
    usuario_id UUID NOT NULL,
    rol_id UUID NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
    FOREIGN KEY (rol_id) REFERENCES rol(id_rol)
);


CREATE TABLE cancha (
	id_cancha UUID PRIMARY KEY NOT NULL,
	cancha VARCHAR(255) UNIQUE NOT NULL,
	descripcion TEXT NULL,
	ancho NUMERIC(5, 2) NULL,
	largo NUMERIC(5, 2) NULL,
	es_sintetico BOOLEAN NOT NULL,
	precio_hora NUMERIC(10, 2) NOT NULL,
	estado_cancha_id UUID NOT NULL,
	FOREIGN KEY (estado_cancha_id) REFERENCES estado_cancha(id_estado_cancha),
	CONSTRAINT chk_cancha_ancho CHECK (ancho >= 0),
	CONSTRAINT chk_cancha_largo CHECK (largo >= 0),
	CONSTRAINT chk_cancha_precio_hora CHECK (precio_hora >= 0)

);


CREATE TABLE reservacion (
	id_reservacion UUID PRIMARY KEY NOT NULL,
	usuario_id UUID UNIQUE NOT NULL,
	cancha_id UUID NOT NULL,
	estado_reservacion_id UUID NOT NULL,
	tiempo_inicio TIMESTAMP UNIQUE NOT NULL,
	duracion INTERVAL NOT NULL,
	dnis dni[] NOT NULL,
	precio_total NUMERIC(10, 2) NOT NULL,
	FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
	FOREIGN KEY (cancha_id) REFERENCES cancha(id_cancha),
	FOREIGN KEY (estado_reservacion_id) REFERENCES estado_reservacion(id_estado_reservacion),
	CONSTRAINT chk_reservacion_precio_total CHECK (precio_total >= 0)

);


CREATE TABLE review (
	id_review UUID PRIMARY KEY NOT NULL,
	usuario_id UUID NOT NULL,
	cancha_id UUID NOT NULL,
	rating SMALLINT NOT NULL,
	comentario TEXT NOT NULL,
	creado TIMESTAMP NOT NULL,
	FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
	FOREIGN KEY (cancha_id) REFERENCES cancha(id_cancha),
	CONSTRAINT chk_review_rating CHECK (rating >= 0 AND rating <= 5)

);


CREATE TABLE penalidad (
	id_penalidad UUID PRIMARY KEY NOT NULL,
	usuario_id UUID NOT NULL,
	reservacion_id UUID NOT NULL,
	razon TEXT NOT NULL,
	fecha_aplicada TIMESTAMP NOT NULL,
	FOREIGN KEY (usuario_id) REFERENCES usuario(id_usuario),
	FOREIGN KEY (reservacion_id) REFERENCES reservacion(id_reservacion)

);