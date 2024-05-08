CREATE TABLE bank_account
(
    id             uuid         NOT NULL PRIMARY KEY,
    account_number numeric      NOT NULL,
    balance        numeric      NOT NULL,
    owner_id       varchar(255) NOT NULL
);