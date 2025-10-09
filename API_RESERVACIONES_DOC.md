# API de Reservaciones - Documentación

## Endpoints Implementados

### 1. Listar Reservaciones (GET)
```http
GET http://localhost:8080/api/reservaciones
GET http://localhost:8080/api/reservaciones?page=0&size=10&sort=tiempoInicio,desc
```

### 2. Filtrar Reservaciones por Usuario
```http
GET http://localhost:8080/api/reservaciones?usuarioId=1
```

### 3. Filtrar Reservaciones por Cancha
```http
GET http://localhost:8080/api/reservaciones?canchaId=2
```

### 4. Filtrar Reservaciones por Estado
```http
GET http://localhost:8080/api/reservaciones?estado=Confirmada
```

### 5. Filtrar Reservaciones por DNI
```http
GET http://localhost:8080/api/reservaciones?dni=73266267
```

### 6. Obtener Reservación por ID (GET)
```http
GET http://localhost:8080/api/reservaciones/1
```

## Ejemplos de Request/Response

### Listar Reservaciones - Exitoso (200 OK)

**Request:**
```http
GET /api/reservaciones?page=0&size=10&sort=tiempoInicio,desc
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "idReservacion": 1,
      "usuarioId": 1,
      "canchaId": 1,
      "cajeroId": null,
      "tiempoInicio": "2025-10-15T14:00:00",
      "dni": "73266267",
      "duracion": "2 hours",
      "precioTotal": 120.00,
      "estadoReservacion": "Confirmada"
    },
    {
      "idReservacion": 2,
      "usuarioId": 2,
      "canchaId": 2,
      "cajeroId": 1,
      "tiempoInicio": "2025-10-14T16:00:00",
      "dni": "73266264",
      "duracion": "1 hour 30 minutes",
      "precioTotal": 90.00,
      "estadoReservacion": "Pendiente"
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
  "totalElements": 2,
  "totalPages": 1,
  "last": true,
  "first": true,
  "numberOfElements": 2,
  "size": 10,
  "number": 0,
  "empty": false
}
```

### Obtener Reservación por ID - Exitoso (200 OK)

**Request:**
```http
GET /api/reservaciones/1
```

**Response (200 OK):**
```json
{
  "idReservacion": 1,
  "usuarioId": 1,
  "canchaId": 1,
  "cajeroId": null,
  "tiempoInicio": "2025-10-15T14:00:00",
  "dni": "73266267",
  "duracion": "2 hours",
  "precioTotal": 120.00,
  "estadoReservacion": "Confirmada"
}
```

### Obtener Reservación - No Encontrada (404 Not Found)

**Request:**
```http
GET /api/reservaciones/999
```

**Response (404 Not Found):**
(Sin body, solo status 404)

### Filtrar por Usuario - Exitoso (200 OK)

**Request:**
```http
GET /api/reservaciones?usuarioId=1
```

**Response (200 OK):**
```json
{
  "content": [
    {
      "idReservacion": 1,
      "usuarioId": 1,
      "canchaId": 1,
      "cajeroId": null,
      "tiempoInicio": "2025-10-15T14:00:00",
      "dni": "73266267",
      "duracion": "2 hours",
      "precioTotal": 120.00,
      "estadoReservacion": "Confirmada"
    }
  ],
  "pageable": {...},
  "totalElements": 1,
  "totalPages": 1
}
```

### Filtrar por Estado - Exitoso (200 OK)

**Request:**
```http
GET /api/reservaciones?estado=Pendiente
```

**Response:** Lista de reservaciones con estado "Pendiente"

## Parámetros de Query

### Filtros Disponibles
- `usuarioId` (Integer) - Filtrar por ID de usuario
- `canchaId` (Short) - Filtrar por ID de cancha
- `estado` (String) - Filtrar por estado (ej: "Confirmada", "Pendiente", "Cancelada")
- `dni` (String) - Filtrar por DNI del cliente

### Paginación
- `page` (Integer) - Número de página (empieza en 0). Default: 0
- `size` (Integer) - Tamaño de página. Default: 10
- `sort` (String) - Campo y dirección de ordenamiento. Default: "tiempoInicio,desc"

