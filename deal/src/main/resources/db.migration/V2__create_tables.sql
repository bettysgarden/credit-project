CREATE TABLE IF NOT EXISTS passport
(
    passport_id 	BIGINT PRIMARY KEY,
    series 			VARCHAR(4),
    number 			VARCHAR(6),
    issue_branch 	VARCHAR,
    issue_date 		DATE
    );

CREATE TABLE IF NOT EXISTS employment
(
    employment_id 			BIGINT PRIMARY KEY,
    status 					INT REFERENCES employment_status (id) ON DELETE CASCADE ON UPDATE NO ACTION UNIQUE, -- UNEMPLOYED, SELF_EMPLOYED, EMPLOYED, BUSINESS_OWNER
	employer_inn 			VARCHAR(12),
    salary 					DECIMAL,
    "position" 				INT REFERENCES "position" (id) ON DELETE CASCADE ON UPDATE NO ACTION        UNIQUE, -- WORKER, MID_MANAGER, TOP_MANAGER, OWNER
	work_experience_total 	INT,
    work_experience_current INT
);

CREATE TABLE IF NOT EXISTS credit
(
    credit_id 			BIGINT PRIMARY KEY,
    amount 				DECIMAL,
    term 				INT,
    monthly_payment 	DECIMAL,
    rate 				DECIMAL,
    psk 				DECIMAL,
    payment_schedule 	JSONB,
    insurance_enable 	BOOLEAN,
    salary_client 		BOOLEAN,
    credit_status 		INT REFERENCES credit_status (id) ON DELETE CASCADE ON UPDATE NO ACTION     UNIQUE -- CALCULATED, ISSUED
    );

CREATE TABLE IF NOT EXISTS client
(
    client_id 			BIGINT PRIMARY KEY,
    last_name 			VARCHAR                                                                                     NOT NULL,
    first_name 			VARCHAR                                                                                     NOT NULL,
    middle_name 		VARCHAR,
    birthdate 			DATE                                                                                        NOT NULL,
    email 				VARCHAR,
    gender 				INT REFERENCES gender (id) ON DELETE CASCADE ON UPDATE NO ACTION                            UNIQUE, -- MALE, FEMALE, NON_BINARY
	marital_status 		INT REFERENCES marital_status (id) ON DELETE CASCADE ON UPDATE NO ACTION                    UNIQUE, -- MARRIED, DIVORCED, SINGLE, WIDOW_WIDOWER
	dependent_amount 	INT,
    passport_id 		BIGINT REFERENCES passport (passport_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL     UNIQUE,
    employment_id 		BIGINT REFERENCES employment (employment_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL UNIQUE,
    account 			VARCHAR
);

CREATE TABLE IF NOT EXISTS application
(
    application_id 	BIGINT PRIMARY KEY,
    client_id 	   	BIGINT REFERENCES client (client_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL     UNIQUE,
    credit_id 	   	BIGINT REFERENCES credit (credit_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL     UNIQUE,
    status         	INT REFERENCES application_status (id) ON DELETE CASCADE ON UPDATE NO ACTION            UNIQUE, -- PREAPPROVAL, APPROVED, CC_DENIED, CC_APPROVED, PREPARE_DOCUMENTS, DOCUMENT_CREATED, CLIENT_DENIED, DOCUMENT_SIGNED, CREDIT_ISSUED
	creation_date  	TIMESTAMP,
    applied_offer  	JSONB,
    sign_date 	   	TIMESTAMP,
    ses_code 	   	BIGINT,
    status_history 	JSONB                                                                                   NOT NULL UNIQUE
);
