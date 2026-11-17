-- ULTIMAQUADRA - Oracle FIAP. Executar uma unica vez em schema sem essas tabelas.
-- ATENCAO: sem dados ficticios de ocorrencias e sem comandos DROP.
CREATE TABLE tb_unidade_saude (
    cd_unidade NUMBER(12) PRIMARY KEY,
    cd_cnes VARCHAR2(7 CHAR) NOT NULL UNIQUE,
    nm_unidade VARCHAR2(160 CHAR) NOT NULL,
    nm_bairro VARCHAR2(80 CHAR) NOT NULL,
    ds_endereco VARCHAR2(260 CHAR) NOT NULL,
    ds_referencia VARCHAR2(180 CHAR) NOT NULL,
    ds_unidade VARCHAR2(500 CHAR) NOT NULL,
    st_ativa NUMBER(1) DEFAULT 1 NOT NULL,
    CONSTRAINT ck_ubs_ativa CHECK (st_ativa IN (0,1))
);
COMMENT ON TABLE tb_unidade_saude IS 'Unidades Basicas de Saude referenciadas pelo UltimaQuadra';
CREATE SEQUENCE sq_tb_unidade_saude START WITH 1 INCREMENT BY 1 NOCACHE;

CREATE TABLE tb_trecho_acesso (
    cd_trecho NUMBER(12) PRIMARY KEY,
    cd_unidade NUMBER(12) NOT NULL,
    nm_trecho VARCHAR2(160 CHAR) NOT NULL,
    ds_trecho VARCHAR2(600 CHAR) NOT NULL,
    ds_distancia_media_entrada VARCHAR2(100 CHAR) NOT NULL,
    st_ativo NUMBER(1) DEFAULT 1 NOT NULL,
    CONSTRAINT fk_trecho_unidade FOREIGN KEY (cd_unidade) REFERENCES tb_unidade_saude(cd_unidade),
    CONSTRAINT uq_trecho_unidade_nome UNIQUE (cd_unidade, nm_trecho),
    CONSTRAINT ck_trecho_ativo CHECK (st_ativo IN (0,1))
);
COMMENT ON TABLE tb_trecho_acesso IS 'Areas de acesso vinculadas a UBS; provisoria ate identificacao real';
CREATE SEQUENCE sq_tb_trecho_acesso START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE INDEX ix_trecho_unidade ON tb_trecho_acesso(cd_unidade);

CREATE TABLE tb_ocorrencia (
    cd_ocorrencia NUMBER(12) PRIMARY KEY,
    cd_trecho NUMBER(12) NOT NULL,
    nm_categoria VARCHAR2(60 CHAR) NOT NULL,
    ds_ocorrencia VARCHAR2(1500 CHAR) NOT NULL,
    ds_referencia_local VARCHAR2(350 CHAR),
    nm_passagem VARCHAR2(40 CHAR),
    nm_situacao VARCHAR2(40 CHAR) DEFAULT 'Relatada' NOT NULL,
    dt_relato DATE NOT NULL,
    ds_largura_livre VARCHAR2(100 CHAR),
    ds_altura_desnivel VARCHAR2(100 CHAR),
    ds_distancia_entrada VARCHAR2(100 CHAR),
    nm_impedimento VARCHAR2(20 CHAR),
    CONSTRAINT fk_ocorrencia_trecho FOREIGN KEY (cd_trecho) REFERENCES tb_trecho_acesso(cd_trecho),
    CONSTRAINT ck_ocorrencia_categoria CHECK (nm_categoria IN (
        'Buraco','Desnível','Passagem estreita','Obstáculo fixo','Obstáculo temporário',
        'Pavimentação irregular','Ausência de rebaixamento','Outros')),
    CONSTRAINT ck_ocorrencia_situacao CHECK (nm_situacao IN (
        'Relatada','Em verificação','Confirmada','Não confirmada','Resolvida')),
    CONSTRAINT ck_ocorrencia_passagem CHECK (nm_passagem IS NULL OR nm_passagem IN (
        'Dificultada','Bloqueada','Livre','Não sei informar')),
    CONSTRAINT ck_ocorrencia_impedimento CHECK (nm_impedimento IS NULL OR nm_impedimento IN ('Parcial','Total'))
);
COMMENT ON TABLE tb_ocorrencia IS 'Relatos comunitarios; unidade associada pelo trecho sem redundancia de FK';
CREATE SEQUENCE sq_tb_ocorrencia START WITH 1 INCREMENT BY 1 NOCACHE;
CREATE INDEX ix_ocorrencia_trecho ON tb_ocorrencia(cd_trecho);


