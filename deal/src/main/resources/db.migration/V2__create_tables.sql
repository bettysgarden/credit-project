CREATE TABLE IF NOT EXISTS passport
(
    id 	            SERIAL PRIMARY KEY,
    series 			VARCHAR(4),
    number 			VARCHAR(6),
    issue_branch 	VARCHAR,
    issue_date 		DATE
    );

CREATE TABLE IF NOT EXISTS employment
(
    id 			            SERIAL PRIMARY KEY,
    status 					INT REFERENCES employment_status (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	employer_inn 			VARCHAR(12),
    salary 					DECIMAL,
    "position" 				INT REFERENCES "position" (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	work_experience_total 	INT,
    work_experience_current INT
);

CREATE TABLE IF NOT EXISTS credit
(
    id       			SERIAL PRIMARY KEY,
    amount 				DECIMAL,
    term 				INT,
    monthly_payment 	DECIMAL,
    rate 				DECIMAL,
    psk 				DECIMAL,
    payment_schedule 	JSONB,
    insurance_enable 	BOOLEAN,
    salary_client 		BOOLEAN,
    status 		        INT REFERENCES credit_status (id) ON DELETE CASCADE ON UPDATE NO ACTION
    );

CREATE TABLE IF NOT EXISTS client
(
    id       			SERIAL PRIMARY KEY,
    last_name 			VARCHAR                                                                             NOT NULL,
    first_name 			VARCHAR                                                                             NOT NULL,
    middle_name 		VARCHAR,
    birthdate 			DATE                                                                                NOT NULL,
    email 				VARCHAR,
    gender 				INT REFERENCES gender (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	marital_status 		INT REFERENCES marital_status (id) ON DELETE CASCADE ON UPDATE NO ACTION,
	dependent_amount 	INT,
    passport_id 		BIGINT REFERENCES passport (id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL     UNIQUE,
    employment_id 		BIGINT REFERENCES employment (id) ON DELETE CASCADE ON UPDATE NO ACTION            UNIQUE,
    account 			VARCHAR
);

CREATE TABLE IF NOT EXISTS application
(
    id           	SERIAL PRIMARY KEY,
    client_id 	   	BIGINT REFERENCES client (id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL     UNIQUE,
    credit_id 	   	BIGINT REFERENCES credit (id) ON DELETE CASCADE ON UPDATE NO ACTION              UNIQUE,
    status         	INT REFERENCES application_status (id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL,
	creation_date  	TIMESTAMP NOT NULL,
    applied_offer  	JSONB,
    sign_date 	   	TIMESTAMP,
    ses_code 	   	BIGINT,
    status_history 	JSONB NOT NULL
);
