DROP DATABASE dispatch;
DROP ROLE dispatch;

CREATE ROLE dispatch LOGIN PASSWORD 'dispatch' NOSUPERUSER NOINHERIT NOCREATEROLE;
CREATE DATABASE dispatch WITH OWNER = dispatch ENCODING = 'UTF8';

\c dispatch;
	
CREATE SCHEMA dispatch 
AUTHORIZATION dispatch;	

ALTER USER dispatch SET search_path TO dispatch;

ALTER DATABASE dispatch SET timezone TO 'UTC';

-- Use backcompat bytea mode for Postgres 9+
ALTER DATABASE dispatch SET bytea_output = 'escape'; 