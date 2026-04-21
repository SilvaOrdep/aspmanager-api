INSERT INTO usuarios (id, nome_completo, email, senha, perfil, status_registro)
VALUES (
	1,
	'Administrador do Sistema',
	'admin@ucsal.com.br',
	'$2a$12$Mkk.h4OJE8Icu4FLNvuBXuG4cvPK0x7qHjbZOdgAhgUPqajHD20Ku',
	'ADMIN',
	'ATIVO'
);

ALTER SEQUENCE usuarios_id_seq RESTART WITH 2;