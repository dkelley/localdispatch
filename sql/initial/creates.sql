-- Tables --

CREATE TABLE account (
	account_id BIGINT NOT NULL,	
	email CHARACTER VARYING(200),
	password CHARACTER VARYING(500),
	remember_me_token CHARACTER VARYING(500) NOT NULL,
	first_name CHARACTER VARYING(200) NOT NULL,
	last_name CHARACTER VARYING(200) NOT NULL,
	phone_number CHARACTER VARYING(10) NOT NULL,
	api_token CHARACTER VARYING(500) NOT NULL,
	time_zone_identifier CHARACTER VARYING(100) DEFAULT 'US/Eastern',
	country_identifier CHARACTER VARYING(100) DEFAULT 'US', -- ISO-3166
	language_identifier CHARACTER VARYING(100) DEFAULT 'en', -- ISO-639
	created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	CONSTRAINT account_pk PRIMARY KEY (account_id)
);

CREATE TABLE role (
	role_id BIGINT NOT NULL,
	description CHARACTER VARYING(100) NOT NULL,
	display_order INTEGER NOT NULL,
	CONSTRAINT role_pk PRIMARY KEY (role_id)
);

CREATE TABLE device_location (
	device_location_id BIGINT NOT NULL,
	device_identifier CHARACTER VARYING(100) NOT NULL,
	speed DOUBLE PRECISION,
	latitude DOUBLE PRECISION NOT NULL,
	longitude DOUBLE PRECISION NOT NULL,
	course DOUBLE PRECISION NOT NULL,
	accuracy DOUBLE PRECISION NOT NULL,
	created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	CONSTRAINT device_location_id_pk PRIMARY KEY (device_location_id)
);

CREATE TABLE account_role (
			    role_id BIGINT NOT NULL,
			    account_id BIGINT NOT NULL,
				created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
				updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
			    CONSTRAINT account_role_pk PRIMARY KEY (role_id, account_id)
);

CREATE TABLE state (
			    state_id BIGINT NOT NULL,
				display_order SMALLINT NOT NULL,    
	    		description CHARACTER VARYING(100) NOT NULL,
	    		state_abbreviation CHARACTER VARYING(2) NOT NULL,
	    		CONSTRAINT state_pk PRIMARY KEY (state_id)
);

CREATE TABLE address (
			    address_id BIGINT NOT NULL,
			    address1 CHARACTER VARYING(100) NOT NULL,
			    address2 CHARACTER VARYING(100),
			    note CHARACTER VARYING(300),
				city CHARACTER VARYING(100) NOT NULL,
				state_id BIGINT NOT NULL,
				zipcode CHARACTER VARYING(9) NOT NULL,
				latitude DOUBLE PRECISION,
                longitude DOUBLE PRECISION,
				updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
			    CONSTRAINT address_pk PRIMARY KEY (address_id)
);


CREATE TABLE trip_status (
			    trip_status_id BIGINT NOT NULL,
			    description CHARACTER VARYING(100) NOT NULL,				
			    CONSTRAINT trip_status_pk PRIMARY KEY (trip_status_id)
);

/*
CREATE TABLE trip (
	    trip_id BIGINT NOT NULL,
	    account_id BIGINT NOT NULL,
		trip_name CHARACTER VARYING(100) NOT NULL,				
		reservation_number CHARACTER VARYING(100) NOT NULL,	    
	    pickup_address_id BIGINT NOT NULL,
	    pickup_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,			    
	    destination_address_id BIGINT NOT NULL,
	    destination_time TIMESTAMP WITHOUT TIME ZONE,			    
	    driver_id BIGINT NOT NULL default 0, -- 0 is not selected
	    car_id BIGINT NOT NULL default 0, -- 0 is not selected
	    trip_status_id BIGINT NOT NULL default 0, -- 0 is new
	    num_of_passengers BIGINT NOT NULL default 1,
		updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
		time_zone_identifier CHARACTER VARYING(100) DEFAULT 'US/Eastern',		
	    CONSTRAINT trip_pk PRIMARY KEY (trip_id)
);

CREATE TABLE message_log (
	message_log_id BIGINT NOT NULL,	
	message TEXT,
	template_name CHARACTER VARYING(500),
	sent_from CHARACTER VARYING(100) NOT NULL,
	sent_to CHARACTER VARYING(1000) NOT NULL,		
	sent_successfully_flag BOOLEAN NOT NULL,
	failure_reason TEXT,
	created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
	updated_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
	CONSTRAINT message_log_pk PRIMARY KEY (message_log_id)
);
*/

