-- Inserts --
INSERT INTO role(role_id, description, display_order) VALUES (1, 'Admin', 1);
INSERT INTO role(role_id, description, display_order) VALUES (2, 'Driver', 2);
INSERT INTO role(role_id, description, display_order) VALUES (4, 'Guest', 3);

INSERT INTO order_status(order_status_id, description) VALUES (0, 'Open');
INSERT INTO order_status(order_status_id, description) VALUES (1, 'Confirmed');
INSERT INTO order_status(order_status_id, description) VALUES (2, 'Picked Up');
INSERT INTO order_status(order_status_id, description) VALUES (3, 'Delivered');
INSERT INTO order_status(order_status_id, description) VALUES (4, 'Completed');

INSERT INTO state VALUES (1, 1, 'Alabama', 'AL');
INSERT INTO state VALUES (2, 2, 'Alaska', 'AK');
INSERT INTO state VALUES (3, 3, 'Arizona', 'AZ');
INSERT INTO state VALUES (4, 4, 'Arkansas', 'AR');
INSERT INTO state VALUES (5, 5, 'California', 'CA');
INSERT INTO state VALUES (6, 6, 'Colorado', 'CO');
INSERT INTO state VALUES (7, 7, 'Connecticut', 'CT');
INSERT INTO state VALUES (8, 8, 'Delaware', 'DE');
INSERT INTO state VALUES (9, 9, 'District of Columbia', 'DC');
INSERT INTO state VALUES (10, 10, 'Florida', 'FL');
INSERT INTO state VALUES (11, 11, 'Georgia', 'GA');
INSERT INTO state VALUES (12, 12, 'Hawaii', 'HI');
INSERT INTO state VALUES (13, 13, 'Idaho', 'ID');
INSERT INTO state VALUES (14, 14, 'Illinois', 'IL');
INSERT INTO state VALUES (15, 15, 'Indiana', 'IN');
INSERT INTO state VALUES (16, 16, 'Iowa', 'IA');
INSERT INTO state VALUES (17, 17, 'Kansas', 'KS');
INSERT INTO state VALUES (18, 18, 'Kentucky', 'KY');
INSERT INTO state VALUES (19, 19, 'Louisiana', 'LA');
INSERT INTO state VALUES (20, 20, 'Maine', 'ME');
INSERT INTO state VALUES (21, 21, 'Maryland', 'MD');
INSERT INTO state VALUES (22, 22, 'Massachusetts', 'MA');
INSERT INTO state VALUES (23, 23, 'Michigan', 'MI');
INSERT INTO state VALUES (24, 24, 'Minnesota', 'MN');
INSERT INTO state VALUES (25, 25, 'Mississippi', 'MS');
INSERT INTO state VALUES (26, 26, 'Missouri', 'MO');
INSERT INTO state VALUES (27, 27, 'Montana', 'MT');
INSERT INTO state VALUES (28, 28, 'Nebraska', 'NE');
INSERT INTO state VALUES (29, 29, 'Nevada', 'NV');
INSERT INTO state VALUES (30, 30, 'New Hampshire', 'NH');
INSERT INTO state VALUES (31, 31, 'New Jersey', 'NJ');
INSERT INTO state VALUES (32, 32, 'New Mexico', 'NM');
INSERT INTO state VALUES (33, 33, 'New York', 'NY');
INSERT INTO state VALUES (34, 34, 'North Carolina', 'NC');
INSERT INTO state VALUES (35, 35, 'North Dakota', 'ND');
INSERT INTO state VALUES (36, 36, 'Ohio', 'OH');
INSERT INTO state VALUES (37, 37, 'Oklahoma', 'OK');
INSERT INTO state VALUES (38, 38, 'Oregon', 'OR');
INSERT INTO state VALUES (39, 39, 'Pennsylvania', 'PA');
INSERT INTO state VALUES (40, 40, 'Rhode Island', 'RI');
INSERT INTO state VALUES (41, 41, 'South Carolina', 'SC');
INSERT INTO state VALUES (42, 42, 'South Dakota', 'SD');
INSERT INTO state VALUES (43, 43, 'Tennessee', 'TN');
INSERT INTO state VALUES (44, 44, 'Texas', 'TX');
INSERT INTO state VALUES (45, 45, 'Utah', 'UT');
INSERT INTO state VALUES (46, 46, 'Vermont', 'VT');
INSERT INTO state VALUES (47, 47, 'Virginia', 'VA');
INSERT INTO state VALUES (48, 48, 'Washington', 'WA');
INSERT INTO state VALUES (49, 49, 'West Virginia', 'WV');
INSERT INTO state VALUES (50, 50, 'Wisconsin', 'WI');
INSERT INTO state VALUES (51, 51, 'Wyoming', 'WY');


