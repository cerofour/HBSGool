CREATE TABLE Usuario (
	idUsuario INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	nombre VARCHAR(40) NOT NULL,
	apellidoPaterno VARCHAR(40) NOT NULL,
	apellidoMaterno VARCHAR(40) NOT NULL,
	dni CHAR(8) UNIQUE NOT NULL,
	telefono CHAR(9) UNIQUE NOT NULL,
	email VARCHAR(255) UNIQUE NOT NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE,
	contrasena VARCHAR(255) NOT NULL,
	rol VARCHAR(16) NOT NULL DEFAULT 'ROLE_USER',
	CONSTRAINT chkUsuarioEmail CHECK (email ~ '^[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*@[a-zA-Z0-9_]+([.][a-zA-Z0-9_]+)*[.][a-zA-Z]{2,5}$'),
	CONSTRAINT chkUsuarioTelefono CHECK (telefono ~ '^\d{9}$'),
	CONSTRAINT chkUsuarioDni CHECK (dni ~ '^\d{8}$')

);


CREATE TABLE Cancha (
	idCancha SMALLINT PRIMARY KEY NOT NULL,
	nombre VARCHAR(100) UNIQUE NOT NULL,
	descripcion VARCHAR(120) NULL,
	precioHora NUMERIC(10, 2) NOT NULL,
	estadoCancha VARCHAR(16) NOT NULL,
	CONSTRAINT chkCanchaPrecioHora CHECK (precioHora >= 0)

);


CREATE TABLE Banner (
	banner VARCHAR(100) UNIQUE NOT NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE

);


CREATE TABLE Cajero (
	idCajero SMALLINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	usuarioId SMALLINT NOT NULL,
	activo BOOLEAN NOT NULL DEFAULT TRUE,
	FOREIGN KEY (usuarioId) REFERENCES Usuario(idUsuario)

);


CREATE TABLE Review (
	idReview INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	usuarioId INT NOT NULL,
	rating CHAR(1) NOT NULL,
	comentario VARCHAR(255) NOT NULL,
	creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (usuarioId) REFERENCES Usuario(idUsuario),
	CONSTRAINT chkReviewRating CHECK (rating IN ('1', '2', '3', '4', '5'))

);


CREATE TABLE Reservacion (
	idReservacion INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	usuarioId INT,
	canchaId SMALLINT NOT NULL,
	cajeroId SMALLINT,
	tiempoInicio TIMESTAMP UNIQUE NOT NULL,
	dni CHAR(8) NOT NULL,
	duracion INTERVAL NOT NULL,
	precioTotal NUMERIC(10, 2) NOT NULL,
	estadoReservacion VARCHAR(16) NOT NULL,
	FOREIGN KEY (usuarioId) REFERENCES Usuario(idUsuario),
	FOREIGN KEY (canchaId) REFERENCES Cancha(idCancha),
	FOREIGN KEY (cajeroId) REFERENCES Cajero(idCajero),
	CONSTRAINT chkReservacionPrecioTotal CHECK (precioTotal >= 0),
	CONSTRAINT chkReservacionDuracion CHECK (duracion >= INTERVAL '1 hour'),
	CONSTRAINT chkReservacionDni CHECK (dni ~ '^\d{8}$')


);


CREATE TABLE SesionCajero (
	idSesionCajero INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	cajeroId SMALLINT NOT NULL,
	montoInicial NUMERIC(6, 2) NOT NULL,
	fechaApertura TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	FOREIGN KEY (cajeroId) REFERENCES Cajero(idCajero),
	CONSTRAINT chkSesionCajeroMontoInicial CHECK (montoInicial >= 0)

);


CREATE TABLE Pago (
	idPago INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	reservacionId INT NOT NULL,
	sesionCajeroId INT NOT NULL,
	cantidadDinero NUMERIC(6, 2) NOT NULL,
	fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	medioPago VARCHAR(16) NOT NULL,
	estadoPago VARCHAR(16) NOT NULL DEFAULT 'PENDIENTE',
	FOREIGN KEY (reservacionId) REFERENCES Reservacion(idReservacion),
	FOREIGN KEY (sesionCajeroId) REFERENCES SesionCajero(idSesionCajero),
	CONSTRAINT chkPagoCantidadDinero CHECK (cantidadDinero >= 0),
    CONSTRAINT chkPagoEstadoPago CHECK (estadoPago in ('PENDIENTE', 'RECHAZADO', 'CONFIRMADO'))
);


CREATE TABLE CierreCajero (
	idCierreCajero INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	sesionCajeroId INT NOT NULL,
	fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	montoTeorico NUMERIC(6, 2) NOT NULL,
	montoReal NUMERIC(6, 2) NOT NULL,
	FOREIGN KEY (sesionCajeroId) REFERENCES SesionCajero(idSesionCajero),
	CONSTRAINT chkCierreCajeroMontoTeorico CHECK (montoTeorico >= 0),
	CONSTRAINT chkCierreCajeroMontoReal CHECK (montoReal >= 0)

);


CREATE TABLE ConfirmacionPagoRemoto (
	idConfirmacionPagoRemoto INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	pagoId INT NOT NULL,
	cajeroId INT NOT NULL,
	fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	evidencia VARCHAR NOT NULL,
	FOREIGN KEY (pagoId) REFERENCES Pago(idPago),
	FOREIGN KEY (cajeroId) REFERENCES Cajero(idCajero)


);


CREATE TABLE MovimientoBoveda (
	idMovimientoBoveda INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
	sesionCajeroId INT NOT NULL,
	tipoMovimientoBoveda VARCHAR(16) NOT NULL,
	motivo VARCHAR(32) NOT NULL,
	FOREIGN KEY (sesionCajeroId) REFERENCES SesionCajero(idSesionCajero)

);

