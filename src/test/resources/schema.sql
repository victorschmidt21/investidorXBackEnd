CREATE TABLE states (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE city (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    state_id BIGINT NOT NULL,
    CONSTRAINT fk_city_state FOREIGN KEY (state_id) REFERENCES states(id)
);

CREATE TABLE address (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    street VARCHAR(255),
    number INT,
    neighborhood VARCHAR(255),
    cep VARCHAR(10),
    city_id BIGINT,
    CONSTRAINT fk_city FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE user (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(255)
);

CREATE TABLE owner (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    cpf_cnpj VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(255),
    ativo BOOLEAN DEFAULT TRUE,
    address_id BIGINT,
    user_id VARCHAR(255),         
    CONSTRAINT fk_owner_address FOREIGN KEY (address_id) REFERENCES address(id),
    CONSTRAINT fk_owner_users FOREIGN KEY (user_id) REFERENCES user(id)
);

CREATE TABLE imoveis (
    id_imovel BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome_imovel VARCHAR(255),
    value_registration DOUBLE,
    date_value DATE,
    ativo BOOLEAN DEFAULT TRUE,
    adress_id BIGINT,
    user_id VARCHAR(255) NOT NULL,
    owner_id BIGINT,
    CONSTRAINT fk_imovel_address FOREIGN KEY (adress_id) REFERENCES address(id),
    CONSTRAINT fk_imovel_users FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_imovel_owner FOREIGN KEY (owner_id) REFERENCES owner(id)
);

CREATE TABLE expense (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    value_expense DOUBLE,
    dates DATE,
    imovel_id BIGINT NOT NULL,
    CONSTRAINT fk_expense_imovel FOREIGN KEY (imovel_id) REFERENCES imoveis(id_imovel)
);

CREATE TABLE renevue (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    value DOUBLE,
    date DATE,
    imovel_id BIGINT NOT NULL,
    CONSTRAINT fk_renevue_imovel FOREIGN KEY (imovel_id) REFERENCES imoveis(id_imovel)
);

CREATE TABLE valuation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name_responsible VARCHAR(255),
    date DATE,
    description TEXT,
    rota_image VARCHAR(255),
    value DOUBLE,
    imovel_id BIGINT,
    CONSTRAINT fk_valuation_imovel FOREIGN KEY (imovel_id) REFERENCES imoveis(id_imovel)
);

CREATE TABLE incc (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    indice DOUBLE,
    date DATE NOT NULL
);
