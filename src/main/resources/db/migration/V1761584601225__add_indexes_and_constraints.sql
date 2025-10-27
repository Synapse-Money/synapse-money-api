CREATE INDEX IF NOT EXISTS idx_accounts_user_id ON accounts(user_id);
CREATE INDEX IF NOT EXISTS idx_accounts_is_active ON accounts(is_active);
CREATE INDEX IF NOT EXISTS idx_transactions_user_id ON transactions(user_id);
CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_date ON transactions(transaction_date);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(transaction_type);
CREATE INDEX IF NOT EXISTS idx_categories_user_id ON categories(user_id);
CREATE INDEX IF NOT EXISTS idx_credit_cards_user_id ON credit_cards(user_id);
CREATE INDEX IF NOT EXISTS idx_credit_card_bills_card_id ON credit_card_bills(credit_card_id);

ALTER TABLE credit_cards
    ADD CONSTRAINT chk_closing_day CHECK (closing_day BETWEEN 1 AND 31),
    ADD CONSTRAINT chk_due_day CHECK (due_day BETWEEN 1 AND 31),
    ADD CONSTRAINT chk_credit_limit CHECK (credit_limit > 0);

ALTER TABLE accounts
    ADD CONSTRAINT chk_initial_balance CHECK (initial_balance >= 0);

ALTER TABLE transactions
    ADD CONSTRAINT chk_amount CHECK (amount > 0);

CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email_unique ON users(LOWER(email));