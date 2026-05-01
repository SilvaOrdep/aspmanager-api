INSERT INTO usuarios (nome_completo, email, senha, perfil, status_registro)
VALUES (
	'Administrador do Sistema',
	'admin@ucsal.com.br',
	'$2a$12$Mkk.h4OJE8Icu4FLNvuBXuG4cvPK0x7qHjbZOdgAhgUPqajHD20Ku',
	'ADMIN',
	'ATIVO'
);

INSERT INTO instituicoes_ensino (id, nome, endereco)
VALUES
    (1, 'UCSAL - Campus Pituaçu', 'Av. Pinto de Aguiar, 2589 - Pituaçu, Salvador - BA');

INSERT INTO escolas (id, nome, status_registro, id_instituicao, id_professor_coordenador)
VALUES
    (1, 'Escola de Educação, Cultura e Humanidades', 'ATIVO', 1, NULL),
    (2, 'Escola de Ciências Sociais e Aplicadas', 'ATIVO', 1, NULL),
    (3, 'Escola de Engenharias e Ciências Tecnológicas', 'ATIVO', 1, NULL),
    (4, 'Escola de Ciências Naturais e da Saúde', 'ATIVO', 1, NULL);


INSERT INTO usuarios (id, nome_completo, email, senha, perfil, status_registro)
VALUES (
	2,
	'OsvaldoRequiao Mello',
	'osvaldo.requiao@ucsal.com.br',
	'$2a$12$fS4l.WRsZSRHHPqrWSpfduo5dbByLjMhfxrvxowMVNqT2gVpl30Te',
	'PROFESSOR',
	'ATIVO'
);

INSERT INTO professores (id, matricula, id_usuario, id_escola)
VALUES
    (1, 200033111, 2, 3);

UPDATE escolas
SET id_professor_coordenador = 1
WHERE id = 3;

ALTER SEQUENCE instituicoes_ensino_id_seq RESTART WITH 2;
ALTER SEQUENCE escolas_id_seq RESTART WITH 5;
ALTER SEQUENCE usuarios_id_seq RESTART WITH 3;
ALTER SEQUENCE professores_id_seq RESTART WITH 2;
