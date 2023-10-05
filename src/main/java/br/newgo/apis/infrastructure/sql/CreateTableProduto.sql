-- Extensão necessária para gerar UUIDs
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Cria uma sequência para gerar IDs para produtos
CREATE SEQUENCE produtos_id_seq
    START 1
    INCREMENT 1
    MINVALUE 1
    NO MAXVALUE
    CACHE 1;

-- Define a tabela PRODUTO
CREATE TABLE PRODUTOS (
    -- ID único para cada produto gerado pela sequência
    id bigint DEFAULT nextval('produtos_id_seq') PRIMARY KEY,
    -- UUID único gerado automaticamente
    hash uuid DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    -- Nome do produto
    nome varchar(255),
    -- Descrição textual do produto
    descricao text,
    -- Código EAN13
    ean13 varchar(13),
    -- Preço do produto
    preco numeric(13,2),
    -- Quantidade em estoque
    quantidade numeric(13,2),
    -- Estoque mínimo
    estoque_min numeric(13,2),
    -- Data de criação do registro
    dtcreate timestamp,
    -- Data de última atualização do registro
    dtupdate timestamp,
    -- Indicador de ativo/inativo
    lativo boolean
);