package com.jazogue.price.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jazogue.price.controller.PriceQueryController;
import com.jazogue.price.dto.PriceRequestDTO;
import com.jazogue.price.dto.PriceResponseDTO;
import com.jazogue.price.entity.PriceEntity;
import com.jazogue.price.repository.PriceRepository;

@Service
public class PriceQueryService {

	private static final Logger log = LoggerFactory.getLogger(PriceQueryController.class);
	
	@Autowired
	private PriceRepository priceRepository;

	public PriceResponseDTO getPrice(PriceRequestDTO priceRequestDTO) {
		List<PriceEntity> prices = priceRepository
				.findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
						priceRequestDTO.getApplicationDate(), priceRequestDTO.getApplicationDate(),
						priceRequestDTO.getProductId(), priceRequestDTO.getBrandId());

		log.info("[PriceQueryService][getPrice] Prices have been retrieved from the database: {} in total.{}",
				prices.size(), prices.size() > 1 ? " It filters by the instance with the highest priority" : "");
		
		Optional<PriceEntity> priceOpt = prices.stream()
				.max((price1, price2) -> Integer.compare(price1.getPriority(), price2.getPriority()));
		
		if (priceOpt.isPresent()) {
			PriceEntity price = priceOpt.get();
			
			log.info("[PriceQueryService][getPrice] The instance with these fields is returned: {}",
					price.toString());
			
			PriceResponseDTO response = new PriceResponseDTO(price.getProductId(), price.getBrandId(),
					price.getPriceList(), price.getStartDate(), price.getEndDate(), price.getPrice());
			
			return response;
		}
		return null;
	}
	
}
