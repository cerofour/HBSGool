# API de Pagos - Documentación

## Endpoints Implementados

### 1. Listar Pagos (GET)
```http
GET http://localhost:8080/api/pagos
GET http://localhost:8080/api/pagos?page=0&size=10&sort=fecha,desc
```

### 2. Filtrar Pagos por Reservación
```http
GET http://localhost:8080/api/pagos?reservacionId=1
```

### 3. Filtrar Pagos por Sesión de Cajero
```http
GET http://localhost:8080/api/pagos?sesionCajeroId=1
```

### 4. Filtrar Pagos por Medio de Pago
```http
GET http://localhost:8080/api/pagos?medioPago=Efectivo
```

### 5. Obtener Pago por ID (GET)
```http
GET http://localhost:8080/api/pagos/1
```

## Ejemplos de Request/Response

### Listar Pagos - Exitoso (200 OK)

**Request:**
```http
GET /api/pagos?page=0&size=10&sort=fecha,desc
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "idPago": 1,
      "reservacionId": 1,
      "sesionCajeroId": 1,
      "cantidadDinero": 120.00,
      "fecha": "2025-10-07T14:30:00",
      "medioPago": "Efectivo"
    },
    {
      "idPago": 2,
      "reservacionId": 2,
      "sesionCajeroId": 1,
      "cantidadDinero": 90.00,
      "fecha": "2025-10-07T15:00:00",
      "medioPago": "Tarjeta"
    },
    {
      "idPago": 3,
      "reservacionId": 3,
      "sesionCajeroId": 2,
      "cantidadDinero": 150.00,
      "fecha": "2025-10-07T16:30:00",
      "medioPago": "Yape"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false,
      "empty": false
    }
  },
  "totalElements": 3,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 3,
  "size": 10,
  "number": 0,
  "empty": false
}
```

### Obtener Pago por ID - Exitoso (200 OK)

**Request:**
```http
GET /api/pagos/1
```

**Response (200 OK):**
```json
{
  "idPago": 1,
  "reservacionId": 1,
  "sesionCajeroId": 1,
  "cantidadDinero": 120.00,
  "fecha": "2025-10-07T14:30:00",
  "medioPago": "Efectivo"
}
```

### Obtener Pago - No Encontrado (404 Not Found)

**Request:**
```http
GET /api/pagos/999
```

**Response (404 Not Found):**
(Sin body, solo status 404)

### Filtrar por Reservación - Exitoso (200 OK)

**Request:**
```http
GET /api/pagos?reservacionId=1
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "idPago": 1,
      "reservacionId": 1,
      "sesionCajeroId": 1,
      "cantidadDinero": 120.00,
      "fecha": "2025-10-07T14:30:00",
      "medioPago": "Efectivo"
    }
  ],
  "pageable": {...},
  "totalElements": 1,
  "totalPages": 1
}
```

**Uso típico:** Ver todos los pagos asociados a una reservación específica. Útil cuando una reservación tiene múltiples pagos parciales o para auditoría.

### Filtrar por Sesión de Cajero - Exitoso (200 OK)

**Request:**
```http
GET /api/pagos?sesionCajeroId=1
```

**Response:** Lista de todos los pagos procesados por un cajero en una sesión específica.

**Uso típico:** Auditoría de caja, cierre de sesión, reportes de cajero.

### Filtrar por Medio de Pago - Exitoso (200 OK)

**Request:**
```http
GET /api/pagos?medioPago=Yape
```

**Response:** Lista de pagos realizados con el método especificado.

**Uso típico:** Reportes de métodos de pago, estadísticas de preferencias de pago.

## Parámetros de Query

### Filtros Disponibles
- `reservacionId` (Integer) - Filtrar por ID de reservación
- `sesionCajeroId` (Integer) - Filtrar por ID de sesión de cajero
- `medioPago` (String) - Filtrar por método de pago (ej: "Efectivo", "Tarjeta", "Yape")

### Paginación
- `page` (Integer) - Número de página (empieza en 0). Default: 0
- `size` (Integer) - Tamaño de página. Default: 10
- `sort` (String) - Campo y dirección de ordenamiento. Default: "fecha,desc"

**Ejemplos de sort:**
- `sort=fecha,asc` - Ordenar por fecha ascendente (más antiguos primero)
- `sort=fecha,desc` - Ordenar por fecha descendente (más recientes primero)
- `sort=cantidadDinero,desc` - Ordenar por monto descendente
- `sort=medioPago,asc` - Ordenar por medio de pago alfabéticamente

