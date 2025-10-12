INSERT INTO Cancha (idCancha, nombre, descripcion, precioHora, estadoCancha)
VALUES
    (1, 'Cancha #1', 'Cancha sintética de 40x60m2', 80.0, 'Disponible'),
    (2, 'Cancha #2', 'Cancha sintética de 40x60m2', 95.0, 'Disponible'),
    (3, 'Cancha #3', 'Cancha sintética de 40x60m2', 100.0, 'Mantenimiento');

INSERT INTO Usuario (rol, nombre, apellidoPaterno, apellidoMaterno, dni, telefono, email, activo, contrasena)
VALUES
    ('Usuario', 'Diego', 'Llacsahuanga', 'Buques', '73266267', '976849906', 'llacsahuanga.buques@gmail.com', True, '123456'),
    ('Usuario', 'Kevin', 'Huanca', 'Fernandez', '73266264', '976849916', 'kev.huanca@gmail.com', True, '123456');

