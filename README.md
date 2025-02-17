# PriceQueryService

**Desarrollado por:** Juan Azogue Mulders

## Descripción

`PriceQueryService` es un servicio RESTful desarrollado con Spring Boot que proporciona un endpoint para consultar precios aplicables de productos. Utiliza una base de datos en memoria H2 para almacenar y consultar los datos de precios.

## Requisitos Previos

- **Java 17** o superior
- **Maven 3.8.4** o superior
- **Spring Boot 3.x**
- **Base de datos H2 (en memoria)**

## Instalación y Configuración

### Clonar el repositorio
```bash
git clone https://github.com/jazogue/price-query-service.git
cd price-query-service
```

### Construir y ejecutar el proyecto
```bash
mvn clean install
mvn spring-boot:run
```

## Endpoints

### `GET /api/price`

Este endpoint permite consultar el precio de un producto basado en la fecha de aplicación, el identificador de producto y el identificador de la cadena.

#### **Parámetros de Entrada**

- **applicationDate** (requerido): Fecha y hora en formato `yyyy-MM-ddTHH:mm:ss` que especifica el momento de la consulta.
- **productId** (requerido): Identificador del producto que se consulta.
- **brandId** (requerido): Identificador de la cadena a la que pertenece el producto.

#### **Respuesta**

La respuesta contiene los siguientes campos:

- **productId**: Identificador del producto.
- **brandId**: Identificador de la cadena.
- **priceList**: Identificador de la tarifa de precios aplicable.
- **startDate**: Fecha de inicio del rango de aplicación del precio.
- **endDate**: Fecha de fin del rango de aplicación del precio.
- **price**: Precio final de venta.

#### **Ejemplo de Solicitud**
```http
GET /api/price?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1
```

#### **Ejemplo de Respuesta**
```json
{
  	"productId": 35455,
  	"brandId": 1,
	"priceList": 1,
	"startDate": "2020-06-14T00:00:00",
	"endDate": "2020-12-31T23:59:59",
	"price": 35.50
}
```

## Base de Datos H2

El servicio usa una base de datos en memoria H2 y se inicializa con los siguientes datos de prueba:

```sql
INSERT INTO PRICES (brand_id, start_date, end_date, price_list, product_id, priority, price, curr) VALUES
(1, '2020-06-14 00:00:00', '2020-12-31 23:59:59', 1, 35455, 0, 35.50, 'EUR'),
(1, '2020-06-14 15:00:00', '2020-06-14 18:30:00', 2, 35455, 1, 25.45, 'EUR'),
(1, '2020-06-15 00:00:00', '2020-06-15 11:00:00', 3, 35455, 1, 30.50, 'EUR'),
(1, '2020-06-15 16:00:00', '2020-12-31 23:59:59', 4, 35455, 1, 38.95, 'EUR');
```

### Acceder a la Consola de H2

La base de datos en memoria se puede consultar desde el navegador:

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Usuario**: `sa`
- **Contraseña**: *(vacío)*

## Ejecución de Pruebas

El servicio cuenta con pruebas automatizadas para validar el correcto funcionamiento del endpoint `GET /api/price`. Para ejecutarlas:

```bash
mvn test
```

Los tests validan las siguientes condiciones:

1. **Inicialización de la Base de Datos**
2. **Consulta con Datos Faltantes**
3. **Consulta con Fecha Inválida**
4. **Consulta con Precio Existente**
5. **Consulta con Precio No Existente**
6. **Consulta con Fecha Posterior a la Vigencia del Precio**

## Arquitectura

El servicio sigue una arquitectura **hexagonal (Ports and Adapters)** para mantener la independencia entre el dominio y las implementaciones externas.

### **Estructura del Proyecto**

```plaintext
com.jazogue.price
 ├── application
 │   ├── usecase
 │   │   ├── PriceQueryService
 │   ├── PriceQueryApplication
 │
 ├── domain
 │   ├── model
 │   │   ├── PriceEntity
 │   │   ├── id
 │   │   │   ├── PriceId
 │   ├── repository
 │   │   ├── PriceRepository
 │
 ├── infrastructure
 │   ├── adapter
 │   │   ├── in
 │   │   │   ├── web
 │   │   │   │   ├── PriceQueryController
 │   ├── dto
 │   │   ├── PriceRequestDTO
 │   │   ├── PriceResponseDTO
 │   ├── exception
 │   │   ├── GlobalExceptionHandler
 │   │   ├── PriceNotFoundException

```

## Manejo de Errores

El `GlobalExceptionHandler` captura y maneja excepciones, devolviendo respuestas claras en JSON. Ejemplo:

```json
{
  "timestamp": "2025-02-16T14:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid data type for parameter 'productId'. Expected type: int",
  "path": "/api/price"
}
```

**Email de contacto:** jazoguemulders@gmail.com