## Campos de PagoDto

| Campo | Tipo | Descripción | Nullable |
|-------|------|-------------|----------|
| idPago | Integer | ID único del pago | No |
| reservacionId | Integer | ID de la reservación asociada | No |
| sesionCajeroId | Integer | ID de la sesión de cajero | No |
| cantidadDinero | BigDecimal | Monto pagado (máx 9999.99) | No |
| fecha | LocalDateTime | Fecha/hora del pago | No |
| medioPago | String | Método de pago utilizado | No |

### Valores típicos de `medioPago`
- `"Efectivo"` - Pago en efectivo
- `"Tarjeta"` - Pago con tarjeta de crédito/débito
- `"Yape"` - Pago por Yape
- `"Plin"` - Pago por Plin
- `"Transferencia"` - Transferencia bancaria
- `"Online"` - Pago online (genérico)

### Restricciones de `cantidadDinero`
- Valor mínimo: 0.00 (constraint en BD)
- Valor máximo: 9999.99 (NUMERIC(6,2))
- Formato: 2 decimales

## Pruebas con PowerShell

### Listar todos los pagos
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos" -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json | ConvertTo-Json -Depth 10
```

### Filtrar por reservación
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos?reservacionId=1" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Filtrar por sesión de cajero
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos?sesionCajeroId=1" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Filtrar por medio de pago
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos?medioPago=Efectivo" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Obtener un pago específico
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos/1" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Con paginación personalizada
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos?page=0&size=5&sort=cantidadDinero,desc" -UseBasicParsing | Select-Object -ExpandProperty Content
```

## Pruebas con cURL

### Listar todos
```bash
curl -X GET "http://localhost:8080/api/pagos"
```

### Filtrar por reservación
```bash
curl -X GET "http://localhost:8080/api/pagos?reservacionId=1"
```

### Filtrar por sesión de cajero
```bash
curl -X GET "http://localhost:8080/api/pagos?sesionCajeroId=1"
```

### Filtrar por medio de pago
```bash
curl -X GET "http://localhost:8080/api/pagos?medioPago=Yape"
```

### Obtener por ID
```bash
curl -X GET "http://localhost:8080/api/pagos/1"
```

## Códigos HTTP Esperados

- `200 OK` - Consulta exitosa
- `404 Not Found` - Pago no encontrado (solo para GET por ID)
- `500 Internal Server Error` - Error en el servidor

## Casos de Uso

### Caso 1: Ver historial de pagos de una reservación
```http
GET /api/pagos?reservacionId=5
```
**Utilidad:** Ver todos los pagos asociados a una reservación. Útil si una reservación se paga en múltiples partes o si hay reembolsos.

### Caso 2: Auditoría de caja (pagos de una sesión)
```http
GET /api/pagos?sesionCajeroId=3
```
**Utilidad:** Ver todos los pagos procesados por un cajero en su turno/sesión. Esencial para el cierre de caja y conciliación.

### Caso 3: Reporte de métodos de pago
```http
GET /api/pagos?medioPago=Yape
```
**Utilidad:** Estadísticas de preferencias de pago, reportes de comisiones por método.

### Caso 4: Pagos recientes
```http
GET /api/pagos?sort=fecha,desc&size=20
```
**Utilidad:** Ver los últimos 20 pagos procesados en el sistema.

### Caso 5: Pagos de mayor monto
```http
GET /api/pagos?sort=cantidadDinero,desc&size=10
```
**Utilidad:** Identificar las transacciones más grandes, análisis de ventas.

## Notas Importantes

1. **Filtros mutuamente excluyentes**: Si pasas múltiples filtros, solo se aplicará el primero en este orden de prioridad:
   1. reservacionId
   2. sesionCajeroId
   3. medioPago

2. **Todos los campos obligatorios**: Ningún campo puede ser null en la tabla Pago (todos son NOT NULL).

3. **Ordenamiento por defecto**: Los pagos se ordenan por `fecha` descendente (más recientes primero).

4. **Formato de fechas**: Las fechas se devuelven en formato ISO-8601: `2025-10-07T14:30:00`

5. **Límites de monto**: 
   - Mínimo: 0.00 (constraint de BD)
   - Máximo: 9999.99 (NUMERIC(6,2))

6. **Relaciones**: 
   - Cada pago está vinculado a una reservación
   - Cada pago está vinculado a una sesión de cajero
   - Estas relaciones son obligatorias (FK NOT NULL)

## Integración con Swagger

