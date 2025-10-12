ALTER TABLE MovimientoBoveda
ADD COLUMN saldo NUMERIC(6, 2) NOT NULL;

ALTER TABLE MovimientoBoveda
DROP CONSTRAINT movimientoboveda_cierrecajeroid_fkey;

ALTER TABLE MovimientoBoveda
RENAME COLUMN cierreCajeroId TO sesionCajeroId;

ALTER TABLE MovimientoBoveda
ADD CONSTRAINT movimientoboveda_sesioncajeroid_fkey
FOREIGN KEY (sesionCajeroId) REFERENCES SesionCajero(idSesionCajero);