-- account_type kolonunu varsayılan olarak 'CUSTOMER' olacak şekilde ekliyoruz
ALTER TABLE accounts
ADD COLUMN account_type VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER'
CHECK (account_type IN ('CUSTOMER', 'SYSTEM'));

-- Sistem (dış dünya) hesabını ilk kez oluşturuyoruz
INSERT INTO accounts(owner_name, currency, status, account_type)
VALUES ('SYSTEM_FUNDING', 'TRY', 'ACTIVE', 'SYSTEM');

-- Sadece account_type = 'SYSTEM' olan satırlar arasında UNIQUE'lik zorlar
CREATE UNIQUE INDEX idx_single_system_account 
ON accounts (account_type) 
WHERE account_type = 'SYSTEM';