Cuando arranques la aplicación, los endpoints estarán disponibles en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- Busca el controller **pago-controller**
- Podrás probar todos los endpoints directamente desde el navegador

## 6. Cancelar Pago (DELETE)

### Endpoint
```http
DELETE http://localhost:8080/api/pagos/{id}
```

### Descripción
Cancela un pago cambiando su estado de "Activo" a "Cancelado" (soft delete). El registro NO se elimina de la base de datos, solo se marca como cancelado para mantener la auditoría.

### Ejemplos de Request/Response

#### Cancelar Pago - Exitoso (200 OK)

**Request:**
```http
DELETE /api/pagos/1
```

**Response (200 OK):**
```json
{
  "idPago": 1,
  "reservacionId": 1,
  "sesionCajeroId": 1,
  "cantidadDinero": 120.00,
  "fecha": "2025-10-07T14:30:00",
  "medioPago": "Efectivo",
  "estadoPago": "Cancelado"
}
```

#### Cancelar Pago - No Encontrado (404 Not Found)

**Request:**
```http
DELETE /api/pagos/999
```

**Response (404 Not Found):**
(Sin body)

#### Cancelar Pago Ya Cancelado - Error (400 Bad Request)

**Request:**
```http
DELETE /api/pagos/1
```
(El pago con ID 1 ya está cancelado)

**Response (400 Bad Request):**
(Sin body, indica que el pago ya estaba cancelado)

### Pruebas con PowerShell

```powershell
# Cancelar un pago
Invoke-WebRequest -Uri "http://localhost:8080/api/pagos/1" -Method DELETE -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Pruebas con cURL

```bash
# Cancelar un pago
curl -X DELETE "http://localhost:8080/api/pagos/1"
```

## Campo estadoPago

### Descripción
A partir de la versión 4 del sistema, todos los pagos tienen un campo `estadoPago` que indica si el pago está activo o ha sido cancelado.

| Campo | Tipo | Descripción | Valores Permitidos | Default |
|-------|------|-------------|-------------------|---------|
| estadoPago | String | Estado actual del pago | "Activo", "Cancelado" | "Activo" |

### Valores Posibles
- `"Activo"` - Pago válido y activo en el sistema
- `"Cancelado"` - Pago cancelado (soft delete), se mantiene para auditoría

### Comportamiento
- Todos los pagos nuevos se crean con estado "Activo"
- Los pagos existentes (antes de V4) tienen estado "Activo" por defecto
- Al llamar DELETE, el estado cambia a "Cancelado"
- Los pagos cancelados NO se eliminan de la base de datos
- Un pago cancelado NO puede volver a cancelarse (retorna 400)

## Códigos HTTP Actualizados

- `200 OK` - Consulta exitosa o cancelación exitosa
- `400 Bad Request` - Intento de cancelar un pago ya cancelado
- `404 Not Found` - Pago no encontrado
- `500 Internal Server Error` - Error en el servidor

## Casos de Uso Actualizados

### Caso 6: Cancelar un pago por error
```http
DELETE /api/pagos/5
```
**Utilidad:** Marcar un pago como cancelado sin eliminarlo. Útil cuando se registró un pago por error o cuando el cliente solicita la anulación.

### Caso 7: Ver todos los pagos incluyendo cancelados
```http
GET /api/pagos
```
**Utilidad:** Ver el historial completo de pagos (activos y cancelados) para auditoría.

## Notas Importantes sobre Cancelación

1. **Soft Delete**: Los pagos NO se eliminan físicamente de la base de datos. Solo cambia el estado a "Cancelado".

2. **Auditoría**: Se mantiene el registro completo del pago cancelado para fines de auditoría y reportes.

3. **Irreversible**: Una vez cancelado, el pago no puede reactivarse (requeriría crear un nuevo endpoint si fuera necesario).

4. **Relaciones intactas**: Las relaciones con Reservacion, SesionCajero y ConfirmacionPagoRemoto se mantienen.

5. **Ventajas del Soft Delete**:
   - ✅ Mantiene historial completo
   - ✅ No rompe integridad referencial
   - ✅ Permite reportes históricos precisos
   - ✅ Facilita auditorías y reconciliaciones

## Migración V4

La funcionalidad de cancelación requiere la migración `V4__estado_pago.sql` que:
- Agrega columna `estadoPago` a la tabla `Pago`
- Establece valor por defecto "Activo"
- Actualiza registros existentes
- Agrega constraint para validar valores ("Activo" o "Cancelado")
