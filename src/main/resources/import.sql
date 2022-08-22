insert into cozinha (id,nome) values (1,'Tailandesa');
insert into cozinha (id, nome) values (2, 'Indiana');

insert into restaurante (nome, taxa_frete, cozinha_id) values ('China In Box', 12, 1);
insert into restaurante (nome, taxa_frete, cozinha_id) values ('Kpop Chicken', 10, 1);
insert into restaurante (nome, taxa_frete,  cozinha_id) values ('Busguers', 5, 2);

insert into forma_pagamento (id,descricao) values (1,'debito');
insert into forma_pagamento (id, descricao) values (2, 'credito');
insert into forma_pagamento (id, descricao) values (3, 'pix');

insert into permissao (id, nome, descricao) values (1, 'normal', 'permissao normal');
insert into permissao (id, nome, descricao) values (2, 'medio', 'permissao media');
insert into permissao (id, nome, descricao) values (3, 'forte', 'permissao forte');

insert into estado (id, nome) values (1, 'São Paulo');
insert into estado (id, nome) values (2, 'Minas Gerais');
insert into estado (id, nome) values (3, 'Ceará');

insert into cidade (nome, estado_id) values ('Sâo Paulo', 1);
insert into cidade (nome, estado_id) values ('Felisburgo', 2);
insert into cidade (nome, estado_id) values ('Rio do Prado', 2);
insert into cidade (nome, estado_id) values ('Fortaleza', 3);