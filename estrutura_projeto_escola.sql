-- --------------------------------------------------------
-- Servidor:                     127.0.0.1
-- Versão do servidor:           10.4.25-MariaDB - mariadb.org binary distribution
-- OS do Servidor:               Win64
-- HeidiSQL Versão:              12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Copiando estrutura do banco de dados para escola
CREATE DATABASE IF NOT EXISTS `escola` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `escola`;

-- Copiando estrutura para tabela escola.aluno
CREATE TABLE IF NOT EXISTS `aluno` (
  `matricula` int(11) NOT NULL AUTO_INCREMENT,
  `nm_aluno` varchar(50) NOT NULL DEFAULT '',
  `cpf_aluno` varchar(50) NOT NULL DEFAULT '',
  `endereco_aluno` varchar(50) NOT NULL DEFAULT '',
  `em_aluno` varchar(50) NOT NULL DEFAULT '',
  `cel_aluno` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`matricula`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.aluno: ~0 rows (aproximadamente)
DELETE FROM `aluno`;

-- Copiando estrutura para tabela escola.curso
CREATE TABLE IF NOT EXISTS `curso` (
  `cd_curso` int(11) NOT NULL AUTO_INCREMENT,
  `nm_curso` varchar(50) NOT NULL DEFAULT '',
  `carga_horaria` int(11) NOT NULL DEFAULT 0,
  `ds_curso` varchar(50) NOT NULL DEFAULT '',
  `cd_funcionario` int(11) DEFAULT NULL,
  PRIMARY KEY (`cd_curso`),
  KEY `cd_funcionario` (`cd_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.curso: ~0 rows (aproximadamente)
DELETE FROM `curso`;

-- Copiando estrutura para tabela escola.curso_aluno
CREATE TABLE IF NOT EXISTS `curso_aluno` (
  `cd_curso` int(11) NOT NULL,
  `matricula` int(11) NOT NULL,
  PRIMARY KEY (`cd_curso`,`matricula`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.curso_aluno: ~0 rows (aproximadamente)
DELETE FROM `curso_aluno`;

-- Copiando estrutura para tabela escola.dias_semana
CREATE TABLE IF NOT EXISTS `dias_semana` (
  `cd_dia` int(11) NOT NULL,
  `ds_dia` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`cd_dia`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.dias_semana: ~0 rows (aproximadamente)
DELETE FROM `dias_semana`;
INSERT INTO `dias_semana` (`cd_dia`, `ds_dia`) VALUES
	(1, 'segunda'),
	(2, 'terça'),
	(3, 'quarta'),
	(4, 'quinta'),
	(5, 'sexta');

-- Copiando estrutura para tabela escola.professor
CREATE TABLE IF NOT EXISTS `professor` (
  `cd_funcionario` int(11) NOT NULL AUTO_INCREMENT,
  `nm_professor` varchar(50) DEFAULT NULL,
  `cpf_professor` varchar(50) DEFAULT NULL,
  `endereco_professor` varchar(50) DEFAULT NULL,
  `em_professor` varchar(50) DEFAULT NULL,
  `cel_professor` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`cd_funcionario`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.professor: ~0 rows (aproximadamente)
DELETE FROM `professor`;

-- Copiando estrutura para tabela escola.sala
CREATE TABLE IF NOT EXISTS `sala` (
  `cd_sala` int(11) NOT NULL AUTO_INCREMENT,
  `nm_sala` varchar(50) NOT NULL DEFAULT '',
  `local_sala` varchar(50) NOT NULL DEFAULT '',
  `capacidade_total` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`cd_sala`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.sala: ~0 rows (aproximadamente)
DELETE FROM `sala`;

-- Copiando estrutura para tabela escola.turma
CREATE TABLE IF NOT EXISTS `turma` (
  `cd_curso` int(11) NOT NULL,
  `cd_sala` int(11) NOT NULL,
  `cd_dia` int(11) NOT NULL,
  PRIMARY KEY (`cd_curso`,`cd_sala`,`cd_dia`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Copiando dados para a tabela escola.turma: ~0 rows (aproximadamente)
DELETE FROM `turma`;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
