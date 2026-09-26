CREATE TABLE ledger_entries (
	id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	transaction_id BIGINT NOT NULL,
	account_id BIGINT NOT NULL, -- bağlı olduğu hesap
	amount NUMERIC(19,2) NOT NULL CHECK (amount <> 0), -- + giriş, - çıkış
	created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
	
	CONSTRAINT fk_ledger_account FOREIGN KEY (account_id) REFERENCES accounts(id),
	CONSTRAINT fk_ledger_transaction FOREIGN KEY (transaction_id) REFERENCES transactions(id)
	
);

CREATE INDEX idx_ledger_entries_account_id ON ledger_entries (account_id);
CREATE INDEX idx_ledger_entries_transaction_id ON ledger_entries (transaction_id);