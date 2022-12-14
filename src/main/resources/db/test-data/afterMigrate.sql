
set foreign_key_checks=0;

delete from cidade;
delete from cozinha;
delete from estado;
delete from forma_pagamento;
delete from grupo;
delete from grupo_permissao;
delete from permissao;
delete from produto;
delete from restaurante;
delete from restaurante_forma_pagamento;
delete from usuario;
delete from usuario_grupo;

set foreign_key_checks=1;

alter table cidade auto_increment=1;
alter table cozinha auto_increment=1;
alter table estado auto_increment=1;
alter table forma_pagamento auto_increment=1;
alter table grupo auto_increment=1;
alter table permissao auto_increment=1;
alter table produto auto_increment=1;
alter table restaurante auto_increment=1;
alter table usuario auto_increment=1;

insert into cozinha (id,nome) values (1,'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');

insert into estado (id, nome) values (1, 'São Paulo');
insert into estado (id, nome) values (2, 'Minas Gerais');
insert into estado (id, nome) values (3, 'Ceará');

insert into cidade (nome, estado_id) values ('Sâo Paulo', 1);
insert into cidade (nome, estado_id) values ('Felisburgo', 2);
insert into cidade (nome, estado_id) values ('Rio do Prado', 2);
insert into cidade (nome, estado_id) values ('Fortaleza', 3);

insert into restaurante (id, nome, taxa_frete, cozinha_id, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro, data_cadastro, data_atualizacao) values (1, 'China In Box', 12, 1, 3, '06857-847', 'Rua Cana verde', '57', 'Centro', utc_timestamp, utc_timestamp);
insert into restaurante (id, nome, taxa_frete, cozinha_id, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_complemento, endereco_bairro, data_cadastro, data_atualizacao) values (2, 'Kpop Chicken', 10, 1, 1, '04022-040', 'Rua Dr. Neyde Apparecida Sollito', '256', 'Ap 88', 'Vila Clementino', utc_timestamp, utc_timestamp);
insert into restaurante (id, nome, taxa_frete, cozinha_id, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro, data_cadastro, data_atualizacao) values (3, 'Busguers', 5, 2, 2, '39895-000', 'Rua Nossa Senhora das Graças', '156', 'Centro', utc_timestamp, utc_timestamp);

insert into forma_pagamento (id, descricao) values (1,'debito');
insert into forma_pagamento (id, descricao) values (2, 'credito');
insert into forma_pagamento (id, descricao) values (3, 'pix');

insert into permissao (id, nome, descricao) values (1, 'CONSULTAR_COZINHA', 'permissao para consulta de cozinha');
insert into permissao (id, nome, descricao) values (2, 'EDITAR_COZINHA', 'permissao para editar cozinha');
insert into permissao (id, nome, descricao) values (3, 'CONSULTAR_EDITAR_COZINHA', 'permissao para editar e consultar cozinha');

insert into restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) values (1,1), (1,2), (1,3), (2,3), (3,2), (3,3);

insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Bife a Cavalo', 'Bife grelhado na chapa com ovo frito e arroz', 25, true, 2);
insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Prato Feito', 'Prato feito (PF) - mistura a escolha com feijão, arroz, purê de batata e salada ou farofa', 18, true, 1);
insert into produto (nome, descricao, preco, ativo, restaurante_id) values ('Filé Mignon', 'Delicioso filé mignon grelhado acompanhado com vinagrete e arroz', 40, false, 2);

insert into grupo (nome) values ('ADMIN');
insert into grupo (nome) values ('READ');
insert into grupo (nome) values ('WRITE');

insert into grupo_permissao (grupo_id, permissao_id) values (1,1), (1,2), (1,3), (2,1), (3,2);

insert into usuario (nome, email, senha, data_cadastro) values ('Valdionor Junior Rodrigues Gil', 'valdionor.teste@gmail.com', '123456', utc_timestamp);
insert into usuario (nome, email, senha, data_cadastro) values ('Macio Atalaia', 'atalaia.teste@gmail.com', '123456', utc_timestamp);
insert into usuario (nome, email, senha, data_cadastro) values ('Claudia Sphinela', 'sphinela.teste@gmail.com', '123456', utc_timestamp);
insert into usuario (nome, email, senha, data_cadastro) values ('Manuel de Jesus', 'm-jesus.teste@gmail.com', '123456', utc_timestamp);

insert into usuario_grupo (usuario_id, grupo_id) values (1,1), (1,2), (1,3), (2,2), (3,3), (4,2), (4,3);
