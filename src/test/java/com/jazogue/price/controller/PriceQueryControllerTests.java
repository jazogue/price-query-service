package com.jazogue.price.controller;

import com.jazogue.price.PriceQueryApplication;
import com.jazogue.price.dto.PriceRequestDTO;
import com.jazogue.price.dto.PriceResponseDTO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(classes = PriceQueryApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = "/data.sql")
public class PriceQueryControllerTests {

	@LocalServerPort
	private int port;

	private RestTemplate restTemplate;

	private String baseUrl;

	@BeforeEach
	public void setUp() {
		restTemplate = new RestTemplate();
		baseUrl = "http://localhost:" + port + "/api/price";
	}

	/**
	 * Verifica que el endpoint `/api/price` devuelve un código de estado 400 
	 * (Bad Request) cuando se omite un campo obligatorio en la solicitud.
	 */
	@Test
	public void testBadRequestWhenFieldIsMissing() throws Exception {
		// Construir la solicitud sin el campo applicationDate
		String url = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("productId", 35455) // Campo obligatorio
				.queryParam("brandId", 1) // Campo obligatorio
				.toUriString();

		try {
			// Ejecutar la solicitud
			restTemplate.getForEntity(url, PriceResponseDTO.class);
			fail("Expected HttpClientErrorException due to missing field");
		} catch (HttpClientErrorException e) {
			// Verificar el código de estado en el bloque catch
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode(),
					"Expected HTTP status 400 Bad Request for missing field");
		}
	}

	/**
	 * Verifica que el endpoint `/api/price` devuelve un código de estado 400 
	 * (Bad Request) cuando se envían valores mal tipados en la solicitud.
	 */
	@Test
	public void testBadRequestOnIncorrectTypeValues() throws Exception {
		// Construir la solicitud con valores incorrectos
		String url = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("applicationDate", "test") // Valor incorrecto
				.queryParam("productId", "test") // Valor incorrecto
				.queryParam("brandId", "test") // Valor incorrecto
				.toUriString();

		try {
			// Ejecutar la solicitud
			restTemplate.getForEntity(url, PriceResponseDTO.class);
			fail("Expected HttpClientErrorException due to incorrect data types");
		} catch (HttpClientErrorException e) {
			// Verificar el código de estado en el bloque catch
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode(), "Expected HTTP status 400 Bad Request");
		}
	}

	/**
	 * Comprueba que se maneje correctamente una solicitud con una fecha en formato
	 * incorrecto. Se espera que la API devuelva un código de estado 400 (Bad
	 * Request) y un mensaje indicando "Invalid date format".
	 */
	@Test
	public void testGetPriceInvalidDateFormat() {
		// Crear el objeto de solicitud
		String invalidDate = "2020-08-14"; // Fecha en formato incorrecto

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl).queryParam("applicationDate", invalidDate)
				.queryParam("productId", 35455).queryParam("brandId", 1).toUriString();

		try {
			// Ejecutar la solicitud
			restTemplate.getForObject(url, String.class);
			fail("Expected HttpClientErrorException due to invalid date format");
		} catch (HttpClientErrorException e) {
			// Verificar que el código de estado sea 400 Bad Request
			assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());

			// Verificar el contenido del error
			String responseBody = e.getResponseBodyAsString();
			assertTrue(responseBody.contains("Invalid date format"));
		}
	}

	/**
	 * Verifica que el endpoint `/api/price` responda con un código de estado 204 
	 * (No Content) cuando no exista ninguna instancia en la base de datos con 
	 * un rango de fechas que incluya la fecha solicitada.
	 */
	@Test
	public void testDateOutOfRange() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2024, 6, 20, 0, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		ResponseEntity<PriceResponseDTO> response = restTemplate.getForEntity(url, PriceResponseDTO.class);

		// Verificar la respuesta
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Expected HTTP status 204 No Content");
	}

	/**
	 * Verifica que el endpoint `/api/price` responda con un código de estado 204 
	 * (No Content) cuando se solicita un registro que no existe en la base de datos.
	 */
	@Test
	public void testNonExistentValueRetrieval() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2021, 6, 20, 0, 0), 9999, 2);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		ResponseEntity<PriceResponseDTO> response = restTemplate.getForEntity(url, PriceResponseDTO.class);

		// Verificar la respuesta
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode(), "Expected HTTP status 204 No Content");
	}

	/**
	 * Verifica que el endpoint `/api/price` devuelve el precio correcto para un producto 
	 * y una marca específicos en distintas fechas y horas. Los siguientes tests aseguran 
	 * que el servicio respete la lógica de rango de fechas y la prioridad de tarifas.
	 */
	@Test
	public void testGetPrice1() throws Exception {
		// Crear el objeto de solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2020, 6, 14, 10, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		PriceResponseDTO response = restTemplate.getForObject(url, PriceResponseDTO.class);

		// Verificar la respuesta
		PriceResponseDTO expectedResponse = new PriceResponseDTO(35455, // productId
				1, // brandId
				1, // priceList
				LocalDateTime.of(2020, 6, 14, 00, 00, 00), // startDate
				LocalDateTime.of(2020, 12, 31, 23, 59, 59), // endDate
				new BigDecimal("35.50") // price
		);

		assertEquals(expectedResponse, response);
	}

	@Test
	public void testGetPrice2() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2020, 6, 14, 16, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		PriceResponseDTO response = restTemplate.getForObject(url, PriceResponseDTO.class);

		// Verificar la respuesta
		PriceResponseDTO expectedResponse = new PriceResponseDTO(35455, // productId
				1, // brandId
				2, // priceList
				LocalDateTime.of(2020, 6, 14, 15, 00, 00), // startDate
				LocalDateTime.of(2020, 6, 14, 18, 30, 00), // endDate
				new BigDecimal("25.45") // price
		);

		assertEquals(expectedResponse, response);
	}

	@Test
	public void testGetPrice3() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2020, 6, 14, 21, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		PriceResponseDTO response = restTemplate.getForObject(url, PriceResponseDTO.class);

		// Verificar la respuesta
		PriceResponseDTO expectedResponse = new PriceResponseDTO(35455, // productId
				1, // brandId
				1, // priceList
				LocalDateTime.of(2020, 6, 14, 00, 00, 00), // startDate
				LocalDateTime.of(2020, 12, 31, 23, 59, 59), // endDate
				new BigDecimal("35.50") // price
		);

		assertEquals(expectedResponse, response);
	}

	@Test
	public void testGetPrice4() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2020, 6, 15, 10, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud GET
		PriceResponseDTO response = restTemplate.getForObject(url, PriceResponseDTO.class);

		// Verificar la respuesta
		PriceResponseDTO expectedResponse = new PriceResponseDTO(35455, // productId
				1, // brandId
				3, // priceList
				LocalDateTime.of(2020, 6, 15, 00, 00, 00), // startDate
				LocalDateTime.of(2020, 6, 15, 11, 00, 00), // endDate
				new BigDecimal("30.50") // price
		);

		assertEquals(expectedResponse, response);
	}

	@Test
	public void testGetPrice5() throws Exception {
		// Construir la solicitud
		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.of(2020, 6, 16, 21, 0), 35455, 1);

		String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
				.queryParam("applicationDate", requestDTO.getApplicationDate())
				.queryParam("productId", requestDTO.getProductId()).queryParam("brandId", requestDTO.getBrandId())
				.toUriString();

		// Ejecutar la solicitud
		PriceResponseDTO response = restTemplate.getForObject(url, PriceResponseDTO.class);

		// Verificar la respuesta
		PriceResponseDTO expectedResponse = new PriceResponseDTO(35455, // productId
				1, // brandId
				4, // priceList
				LocalDateTime.of(2020, 6, 15, 16, 00, 00), // startDate
				LocalDateTime.of(2020, 12, 31, 23, 59, 59), // endDate
				new BigDecimal("38.95") // price
		);

		assertEquals(expectedResponse, response);
	}

}