# PriceQueryService

**Desarrollado por:** Juan Azogue Mulders

## Descripción

`PriceQueryService` es un servicio RESTful desarrollado con Spring Boot que proporciona un endpoint para consultar precios aplicables de productos. Utiliza una base de datos en memoria H2 para almacenar y consultar los datos de precios.

## Endpoints

### `GET /api/price`

Este endpoint permite consultar el precio de un producto basado en la fecha de aplicación, el identificador de producto y el identificador de la cadena.

#### Parámetros de Entrada

- **applicationDate** (requerido): Fecha y hora en formato `yyyy-MM-ddTHH:mm:ss` que especifica el momento de la consulta.
- **productId** (requerido): Identificador del producto que se consulta.
- **brandId** (requerido): Identificador de la cadena a la que pertenece el producto.

#### Respuesta

La respuesta contiene los siguientes campos:

- **productId**: Identificador del producto.
- **brandId**: Identificador de la cadena.
- **priceList**: Identificador de la tarifa de precios aplicable.
- **startDate**: Fecha de inicio del rango de aplicación del precio.
- **endDate**: Fecha de fin del rango de aplicación del precio.
- **price**: Precio final de venta.

#### Ejemplo de Solicitud

```http
GET /api/price?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1
