CREATE TABLE IF NOT EXISTS application
(
    application_id bigint,
    client_id bigint,
    credit_id bigint,
    status VARCHAR, -- PREAPPROVAL, APPROVED, CC_DENIED, CC_APPROVED, PREPARE_DOCUMENTS, DOCUMENT_CREATED, CLIENT_DENIED, DOCUMENT_SIGNED, CREDIT_ISSUED
    creation_date TIMESTAMP,
    applied_offer jsonb,
    sign_date TIMESTAMP,
    ses_code bigint,
    status_history jsonb
);

CREATE TABLE IF NOT EXISTS credit
(
    credit_id bigint,
    amount decimal,
    term int,
    monthly_payment decimal,
    rate decimal,
    psk decimal,
    payment_schedule jsonb,
    insurance_enable boolean,
    salary_client boolean,
    credit_status varchar(15) -- CALCULATED, ISSUED
    );

CREATE TABLE IF NOT EXISTS client
(
    client_id bigint,
    last_name varchar,
    first_name varchar,
    middle_name varchar,
    birthdate date,
    email varchar,
    gender varchar, -- MALE, FEMALE, NON_BINARY
    marital_status varchar, -- MARRIED, DIVORCED, SINGLE, WIDOW_WIDOWER
    dependent_amount int,
    passport_id bigint,
    employment_id bigint,
    account varchar
);

CREATE TABLE IF NOT EXISTS passport
(
    passport_id bigint,
    series varchar(4),
    number varchar(6),
    issue_branch varchar,
    issue_date date
    );

CREATE TABLE IF NOT EXISTS employment
(
    employment_id bigint,
    status varchar, -- UNEMPLOYED SELF_EMPLOYED, EMPLOYED, BUSINESS_OWNER
    employer_inn varchar(12),
    salary decimal,
    position varchar, -- WORKER, MID_MANAGER, TOP_MANAGER, OWNER
    work_experience_total int,
    work_experience_current int
);
