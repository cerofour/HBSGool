ALTER TABLE MovimientoBoveda
DROP COLUMN userId;

ALTER TABLE MovimientoBoveda
ADD COLUMN usuarioId INT;

ALTER TABLE MovimientoBoveda
ADD CONSTRAINT Fk_MovimientoBoveda_UsuarioId
FOREIGN KEY (usuarioId)
REFERENCES Usuario (idUsuario)