package com.jazogue.price.infrastructure.exception;
 
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Maneja la excepción cuando un precio no se encuentra en la base de datos.
	 * Retorna un 404 Not Found con el mensaje de error correspondiente.
	 */
	@ExceptionHandler(PriceNotFoundException.class)
	public ResponseEntity<String> handlePriceNotFoundException(PriceNotFoundException ex) {
		return new ResponseEntity<>("Error: The requested price was not found." + ex.getMessage(), HttpStatus.NOT_FOUND);
	}

	/**
	 * Maneja excepciones inesperadas del sistema. Retorna un 500 Internal Server
	 * Error con un mensaje genérico.
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleGeneralException(Exception ex) {
		return new ResponseEntity<>("An unexpected error occurred on the server: " + ex.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * Maneja argumentos inválidos en la API, como valores incorrectos en los DTOs.
	 * Retorna un 400 Bad Request con el mensaje de error correspondiente.
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
		return new ResponseEntity<>("Request error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja errores cuando el cuerpo de la solicitud no puede ser leído o tiene un
	 * formato inválido. Retorna un 400 Bad Request indicando que los datos enviados
	 * no son válidos.
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return new ResponseEntity<>("Invalid request format. Please check the submitted data.", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja errores de validación en los DTOs cuando un campo requerido no cumple
	 * con las reglas definidas. Retorna un 400 Bad Request con detalles de los
	 * errores de validación.
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> "Field '" + error.getField() + "': " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return new ResponseEntity<>("Validation error in the submitted data: " + message, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja errores cuando falta un parámetro requerido en la solicitud. Retorna
	 * un 400 Bad Request indicando el parámetro que falta.
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex) {
		return new ResponseEntity<>("Missing required parameter: '" + ex.getParameterName() + "'", HttpStatus.BAD_REQUEST);
	}

	/**
	 * Maneja errores cuando un parámetro tiene un tipo de dato incorrecto. Retorna
	 * un 400 Bad Request indicando el nombre del parámetro y el tipo esperado.
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
		return new ResponseEntity<>("Invalid data type for parameter '" + ex.getName() + "'. Expected type: "
				+ ex.getRequiredType().getSimpleName(), HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Maneja errores cuando la fecha proporcionada tiene un formato incorrecto.
	 * Retorna un 400 Bad Request indicando el formato esperado.
	 */
	@ExceptionHandler(DateTimeParseException.class)
	public ResponseEntity<String> handleDateTimeParseException(DateTimeParseException ex) {
	    return new ResponseEntity<>("Invalid date format. Please use 'yyyy-MM-dd'T'HH:mm:ss'.", 
	            HttpStatus.BAD_REQUEST);
	}

}
