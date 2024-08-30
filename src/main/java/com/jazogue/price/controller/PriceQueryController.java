package com.jazogue.price.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jazogue.price.dto.PriceRequestDTO;
import com.jazogue.price.dto.PriceResponseDTO;
import com.jazogue.price.service.PriceQueryService;

@RestController
@RequestMapping("/api")
public class PriceQueryController {

	private static final Logger log = LoggerFactory.getLogger(PriceQueryController.class);

	@Autowired
	private PriceQueryService priceQueryService;

	@GetMapping("/price")
	public ResponseEntity<PriceResponseDTO> getPrice(@RequestParam("applicationDate") String applicationDateStr,
			@RequestParam("productId") int productId, @RequestParam("brandId") int brandId) {
		log.info("[PriceQueryController][getPrice] Received request: applicationDate={}, productId={}, brandId={}",
				applicationDateStr, productId, brandId);

		LocalDateTime applicationDate;
		try {
			applicationDate = LocalDateTime.parse(applicationDateStr);
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid date format. Please use 'yyyy-MM-dd'T'HH:mm:ss'.");
		}

		PriceRequestDTO requestDTO = new PriceRequestDTO(applicationDate, productId, brandId);
		PriceResponseDTO priceResponseDTO = priceQueryService.getPrice(requestDTO);
		
		if (priceResponseDTO != null) {
			log.info("[PriceQueryController][getPrice] Returning response: {}", priceResponseDTO);
			return ResponseEntity.status(HttpStatus.OK).body(priceResponseDTO);
		} else {
			log.info("[PriceQueryController][getPrice] No database instance found");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}
	}

}
