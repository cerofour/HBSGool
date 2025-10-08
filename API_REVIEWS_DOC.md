# API de Reviews - Documentación

## Endpoints Implementados

### 1. Listar Reviews (GET)
```http
GET http://localhost:8080/api/reviews
GET http://localhost:8080/api/reviews?usuarioId=1
GET http://localhost:8080/api/reviews?page=0&size=5&sort=creado,desc
```

### 2. Obtener Review por ID (GET)
```http
GET http://localhost:8080/api/reviews/1
```

### 3. Crear Review (POST) ⭐ NUEVO
```http
POST http://localhost:8080/api/reviews
Content-Type: application/json

{
    "usuarioId": 1,
    "rating": "5",
    "comentario": "Excelente cancha, muy limpia y buen servicio"
}
```

## Ejemplos de Request/Response

### Crear Review - Exitoso (201 Created)

**Request:**
```json
POST /api/reviews
{
    "usuarioId": 1,
    "rating": "4",
    "comentario": "Buena experiencia, recomendable"
}
```

**Response (201 Created):**
```json
{
    "idReview": 4,
    "usuarioId": 1,
    "rating": "4",
    "comentario": "Buena experiencia, recomendable",
    "creado": "2025-10-07T16:05:30.123456"
}
```

### Crear Review - Error de Validación (400 Bad Request)

**Request con datos inválidos:**
```json
POST /api/reviews
{
    "usuarioId": 0,
    "rating": "6",
    "comentario": ""
}
```

**Response (400 Bad Request):**
```json
{
    "usuarioId": "El usuarioId debe ser mayor a 0",
    "rating": "El rating debe ser entre 1 y 5",
    "comentario": "El comentario es obligatorio"
}
```

### Crear Review - Campos Faltantes (400 Bad Request)

**Request incompleto:**
```json
POST /api/reviews
{
    "usuarioId": 1
}
```

**Response (400 Bad Request):**
```json
{
    "rating": "El rating es obligatorio",
    "comentario": "El comentario es obligatorio"
}
```

## Validaciones Implementadas

### usuarioId
- ✅ Obligatorio
- ✅ Debe ser mayor a 0
- ✅ El usuario debe existir en la tabla Usuario (validado por foreign key en BD)

### rating
- ✅ Obligatorio
- ✅ Debe ser un string de 1 carácter
- ✅ Solo valores permitidos: "1", "2", "3", "4", "5"

### comentario
- ✅ Obligatorio
- ✅ No puede estar vacío
- ✅ Máximo 255 caracteres

## Pruebas con cURL (PowerShell)

### Crear un review válido
```powershell
$body = @{
    usuarioId = 1
    rating = "5"
    comentario = "Excelente servicio"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/reviews" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Listar todos los reviews
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/api/reviews" -UseBasicParsing | Select-Object -ExpandProperty Content
```

## Pruebas con Postman

1. Crear nueva request POST
2. URL: `http://localhost:8080/api/reviews`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
    "usuarioId": 1,
    "rating": "5",
    "comentario": "Muy buena cancha"
}
```
5. Send

## Código HTTP Esperados

- `201 Created` - Review creado exitosamente
- `400 Bad Request` - Datos de entrada inválidos (validaciones fallidas)
- `500 Internal Server Error` - Error en el servidor (ej: usuario no existe por FK)

## Notas Importantes

1. **Foreign Key**: Si el `usuarioId` no existe en la tabla `Usuario`, PostgreSQL devolverá un error de constraint. En ese caso verás un 500 con mensaje de "violates foreign key constraint".

2. **Timestamp automático**: El campo `creado` se genera automáticamente con la fecha/hora actual del servidor.

3. **ID autoincremental**: El `idReview` es generado automáticamente por PostgreSQL (IDENTITY).

4. **Validaciones**: Todas las validaciones definidas en `CreateReviewDto` se ejecutan antes de intentar guardar en la base de datos.
