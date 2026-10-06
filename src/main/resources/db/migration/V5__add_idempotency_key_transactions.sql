ALTER TABLE transactions
ADD COLUMN idempotency_key VARCHAR(64) CONSTRAINT uq_transactions_idempotency_key UNIQUE