/* 
INSERT INTO address(address_id, address1, address2, city, state_id, zipcode, latitude, longitude) VALUES(1, '221 S. Ithan Ave', null, 'Rosemont', 39, '19010', 40.028828,-75.350243);
INSERT INTO address(address_id, address1, address2, city, state_id, zipcode, latitude, longitude) VALUES(2, 'Philadelphia Airport', 'Terminal C', 'Philadephia', 39, '19010', 39.876376,-75.243466);

INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(1, 'Dan', 'Kelley', '6103061733', '7054deb714e89499a9eb3d435d12f37e', '1', '1');
INSERT INTO account_role(account_id, role_id) VALUES(1,4);

INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(2, 'Chris', 'Wolfington', '7816355393', '7054deb714e89499a9eb3d435d12f37e', '2', '2');
INSERT INTO account_role(account_id, role_id) VALUES(2,4);

INSERT INTO trip(trip_name, reservation_number, trip_id,account_id, pickup_address_id, pickup_time, destination_address_id, destination_time, driver_id)
VALUES('Trip 1021', '1234', 1021, 1, 1, now(), 2, null, 0);

INSERT INTO trip(trip_name, reservation_number, trip_id,account_id, pickup_address_id, pickup_time, destination_address_id, destination_time, driver_id)
VALUES('Trip 1022', 'ABCD', 1022, 2, 1, now(), 2, null, 1);
*/

-- Add an admin account
INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(1, 'Dan', 'Kelley', '6103061733','7054deb714e89499a9eb3d435d12f37e', '1', '1');
INSERT INTO account_role(account_id, role_id) VALUES(1,1);

INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(2, 'Rich', 'Siegel', '1234567890', '7054deb714e89499a9eb3d435d12f37e', '2', '2');
INSERT INTO account_role(account_id, role_id) VALUES(2,1);

-- Add an driver account
INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(3, 'Driver', 'One', '6103061733','7054deb714e89499a9eb3d435d12f37e', '3', '3');
INSERT INTO account_role(account_id, role_id) VALUES(3,2);

INSERT INTO account (account_id, first_name, last_name, phone_number, password, remember_me_token, api_token) VALUES(4, 'Driver', 'Two', '1234567890', '7054deb714e89499a9eb3d435d12f37e', '4', '4');
INSERT INTO account_role(account_id, role_id) VALUES(4,2);


-- add restaurants
insert into address (address_id, address1, city, state_id, zipcode, latitude, longitude)
values(nextval('address_seq'), '333 Belrose Ln','Radnor',39, 19087, 40.045719,-75.361857);

insert into restaurant(restaurant_id, name, phone_number, notes, address_id)
values(nextval('restaurant_seq'),'333 Belrose','6102931000', 'Ask for Brian',currval('address_seq'));

insert into address (address_id, address1, city, state_id, zipcode, latitude, longitude)
values(nextval('address_seq'), '555 E Lancaster Ave','Radnor',39, 19087, 40.04121,-75.367758);

insert into restaurant(restaurant_id, name, phone_number, notes, address_id)
values(nextval('restaurant_seq'),'Susanna Foo','6106888808', 'Always check the order',currval('address_seq'));

insert into address (address_id, address1, city, state_id, zipcode, latitude, longitude)
values(nextval('address_seq'), '200 W Lancaster Ave','Wayne',39, 19087, 40.044019,-75.391275);

insert into restaurant(restaurant_id, name, phone_number, notes, address_id)
values(nextval('restaurant_seq'),'White Dog Cafe','6102253700', 'gret',currval('address_seq'));

insert into address (address_id, address1, city, state_id, zipcode, latitude, longitude)
values(nextval('address_seq'), '789 E Lancaster Ave','Wayne',39, 19085, 40.0367,-75.350645);

insert into restaurant(restaurant_id, name, phone_number, notes, address_id)
values(nextval('restaurant_seq'),'Azie on Main','6105275700', 'gret',currval('address_seq'));