-- DADOS INICIAIS: 7 UBS e 7 trechos genericos (0 ocorrencias)
-- Sete UBS da base.ts do React e uma area provisoria por UBS.
-- NENHUMA ocorrencia de obstaculo e criada.
-- Executar este script inteiro somente uma vez, sem inserts duplicados.

INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '2029618', 'UBS Bom Retiro — Dr. Octavio Augusto Rodovalho', 'Bom Retiro', 'Rua Tenente Pena, 8 — Bom Retiro, São Paulo — SP, CEP 01127-020', 'Rua Tenente Pena, 8', 'Unidade Básica de Saúde da rede municipal no bairro Bom Retiro.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '6048633', 'UBS Boracea', 'Barra Funda', 'Rua Boracea, 270 — Barra Funda, São Paulo — SP, CEP 01135-010', 'Rua Boracea, 270', 'Unidade Básica de Saúde da rede municipal na região da Barra Funda.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '2786834', 'UBS Cambuci', 'Cambuci', 'Avenida Lacerda Franco, 791 — Cambuci, São Paulo — SP, CEP 01536-000', 'Avenida Lacerda Franco, 791', 'Unidade Básica de Saúde da rede municipal no bairro Cambuci.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '2787113', 'UBS Humaitá — Dr. João de Azevedo Lage', 'Bela Vista', 'Rua Humaitá, 520 — Bela Vista, São Paulo — SP, CEP 01321-010', 'Rua Humaitá, 520', 'Unidade Básica de Saúde da rede municipal na Bela Vista.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '6090621', 'UBS República — Fernanda Sante Limeira', 'Centro', 'Rua Líbero Badaró, 282 — Centro, São Paulo — SP, CEP 01008-000', 'Rua Líbero Badaró, 282', 'Unidade Básica de Saúde da rede municipal na região central de São Paulo.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '2752336', 'UBS Santa Cecília — Dr. Humberto Pascale', 'Campos Elíseos', 'Rua Vitorino Carmilo, 599 — Campos Elíseos, São Paulo — SP, CEP 01153-000', 'Rua Vitorino Carmilo, 599', 'Unidade Básica de Saúde da rede municipal na região de Campos Elíseos.', 1);
INSERT INTO tb_unidade_saude (cd_unidade, cd_cnes, nm_unidade, nm_bairro, ds_endereco, ds_referencia, ds_unidade, st_ativa)
VALUES (sq_tb_unidade_saude.NEXTVAL, '5975220', 'UBS Sé', 'Sé', 'Rua Frederico Alvarenga, 259, térreo — Sé, São Paulo — SP, CEP 01020-030', 'Rua Frederico Alvarenga, 259', 'Unidade Básica de Saúde da rede municipal no distrito da Sé.', 1);

INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Tenente Pena, 8, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '2029618';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Boracea, 270, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '6048633';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Avenida Lacerda Franco, 791, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '2786834';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Humaitá, 520, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '2787113';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Líbero Badaró, 282, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '6090621';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Vitorino Carmilo, 599, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '2752336';
INSERT INTO tb_trecho_acesso (cd_trecho, cd_unidade, nm_trecho, ds_trecho, ds_distancia_media_entrada, st_ativo)
SELECT sq_tb_trecho_acesso.NEXTVAL, cd_unidade, 'Entorno imediato da UBS — local a indicar', 'Área de acesso próxima ao endereço Rua Frederico Alvarenga, 259, ainda sem divisão em trechos identificados. Ao relatar uma barreira, informe um ponto reconhecível próximo ao obstáculo.', 'Área sem delimitação específica', 1
FROM tb_unidade_saude WHERE cd_cnes = '5975220';
COMMIT;