-- Indexes --

CREATE UNIQUE INDEX account_username_idx ON account (email);  
CREATE UNIQUE INDEX account_remember_me_token_idx ON account (remember_me_token);
CREATE UNIQUE INDEX account_api_token_idx ON account (api_token);
-- Sequences --


CREATE SEQUENCE address_seq START WITH 1000;
CREATE SEQUENCE trip_seq START WITH 1000;
CREATE SEQUENCE account_seq START WITH 1000;
CREATE SEQUENCE message_log_seq START WITH 1000;
CREATE SEQUENCE device_location_seq START WITH 1000;

-- where is trip_seq

-- Functions --
CREATE OR REPLACE FUNCTION set_updated_date() RETURNS TRIGGER AS $$
BEGIN        
	NEW.updated_date := 'now';
	RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Thanks to http://iamtgc.com/2009/01/14/implementing-zip-code-proximity-functions-in-mysql-and-postgresql/
CREATE OR REPLACE FUNCTION miles_between_coordinates(latitude1 DOUBLE PRECISION, longitude1 DOUBLE PRECISION, latitude2 DOUBLE PRECISION, longitude2 DOUBLE PRECISION) RETURNS DOUBLE PRECISION AS $$
BEGIN    
	-- 3959.0 is miles, use 6371.0 for km
	-- 57.2958 is an approximation of 180 / pi
	RETURN (
		3959.0 * acos(sin(latitude1 / 57.2958) * sin(latitude2 / 57.2958) + cos(latitude1 / 57.2958) * cos(latitude2 / 57.2958) * cos((longitude2 / 57.2958) - (longitude1 / 57.2958)))
	);
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION first_day_of_month(DATE) RETURNS DATE AS $$
	SELECT date_trunc('MONTH', $1)::date;
$$ LANGUAGE 'sql' IMMUTABLE STRICT;

CREATE OR REPLACE FUNCTION last_day_of_month(DATE) RETURNS DATE AS $$
	SELECT (date_trunc('MONTH', $1) + INTERVAL '1 MONTH - 1 day')::date;
$$ LANGUAGE 'sql' IMMUTABLE STRICT;



-- Views --
--
--CREATE OR REPLACE VIEW v_restaurant_guest_with_status 
--AS
--SELECT rg.guest_id, rg.restaurant_guest_id, rg.restaurant_id, gv.vip_status_id, gv.description as vip_status, bv.min_points_required, bv.max_points_required, gv.ranking, b.brand_id,
--bg.created_date as start_date_with_brand, rg.sign_up_date
--FROM restaurant_guest rg, restaurant r, brand b, brand_guest bg, brand_vip_status bv, vip_status gv
--WHERE rg.restaurant_id = r.restaurant_id AND r.brand_id = b.brand_id AND b.brand_id = bg.brand_id 
--AND rg.guest_id = bg.guest_id AND bg.brand_vip_status_id = bv.brand_vip_status_id
--AND bv.vip_status_id = gv.vip_status_id;


-- Triggers --
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON account FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON account_role FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON address FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
-- CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON trip FOR EACH ROW EXECUTE PROCEDURE set_updated_date();
-- CREATE TRIGGER set_updated_date BEFORE INSERT OR UPDATE ON message_log FOR EACH ROW EXECUTE PROCEDURE set_updated_date();

-- Constraints --  

ALTER TABLE account_role ADD CONSTRAINT role_account_role_fk
FOREIGN KEY (role_id)
REFERENCES role (role_id);

ALTER TABLE account_role ADD CONSTRAINT account_account_role_fk
FOREIGN KEY (account_id)
REFERENCES account (account_id);