-- Create Schema
--CREATE SCHEMA ${defaultSchema};



-- CONNECTION TYPE CATEGORY
CREATE TABLE IF NOT EXISTS ${defaultSchema}.connection_type_categories_tbl (
    id VARCHAR PRIMARY KEY,
    name VARCHAR NOT NULL,
    type VARCHAR,
    additional_info JSONB DEFAULT '{}'::JSONB,
    is_deleted BOOLEAN DEFAULT false,
    metadata JSONB DEFAULT '{}'::JSONB
    );

--CONNECTION TYPE

CREATE TABLE IF NOT EXISTS ${defaultSchema}.connection_type_tbl (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    short_name VARCHAR(255),
    short_code VARCHAR(255),
    type VARCHAR(255),
    connection_type_details JSONB DEFAULT '{}'::JSONB,
    metadata JSONB DEFAULT '{}'::JSONB,
    skip_flow_scripts BOOLEAN DEFAULT FALSE,
    is_global_connection_type BOOLEAN DEFAULT FALSE,
    is_private BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    version VARCHAR(50) NOT NULL,
    status VARCHAR(50) DEFAULT 'COMING_SOON'
);


-- CONNECTION DEFINITION TABLE

CREATE TABLE IF NOT EXISTS ${defaultSchema}.connection_definition_tbl (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    display_name VARCHAR(255) NOT NULL,
    object_type VARCHAR(50) NOT NULL,
    connection_type_id VARCHAR(36) NOT NULL,
    connection_type_name VARCHAR(255) NOT NULL,
    version VARCHAR(50) NOT NULL,
    attributes JSONB NOT NULL DEFAULT '[]'::JSONB,
    other JSONB DEFAULT '{}'::JSONB,
    metadata JSONB DEFAULT '{}'::JSONB,
    is_deleted BOOLEAN DEFAULT false,
    mode VARCHAR(50)
);

-- CONNECTION TOKEN TABLE

CREATE TABLE IF NOT EXISTS ${defaultSchema}.connection_token_tbl (
    id VARCHAR(36) PRIMARY KEY,
    connection_type_id VARCHAR(36) NOT NULL,
    project_id VARCHAR(36) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    connection_name VARCHAR(255),
    connection_id VARCHAR(36),
    is_reauthorization BOOLEAN DEFAULT FALSE,
    user_id VARCHAR (36),
    user_name VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    connection_properties JSONB DEFAULT '{}'::JSONB
    );




-- CONNECTION TABLE
CREATE TABLE IF NOT EXISTS ${defaultSchema}.connection_tbl (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    normalized_name VARCHAR(255),
    short_code VARCHAR(255),
    connection_object_type VARCHAR(255),
    connection_type_id VARCHAR(255) NOT NULL,
    connection_type_name VARCHAR(255),
    process_type VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL,
    account_id VARCHAR(36) NOT NULL,
    account_code VARCHAR(50) NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    project_id VARCHAR(36) NOT NULL,
    project_code VARCHAR(50) NOT NULL,
    project_name VARCHAR(255),
    properties JSONB DEFAULT '{}'::JSONB,
    custom_attributes JSONB DEFAULT '[]'::JSONB,
    metadata JSONB DEFAULT '{}'::JSONB,
    is_global_connection BOOLEAN DEFAULT FALSE,
    is_upgrade_available BOOLEAN DEFAULT FALSE,
    is_editable BOOLEAN DEFAULT FALSE,
    is_private BOOLEAN DEFAULT FALSE,
    is_invalid BOOLEAN DEFAULT FALSE,
    invalidation_reason VARCHAR(255) DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE,
    version DECIMAL
);
