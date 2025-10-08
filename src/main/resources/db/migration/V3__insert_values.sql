INSERT INTO Cancha (idCancha, nombre, descripcion, precioHora, estadoCancha)
VALUES
    (1, 'Cancha #1', 'Cancha sintética de 40x60m2', 80.0, 'Disponible'),
    (2, 'Cancha #2', 'Cancha sintética de 40x60m2', 95.0, 'Disponible'),
    (3, 'Cancha #3', 'Cancha sintética de 40x60m2', 100.0, 'Mantenimiento');

INSERT INTO Usuario (rol, nombre, apellidoPaterno, apellidoMaterno, dni, telefono, email, activo, contrasena)
VALUES
    ('Usuario', 'Diego', 'Llacsahuanga', 'Buques', '73266267', '976849906', 'llacsahuanga.buques@gmail.com', True, '123456'),
    ('Usuario', 'Kevin', 'Huanca', 'Fernandez', '73266264', '976849916', 'kev.huanca@gmail.com', True, '123456');

INSERT INTO Review (usuarioId, rating, comentario)
VALUES
    (1, '5', 'Excelente servicio, las canchas están en muy buen estado'),
    (2, '4', 'Buena experiencia, recomendable'),
    (1, '3', 'Servicio regular, podría mejorar');

INSERT INTO Cajero (nombre, apellidoPaterno, apellidoMaterno, dni, activo, contrasena)
VALUES
    ('María', 'García', 'López', '72345678', True, 'cajero123'),
    ('Carlos', 'Pérez', 'Sánchez', '71234567', True, 'cajero456');

INSERT INTO SesionCajero (cajeroId, montoInicial)
VALUES
    (1, 100.00),
    (2, 150.00);

INSERT INTO Reservacion (usuarioId, canchaId, cajeroId, tiempoInicio, dni, duracion, precioTotal, estadoReservacion)
VALUES
    (1, 1, 1, '2025-10-10 14:00:00', '73266267', INTERVAL '2 hours', 160.00, 'Confirmada'),
    (2, 2, 1, '2025-10-10 16:00:00', '73266264', INTERVAL '1 hour', 95.00, 'Confirmada'),
    (1, 1, 2, '2025-10-11 18:00:00', '73266267', INTERVAL '3 hours', 240.00, 'Pendiente'),
    (NULL, 3, NULL, '2025-10-12 10:00:00', '12345678', INTERVAL '2 hours', 200.00, 'Cancelada');

INSERT INTO Pago (reservacionId, sesionCajeroId, cantidadDinero, medioPago)
VALUES
    (1, 1, 160.00, 'Efectivo'),
    (2, 1, 95.00, 'Tarjeta'),
    (3, 2, 120.00, 'Yape'),
    (3, 2, 120.00, 'Plin');
