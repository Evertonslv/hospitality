CREATE TABLE reservations (
      id SERIAL PRIMARY KEY,
      guest_id INT NOT NULL,
      reservation_date TIMESTAMP NOT NULL,
      check_in_date DATE NULL,
      check_out_date DATE NULL,
      check_in_time TIMESTAMP NULL,
      check_out_time TIMESTAMP NULL,
      has_car BOOLEAN NOT NULL,
      status VARCHAR(50) NOT NULL,
      FOREIGN KEY (guest_id) REFERENCES guests(id) ON DELETE CASCADE
);