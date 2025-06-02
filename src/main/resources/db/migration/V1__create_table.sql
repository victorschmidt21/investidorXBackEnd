CREATE TABLE `user` (
                        `id` varchar(255) NOT NULL,
                        `username` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
);

CREATE TABLE `states` (
                          `id` bigint NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) DEFAULT NULL,
                          PRIMARY KEY (`id`)
);

CREATE TABLE `city` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `nome` varchar(255) DEFAULT NULL,
                        `state_id` bigint NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `FKupmjeinovqe7uk3k55v5cii1` (`state_id`),
                        CONSTRAINT `FKupmjeinovqe7uk3k55v5cii1` FOREIGN KEY (`state_id`) REFERENCES `states` (`id`)
);

CREATE TABLE `address` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `cep` int DEFAULT NULL,
                           `neighborhood` varchar(255) DEFAULT NULL,
                           `number` int DEFAULT NULL,
                           `street` varchar(255) DEFAULT NULL,
                           `city_id` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKpo044ng5x4gynb291cv24vtea` (`city_id`),
                           CONSTRAINT `FKpo044ng5x4gynb291cv24vtea` FOREIGN KEY (`city_id`) REFERENCES `city` (`id`)
);

CREATE TABLE `owner` (
                         `id` bigint NOT NULL AUTO_INCREMENT,
                         `ativo` bit(1) DEFAULT NULL,
                         `cpf_cnpj` varchar(255) DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `name` varchar(255) DEFAULT NULL,
                         `phone` varbinary(255) DEFAULT NULL,
                         `address_id` bigint DEFAULT NULL,
                         `user_id` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `FKda9u2g6vwepwq8fb26hiyxcdv` (`address_id`),
                         KEY `FKsi1e0ouv7mj9eg3ts4buj4wer` (`user_id`),
                         CONSTRAINT `FKda9u2g6vwepwq8fb26hiyxcdv` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
                         CONSTRAINT `FKsi1e0ouv7mj9eg3ts4buj4wer` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `imoveis` (
                           `id_imovel` bigint NOT NULL AUTO_INCREMENT,
                           `ativo` bit(1) DEFAULT NULL,
                           `date_value` date DEFAULT NULL,
                           `nome_imovel` varchar(255) DEFAULT NULL,
                           `value_registration` double DEFAULT NULL,
                           `adress_id` bigint DEFAULT NULL,
                           `owner_id` bigint DEFAULT NULL,
                           `user_id` varchar(255) NOT NULL,
                           PRIMARY KEY (`id_imovel`),
                           KEY `FKe50961dc84fhcag2mgtavcjr` (`adress_id`),
                           KEY `FK9a00j2xyb85niikw7q7c4li9n` (`owner_id`),
                           KEY `FKimlnn5xil9opqxer9x5xcglre` (`user_id`),
                           CONSTRAINT `FK9a00j2xyb85niikw7q7c4li9n` FOREIGN KEY (`owner_id`) REFERENCES `owner` (`id`),
                           CONSTRAINT `FKe50961dc84fhcag2mgtavcjr` FOREIGN KEY (`adress_id`) REFERENCES `address` (`id`),
                           CONSTRAINT `FKimlnn5xil9opqxer9x5xcglre` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE `valuation` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `date` date DEFAULT NULL,
                             `description` varchar(255) DEFAULT NULL,
                             `name_responsible` varchar(255) DEFAULT NULL,
                             `rota_image` varchar(255) DEFAULT NULL,
                             `value` double NOT NULL,
                             `imovel_id_imovel` bigint DEFAULT NULL,
                             PRIMARY KEY (`id`),
                             KEY `FKmxoytjudr8319j9j2079qrsbe` (`imovel_id_imovel`),
                             CONSTRAINT `FKmxoytjudr8319j9j2079qrsbe` FOREIGN KEY (`imovel_id_imovel`) REFERENCES `imoveis` (`id_imovel`)
);

CREATE TABLE `renevue` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `date` date DEFAULT NULL,
                           `description` varchar(255) DEFAULT NULL,
                           `title` varchar(255) DEFAULT NULL,
                           `value` double NOT NULL,
                           `imovel_id_imovel` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FK4l7dn81d0x1cspbin8bnhamnr` (`imovel_id_imovel`),
                           CONSTRAINT `FK4l7dn81d0x1cspbin8bnhamnr` FOREIGN KEY (`imovel_id_imovel`) REFERENCES `imoveis` (`id_imovel`)
);

CREATE TABLE `expense` (
                           `id` bigint NOT NULL AUTO_INCREMENT,
                           `date` date DEFAULT NULL,
                           `description` varchar(255) DEFAULT NULL,
                           `title` varchar(255) DEFAULT NULL,
                           `value` double NOT NULL,
                           `imovel_id_imovel` bigint DEFAULT NULL,
                           PRIMARY KEY (`id`),
                           KEY `FKm6chu0g5dbqt19retkhqn82x3` (`imovel_id_imovel`),
                           CONSTRAINT `FKm6chu0g5dbqt19retkhqn82x3` FOREIGN KEY (`imovel_id_imovel`) REFERENCES `imoveis` (`id_imovel`)
);

CREATE TABLE `incc` (
                        `id` bigint NOT NULL AUTO_INCREMENT,
                        `date` date NOT NULL,
                        `indice` double NOT NULL,
                        PRIMARY KEY (`id`)
);
