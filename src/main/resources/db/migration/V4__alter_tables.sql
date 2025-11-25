-- Table usuario
ALTER TABLE Usuario
DROP CONSTRAINT chkUsuarioDni;

ALTER TABLE Usuario
ALTER COLUMN dni TYPE VARCHAR(12);

ALTER TABLE Usuario
ADD CONSTRAINT chkUsuarioDni
CHECK (dni ~ '^[0-9]{8}$' OR dni ~ '^[0-9]{12}$');

-- Table Reservacion

ALTER TABLE Reservacion
DROP CONSTRAINT chkReservacionDni;

ALTER TABLE Reservacion
ALTER COLUMN dni TYPE VARCHAR(12);

ALTER TABLE Reservacion
ADD CONSTRAINT chkReservacionDni
CHECK (dni ~ '^[0-9]{8}$' OR dni ~ '^[0-9]{12}$');