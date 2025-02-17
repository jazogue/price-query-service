package com.jazogue.price.infrastructure.adapter.in.web;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jazogue.price.application.usecase.PriceQueryService;
import com.jazogue.price.infrastructure.dto.PriceRequestDTO;
import com.jazogue.price.infrastructure.dto.PriceResponseDTO;

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

		PriceRequestDTO requestDTO = new PriceRequestDTO(LocalDateTime.parse(applicationDateStr), productId, brandId);
		PriceResponseDTO priceResponseDTO = priceQueryService.getPrice(requestDTO);

		log.info("[PriceQueryController][getPrice] Returning response: {}", priceResponseDTO);
		return ResponseEntity.status(HttpStatus.OK).body(priceResponseDTO);
	}

}
