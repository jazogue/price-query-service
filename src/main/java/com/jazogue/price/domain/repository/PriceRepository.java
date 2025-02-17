package com.jazogue.price.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jazogue.price.domain.model.PriceEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceRepository extends JpaRepository<PriceEntity, Integer> {

	List<PriceEntity> findByStartDateLessThanEqualAndEndDateGreaterThanEqualAndProductIdAndBrandId(
			LocalDateTime startDate, LocalDateTime endDate, Integer productId, Integer priceList);

}