insert into cozinha (id,nome) values (1,'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');

insert into restaurante (nome, taxa_frete, cozinha_id) values ('China In Box', 12, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Kpop Chicken', 10, 1);
insert into restaurante (nome, taxa_frete,  cozinha_id) values ('Busguers', 5, 2);

insert into forma_pagamento (id,descricao) values (1,'debito');
insert into forma_pagamento (id, descricao) values (2, 'credito');
insert into forma_pagamento (id, descricao) values (3, 'pix');

insert into permissao (id, nome, descricao) values (1, 'CONSULTAR_COZINHA', 'permissao para consulta de cozinha');
insert into permissao (id, nome, descricao) values (2, 'EDITAR_COZINHA', 'permissao para editar cozinha');
insert into permissao (id, nome, descricao) values (3, 'CONSULTAR_EDITAR_COZINHA', 'permissao para editar e consultar cozinha');

insert into estado (id, nome) values (1, 'São Paulo');
insert into estado (id, nome) values (2, 'Minas Gerais');
insert into estado (id, nome) values (3, 'Ceará');

insert into cidade (nome, estado_id) values ('Sâo Paulo', 1);
insert into cidade (nome, estado_id) values ('Felisburgo', 2);
insert into cidade (nome, estado_id) values ('Rio do Prado', 2);
insert into cidade (nome, estado_id) values ('Fortaleza', 3);

insert into restaurante_forma_pagamento (restaurante_id, forma_pagamento_id) values (1,1), (1,2), (1,3), (2,3), (3,2), (3,3);