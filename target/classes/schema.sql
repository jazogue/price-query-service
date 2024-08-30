-- Crear la tabla PRICES
CREATE TABLE IF NOT EXISTS PRICES (
  brand_id INT,
  start_date TIMESTAMP,
  end_date TIMESTAMP,
  price_list INT,
  product_id INT,
  priority INT,
  price DECIMAL(10, 2),
  curr VARCHAR(3),
  PRIMARY KEY (price_list, product_id)
);
