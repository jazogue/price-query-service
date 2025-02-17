package com.jazogue.price.infrastructure.adapter;

import com.jazogue.price.application.PriceQueryApplication;
import com.jazogue.price.domain.model.PriceEntity;
import com.jazogue.price.domain.repository.PriceRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = PriceQueryApplication.class)
public class PriceQueryRepositoryTests {

	@Autowired
	private PriceRepository pricesRepository;

	/**
	 * Verifica que la base de datos contenga exactamente 4 instancias después de la
	 * inicialización de datos, asegurando que el número de registros en la tabla
	 * sea correcto.
	 */
	@Test
	public void testDataInitialization() {
		// Obtiene todas las instancias de PriceEntity de la base de datos
		List<PriceEntity> prices = pricesRepository.findAll();
		
	    // Verifica que la cantidad de instancias en la base de datos sea igual a 4
		assertEquals(4, prices.size(), "There should be 4 instances in the table");
	}

}
