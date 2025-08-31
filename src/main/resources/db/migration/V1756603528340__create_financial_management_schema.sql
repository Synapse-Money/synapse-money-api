DO
$$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'transaction_type_enum') THEN
            CREATE TYPE transaction_type_enum AS ENUM ('income', 'expense', 'transfer');
        END IF;
    END
$$;

CREATE SEQUENCE IF NOT EXISTS users_id_seq;
CREATE SEQUENCE IF NOT EXISTS financial_institutions_id_seq;
CREATE SEQUENCE IF NOT EXISTS account_types_id_seq;
CREATE SEQUENCE IF NOT EXISTS accounts_id_seq;
CREATE SEQUENCE IF NOT EXISTS credit_card_brands_id_seq;
CREATE SEQUENCE IF NOT EXISTS credit_cards_id_seq;
CREATE SEQUENCE IF NOT EXISTS categories_id_seq;
CREATE SEQUENCE IF NOT EXISTS subcategories_id_seq;
CREATE SEQUENCE IF NOT EXISTS transactions_id_seq;
CREATE SEQUENCE IF NOT EXISTS credit_card_bills_id_seq;

CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT    DEFAULT nextval('users_id_seq') PRIMARY KEY,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    first_name VARCHAR(100)        NOT NULL,
    last_name  VARCHAR(100)        NOT NULL,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS financial_institutions
(
    id         BIGINT    DEFAULT nextval('financial_institutions_id_seq') PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    logo_url   VARCHAR(255),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS account_types
(
    id          BIGINT    DEFAULT nextval('account_types_id_seq') PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS accounts
(
    id                       BIGINT         DEFAULT nextval('accounts_id_seq') PRIMARY KEY,
    user_id                  BIGINT       NOT NULL,
    financial_institution_id BIGINT       NOT NULL,
    account_type_id          BIGINT       NOT NULL,
    name                     VARCHAR(100) NOT NULL,
    initial_balance          DECIMAL(12, 2) DEFAULT 0.00,
    current_balance          DECIMAL(12, 2) DEFAULT 0.00,
    is_default               BOOLEAN        DEFAULT FALSE,
    is_active                BOOLEAN        DEFAULT TRUE,
    created_at               TIMESTAMP      DEFAULT now(),
    updated_at               TIMESTAMP      DEFAULT now(),
    CONSTRAINT fk_accounts_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_accounts_financial_institution FOREIGN KEY (financial_institution_id) REFERENCES financial_institutions (id) ON DELETE SET NULL,
    CONSTRAINT fk_accounts_account_type FOREIGN KEY (account_type_id) REFERENCES account_types (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS credit_card_brands
(
    id         BIGINT    DEFAULT nextval('credit_card_brands_id_seq') PRIMARY KEY,
    name       VARCHAR(50) NOT NULL,
    logo_url   VARCHAR(255),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS credit_cards
(
    id                   BIGINT    DEFAULT nextval('credit_cards_id_seq') PRIMARY KEY,
    user_id              BIGINT         NOT NULL,
    account_id           BIGINT         NOT NULL,
    credit_card_brand_id BIGINT         NOT NULL,
    name                 VARCHAR(100)   NOT NULL,
    credit_limit         DECIMAL(12, 2) NOT NULL,
    closing_day          INTEGER        NOT NULL,
    due_day              INTEGER        NOT NULL,
    last_four_digits     VARCHAR(4),
    is_default           BOOLEAN   DEFAULT FALSE,
    is_active            BOOLEAN   DEFAULT TRUE,
    created_at           TIMESTAMP DEFAULT now(),
    updated_at           TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_credit_cards_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_credit_cards_account FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE SET NULL,
    CONSTRAINT fk_credit_cards_brand FOREIGN KEY (credit_card_brand_id) REFERENCES credit_card_brands (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id               BIGINT    DEFAULT nextval('categories_id_seq') PRIMARY KEY,
    user_id          BIGINT                NOT NULL,
    name             VARCHAR(100)          NOT NULL,
    color            VARCHAR(7),
    icon             VARCHAR(50),
    transaction_type transaction_type_enum NOT NULL,
    is_default       BOOLEAN   DEFAULT FALSE,
    created_at       TIMESTAMP DEFAULT now(),
    updated_at       TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_categories_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS subcategories
(
    id          BIGINT    DEFAULT nextval('subcategories_id_seq') PRIMARY KEY,
    category_id BIGINT       NOT NULL,
    name        VARCHAR(100) NOT NULL,
    color       VARCHAR(7),
    icon        VARCHAR(50),
    created_at  TIMESTAMP DEFAULT now(),
    updated_at  TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_subcategories_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS transactions
(
    id                  BIGINT    DEFAULT nextval('transactions_id_seq') PRIMARY KEY,
    user_id             BIGINT                NOT NULL,
    account_id          BIGINT                NOT NULL,
    category_id         BIGINT,
    subcategory_id      BIGINT,
    credit_card_id      BIGINT,
    transfer_account_id BIGINT,
    amount              DECIMAL(12, 2)        NOT NULL,
    description         VARCHAR(255),
    transaction_date    DATE                  NOT NULL,
    transaction_type    transaction_type_enum NOT NULL,
    is_recurring        BOOLEAN   DEFAULT FALSE,
    created_at          TIMESTAMP DEFAULT now(),
    updated_at          TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_transactions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_account FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE,
    CONSTRAINT fk_transactions_category FOREIGN KEY (category_id) REFERENCES categories (id) ON DELETE SET NULL,
    CONSTRAINT fk_transactions_subcategory FOREIGN KEY (subcategory_id) REFERENCES subcategories (id) ON DELETE SET NULL,
    CONSTRAINT fk_transactions_credit_card FOREIGN KEY (credit_card_id) REFERENCES credit_cards (id) ON DELETE SET NULL,
    CONSTRAINT fk_transactions_transfer_account FOREIGN KEY (transfer_account_id) REFERENCES accounts (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS credit_card_bills
(
    id              BIGINT    DEFAULT nextval('credit_card_bills_id_seq') PRIMARY KEY,
    credit_card_id  BIGINT         NOT NULL,
    reference_month INTEGER        NOT NULL,
    reference_year  INTEGER        NOT NULL,
    total_amount    DECIMAL(12, 2) NOT NULL,
    due_date        DATE           NOT NULL,
    closing_date    DATE           NOT NULL,
    is_paid         BOOLEAN   DEFAULT FALSE,
    payment_date    DATE,
    created_at      TIMESTAMP DEFAULT now(),
    updated_at      TIMESTAMP DEFAULT now(),
    CONSTRAINT fk_credit_card_bills_credit_card FOREIGN KEY (credit_card_id) REFERENCES credit_cards (id) ON DELETE CASCADE
);