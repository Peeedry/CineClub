CREATE TABLE IF NOT EXISTS Usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    tipo CHAR(1) DEFAULT 'U',
    ativo CHAR(1) DEFAULT 'S'
);

CREATE TABLE IF NOT EXISTS Filmes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    genero VARCHAR(100),
    duracao INT,
    sinopse TEXT,
    formato VARCHAR(30),
    valor DECIMAL(10,2),
    quantidade INT DEFAULT 0,
    poster VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Compras (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuarioID INT NOT NULL,
    dataCompra DATE NOT NULL,
    valorTotal DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    formaPagamento VARCHAR(30) NOT NULL DEFAULT 'DINHEIRO',

    FOREIGN KEY (usuarioID)
        REFERENCES Usuarios(id)
);

CREATE TABLE IF NOT EXISTS ItensCompra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compraID INT NOT NULL,
    filmeID INT NOT NULL,
    precoUnitario DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (compraID)
        REFERENCES Compras(id),

    FOREIGN KEY (filmeID)
        REFERENCES Filmes(id)
);