-- Space Chemistry — esquema de highscores
-- Ejecutar en SQL Server (Express o superior).
-- IMPORTANTE: no ejecutes la sección de DROP en entornos con datos.

CREATE DATABASE spaceInvaders;
GO

USE spaceInvaders;
GO

IF OBJECT_ID('dbo.highscore', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.highscore (
        id INT NOT NULL PRIMARY KEY IDENTITY(1, 1),
        username VARCHAR(60) NOT NULL,
        score INT NOT NULL
    );
END
GO

-- Datos de ejemplo (opcional)
-- INSERT INTO dbo.highscore (username, score) VALUES ('John', 10000);
-- INSERT INTO dbo.highscore (username, score) VALUES ('Demo', 5000);

-- Consulta de top 5
-- SELECT TOP 5 username, score FROM dbo.highscore ORDER BY score DESC;

-- =====================================================================
-- PELIGRO: solo para reset total en desarrollo
-- =====================================================================
-- USE master;
-- GO
-- DROP DATABASE spaceInvaders;
-- GO