**Ejemplos de sort:**
- `sort=tiempoInicio,asc` - Ordenar por fecha ascendente (más antiguas primero)
- `sort=tiempoInicio,desc` - Ordenar por fecha descendente (más recientes primero)
- `sort=precioTotal,desc` - Ordenar por precio descendente
- `sort=estadoReservacion,asc` - Ordenar por estado alfabéticamente

## Campos de ReservacionDto

| Campo | Tipo | Descripción | Nullable |
|-------|------|-------------|----------|
| idReservacion | Integer | ID único de la reservación | No |
| usuarioId | Integer | ID del usuario que reservó | Sí |
| canchaId | Short | ID de la cancha reservada | No |
| cajeroId | Short | ID del cajero que gestionó | Sí |
| tiempoInicio | LocalDateTime | Fecha/hora de inicio | No |
| dni | String | DNI del cliente (8 dígitos) | No |
| duracion | String | Duración en formato legible | No |
| precioTotal | BigDecimal | Precio total de la reserva | No |
| estadoReservacion | String | Estado actual | No |

### Formato de `duracion`
El campo `duracion` se convierte de PostgreSQL INTERVAL a String legible:
- `"1 hour"` - Una hora
- `"2 hours"` - Dos horas
- `"1 hour 30 minutes"` - Una hora y media
- `"90 minutes"` - 90 minutos

### Valores típicos de `estadoReservacion`
- `"Pendiente"` - Reserva creada, pendiente de confirmación
- `"Confirmada"` - Reserva confirmada
- `"Cancelada"` - Reserva cancelada
- `"Completada"` - Reserva ya realizada

## Pruebas con PowerShell

### Listar todas las reservaciones
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones" -UseBasicParsing | Select-Object -ExpandProperty Content | ConvertFrom-Json | ConvertTo-Json -Depth 10
```

### Filtrar por usuario
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones?usuarioId=1" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Filtrar por cancha
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones?canchaId=2" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Filtrar por estado
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones?estado=Confirmada" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Obtener una reservación específica
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones/1" -UseBasicParsing | Select-Object -ExpandProperty Content
```

### Con paginación personalizada
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reservaciones?page=0&size=5&sort=precioTotal,desc" -UseBasicParsing | Select-Object -ExpandProperty Content
```

## Pruebas con cURL

### Listar todas
```bash
curl -X GET "http://localhost:8080/api/reservaciones"
```

### Filtrar por usuario
```bash
curl -X GET "http://localhost:8080/api/reservaciones?usuarioId=1"
```

### Filtrar por DNI
```bash
curl -X GET "http://localhost:8080/api/reservaciones?dni=73266267"
```

### Obtener por ID
```bash
curl -X GET "http://localhost:8080/api/reservaciones/1"
```

## Códigos HTTP Esperados

- `200 OK` - Consulta exitosa
- `404 Not Found` - Reservación no encontrada (solo para GET por ID)
- `500 Internal Server Error` - Error en el servidor

## Notas Importantes

1. **Filtros mutuamente excluyentes**: Si pasas múltiples filtros (ej: usuarioId y canchaId), solo se aplicará el primero en este orden de prioridad:
   1. usuarioId
   2. canchaId
   3. estado
   4. dni

2. **Campos nullable**: 
   - `usuarioId` puede ser null (reservas sin usuario registrado)
   - `cajeroId` puede ser null (reservas online sin cajero)

3. **Ordenamiento por defecto**: Las reservaciones se ordenan por `tiempoInicio` descendente (más recientes primero).

4. **Formato de fechas**: Las fechas se devuelven en formato ISO-8601: `2025-10-15T14:00:00`

5. **Duración mínima**: La base de datos tiene un constraint de que la duración debe ser al menos 1 hora.

## Integración con Swagger

Cuando arranques la aplicación, los endpoints estarán disponibles en:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- Busca el controller **reservacion-controller**
- Podrás probar todos los endpoints directamente desde el navegador
