INSERT INTO usuarios (id, nome_completo, email, senha, perfil, status_registro)
VALUES (
	1,
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
    (1, 'Escola de Tecnologia e Sistemas', 'ATIVO', 1, NULL);

ALTER SEQUENCE instituicoes_ensino_id_seq RESTART WITH 2;
ALTER SEQUENCE escolas_id_seq RESTART WITH 2;
ALTER SEQUENCE usuarios_id_seq RESTART WITH 2;


