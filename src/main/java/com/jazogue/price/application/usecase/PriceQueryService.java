package com.jazogue.price.application.usecase;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.jazogue.price.domain.model.PriceEntity;
import com.jazogue.price.domain.repository.PriceRepository;
import com.jazogue.price.infrastructure.adapter.in.web.PriceQueryController;
import com.jazogue.price.infrastructure.dto.PriceRequestDTO;
import com.jazogue.price.infrastructure.dto.PriceResponseDTO;
import com.jazogue.price.infrastructure.exception.PriceNotFoundException;

import jakarta.validation.Valid;

@Service
public class PriceQueryService {

	private static final Logger log = LoggerFactory.getLogger(PriceQueryController.class);

	@Autowired
	private PriceRepository priceRepository;

	public PriceResponseDTO getPrice(@RequestBody @Valid PriceRequestDTO priceRequestDTO) {
		List<PriceEntity> prices = priceRepository
				.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
						priceRequestDTO.getApplicationDate(), priceRequestDTO.getApplicationDate(),
						priceRequestDTO.getProductId(), priceRequestDTO.getBrandId());

		if (prices.isEmpty()) {
			log.warn("[PriceQueryService][getPrice] No price found for the given parameters: {}", priceRequestDTO);
			throw new PriceNotFoundException("No price found for the given parameters.");
		}

		PriceEntity price = prices.stream().max(Comparator.comparingInt(PriceEntity::getPriority))
				.orElseThrow(() -> new PriceNotFoundException("No price found for the given parameters."));

		log.info("[PriceQueryService][getPrice] The instance with these fields is returned: {}", price.toString());

		return new PriceResponseDTO(price.getProductId(), price.getBrandId(), price.getPriceList(),
				price.getStartDate(), price.getEndDate(), price.getPrice());
	}
}
