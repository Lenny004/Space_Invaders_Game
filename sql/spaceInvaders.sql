-- Space Chemistry — esquema histórico (SQL Server).
-- El juego ya NO usa SQL Server: la persistencia activa es SQLite
-- en data/space-chemistry.db (ver SqliteScoreRepository).
-- Este script se conserva solo como referencia del proyecto académico original.

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
