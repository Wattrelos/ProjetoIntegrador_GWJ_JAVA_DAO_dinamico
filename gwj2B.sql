--
-- Banco de dados: `gwj2`
--
CREATE DATABASE IF NOT EXISTS `gwj2` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_uca1400_ai_ci;
USE `gwj2`;

-- --------------------------------------------------------

--
-- Estrutura para tabela `cliente`
--

CREATE TABLE IF NOT EXISTS `cliente` (
  `id` int(11) NOT NULL,
  `nome` varchar(80) DEFAULT NULL COMMENT 'Nome completo',
  `sobrenome` varchar(80) DEFAULT NULL COMMENT 'Nome completo',
  `telefone` varchar(15) DEFAULT NULL COMMENT 'telefone',
  `cpf` varchar(14) DEFAULT NULL COMMENT 'CPF',
  `observacao` text DEFAULT NULL COMMENT 'Obsrvação',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Despejando dados para a tabela `cliente`
--

INSERT IGNORE INTO `cliente` (`id`, `nome`, `sobrenome`, `telefone`, `cpf`, `observacao`) VALUES
(1, 'Tiago', 'Zebedeu', '(11)999 999 999', '999.999.999-00', 'Prefera agendar às sextas-feiras'),
(12, 'João', 'Zebedeu', '(11)999 999 999', '999.999.999-00', 'Prefera agendar às sextas-feiras'),
(23, 'Paulo', 'deTarso', '(11)999 999 999', '999.999.999-00', 'Sempre fazer barba, cabelo e bigode.'),
(24, 'Matheus', 'Publicano', '(11)999 999 999', '999.999.999-00', 'Loção após a barba.'),
(29, 'Timóteo', 'MÃ©dico', '(11)999 999 999', '999.999.999-00', 'NÃ£o mexer na barba. nem no bigode.');

-- --------------------------------------------------------

--
-- Estrutura para tabela `endereco`
--

CREATE TABLE IF NOT EXISTS `endereco` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'Identificação',
  `cliente_id` int(11) NOT NULL COMMENT 'ID usuário',
  `nome` varchar(80) DEFAULT NULL COMMENT 'Nome de quem recebe',
  `logradouro` varchar(80) NOT NULL COMMENT 'Logradouro',
  `numero` varchar(5) NOT NULL COMMENT 'Número',
  `complemento` varchar(30) DEFAULT ' ' COMMENT 'Complemento',
  `bairro` varchar(40) NOT NULL COMMENT 'Bairro',
  `cidade` varchar(40) NOT NULL COMMENT 'Cidade',
  `estado` varchar(20) NOT NULL COMMENT 'Estado',
  `cep` varchar(9) NOT NULL COMMENT 'CEP',
  `data_cadastro` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Data de Cadastro',
  `observacao` mediumtext DEFAULT ' ' COMMENT 'Observação',
  UNIQUE KEY `Control` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=DYNAMIC;

--
-- Despejando dados para a tabela `endereco`
--

INSERT IGNORE INTO `endereco` (`id`, `cliente_id`, `nome`, `logradouro`, `numero`, `complemento`, `bairro`, `cidade`, `estado`, `cep`, `data_cadastro`, `observacao`) VALUES
(1, 1, 'Tiago Zebedeu', 'Rua Alex Wachholz', '895', 'Apt 309', 'Centro', 'Pomerode', 'Santa Catarina', '89355-370', '2026-04-09 23:04:54', 'Campainha quebrada'),
(3, 23, 'Paulo de Tarso', 'Rua Almirante Lucas A Boiteux', '272', '', 'Escola Agrícola', 'Blumenau', 'Santa Catarina', '89031-280', '2026-04-10 20:34:55', ''),
(4, 24, 'Matheus Publicano', 'Rua Oscar Freire', '973', '', 'Cerqueira César', 'São Paulo', 'São Paulo', '01426-003', '2026-04-15 18:03:56', 'Prefere agendar aos domingos.');

-- --------------------------------------------------------

--
-- Estrutura para tabela `servico`
--

CREATE TABLE IF NOT EXISTS `servico` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'identificação',
  `nome` varchar(120) NOT NULL COMMENT 'Nome do Serviço',
  `descricao` text NOT NULL COMMENT 'Descrição',
  `preco` double(10,2) NOT NULL COMMENT 'Preço',
  `duracao` int(11) NOT NULL COMMENT 'Duração em minutos',
  `tipo` varchar(120) NOT NULL COMMENT 'Nome do Serviço',
  `ativo` char(1) NOT NULL COMMENT 'Ativo',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci ROW_FORMAT=DYNAMIC;

--
-- Despejando dados para a tabela `servico`
--

INSERT IGNORE INTO `servico` (`id`, `nome`, `descricao`, `preco`, `duracao`, `tipo`, `ativo`) VALUES
(1, 'Corte padrão', 'Corte de cabelo masculino padrão', 30.00, 20, 'cabelo', '0'),
(2, 'Corte moicano', 'Corte de cabelo masculino estilo moicano', 40.00, 50, 'cabelo', '0'),
(3, 'Corte zero', 'Corte de cabelo masculino zero', 25.00, 10, 'cabelo', '0'),
(4, 'Corte em V', 'Estilo de corte de cabelo em V', 25.50, 10, 'cabelo', '0'),
(5, 'Corte militar', 'Estilo de corte de cabelo estilo exército', 19.99, 10, 'cabelo', '0'),
(7, 'Corte personalizado 2', 'Estilo personalizado 2 ao gosto do cliente', 75.40, 10, 'cabelo', '0'),
(10, 'Corte personalizado 3', 'Estilo personalizado 3 ao gosto do cliente', 75.40, 10, 'cabelo', '0');

-- --------------------------------------------------------

--
-- Estrutura para tabela `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `grupo_usuario_id` int(11) DEFAULT 0 COMMENT 'Grupo',
  `nome_usuario` varchar(20) DEFAULT NULL COMMENT 'Nome de usuário',
  `email` varchar(96) NOT NULL COMMENT 'email',
  `status` tinyint(1) DEFAULT 0 COMMENT 'Ativo?',
  `senha` varchar(255) DEFAULT NULL COMMENT 'Senha',
  `ip` varchar(40) DEFAULT NULL,
  `token` text DEFAULT NULL,
  `data_cadastro` timestamp NOT NULL DEFAULT current_timestamp() COMMENT 'Data de Cadastro',
  PRIMARY KEY (`id`,`email`),
  UNIQUE KEY `email` (`email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;

--
-- Despejando dados para a tabela `usuario`
--

INSERT IGNORE INTO `usuario` (`id`, `grupo_usuario_id`, `nome_usuario`, `email`, `status`, `senha`, `ip`, `token`, `data_cadastro`) VALUES
(1, 1, 'tiago', 'tiago.zebedeu@yahoo.com', 1, '#D1cipul0Am4d0', '172.22.222.222', '$UF2_3fcx5lmpxRTir8KgQQCst5TUbd6z2K6OGADuoqfxKdeyguO0ADLKYKrkOb8xZL0U', '2026-04-09 23:36:17'),
(12, 1, 'joao', 'joao.zebedeu@yahoo.com', 1, '#D1cipul0Am4d0', '172.22.222.222', '$UF2_3fcx5lmpxRTir8KgQQCst5TUbd6z2K6OGADuoqfxKdeyguO0ADLKYKrkOb8xZL0U', '2026-04-09 23:36:17'),
(23, 1, 'paulo', 'paulo.tarso5@gmail.com', 1, '#D1cipul0Am4d0', '172.22.222.221', '$UF2_3fcx5lmpxRTir8KgQQCst5TUbd6z2K6OGADuoqfxKdeyguO0ADLKYKrkOb8xZL0U', '2026-04-10 20:34:55'),
(24, 1, 'Matheus', 'matheus.publicano@gmail.com', 1, 'y6yr98I@#rwu', '172.22.222.221', '$UF2_3HlRRw6Z8I0OxWt4suLpUXUq4IHSXATluUUcS79wQxjo7tPvz0qrcQ6Nf53Z1Z86F', '2026-04-15 18:03:56'),
(29, NULL, 'timoteo', 'timoteo.efesio@gmail.com', 1, 'EmfEKoLwUqEucUOx', NULL, '$UF2_2AV191ZBp3WVFOBhta61PkP6bVVKjhi1ILVcXQeSpmW9AA3y8rOO1ljziwy9SI', '2026-04-29 18:39:06');
COMMIT;
