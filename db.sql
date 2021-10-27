DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS favorites;

CREATE TABLE accounts (
    userID TEXT PRIMARY KEY,
    tokenID TEXT NOT NULL,
    email TEXT UNIQUE
);

CREATE TABLE news (
    id TEXT PRIMARY KEY,
    webTitle TEXT NOT NULL,
    webImage TEXT NOT NULL,
    webDesc TEXT NOT NULL,
    webUrl TEXT NOT NULL
);

CREATE TABLE favorites (
    account TEXT NOT NULL,
    content TEXT NOT NULL,
    PRIMARY KEY (account, content),
    FOREIGN KEY (account) REFERENCES accounts (userID),
    FOREIGN KEY (content) REFERENCES news (id)
);
