-- Agregar columna estadoPago a la tabla Pago
ALTER TABLE Pago
ADD COLUMN estadoPago VARCHAR(16) DEFAULT 'Activo';

-- Actualizar registros existentes (si los hay) para que tengan el estado 'Activo'
UPDATE Pago SET estadoPago = 'Activo' WHERE estadoPago IS NULL;

-- Hacer la columna NOT NULL después de actualizar los registros existentes
ALTER TABLE Pago
ALTER COLUMN estadoPago SET NOT NULL;

-- Agregar constraint para validar que solo se permitan valores 'Activo' o 'Cancelado'
ALTER TABLE Pago
ADD CONSTRAINT chkPagoEstadoPago CHECK (estadoPago IN ('Activo', 'Cancelado'));