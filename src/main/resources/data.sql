CREATE TABLE Customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    surname VARCHAR(100),
    creditLimit DOUBLE,
    usedCreditLimit DOUBLE
);

CREATE TABLE Loan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customerId BIGINT,
    loanAmount DOUBLE,
    numberOfInstallments INT,
    createDate DATE,
    isPaid BOOLEAN,
    FOREIGN KEY (customerId) REFERENCES Customer(id)
);

CREATE TABLE LoanInstallment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    loanId BIGINT,
    amount DOUBLE,
    paidAmount DOUBLE,
    dueDate DATE,
    paymentDate DATE,
    isPaid BOOLEAN,
    FOREIGN KEY (loanId) REFERENCES Loan(id)
);


-- Insert test customers
INSERT INTO Customer (name, surname, creditLimit, usedCreditLimit)
VALUES
    ('John', 'Doe', 10000.00, 2000.00),
    ('Jane', 'Smith', 5000.00, 1500.00),
    ('Alice', 'Johnson', 8000.00, 1000.00),
    ('Bob', 'Williams', 12000.00, 4000.00);

-- Insert loans for customers
-- Loan 1: John Doe gets a loan of 2000 with 12 installments and 0.2 interest rate
INSERT INTO Loan (customerId, loanAmount, numberOfInstallments, createDate, isPaid)
VALUES
    (1, 2000.00, 12, '2025-01-01', FALSE),  -- John Doe's loan

    -- Loan 2: Jane Smith gets a loan of 1000 with 6 installments and 0.15 interest rate
    (2, 1000.00, 6, '2025-02-01', FALSE),   -- Jane Smith's loan

    -- Loan 3: Alice Johnson gets a loan of 5000 with 24 installments and 0.25 interest rate
    (3, 5000.00, 24, '2025-03-01', FALSE),  -- Alice Johnson's loan

    -- Loan 4: Bob Williams gets a loan of 3000 with 9 installments and 0.18 interest rate
    (4, 3000.00, 9, '2025-04-01', FALSE);   -- Bob Williams' loan

-- Insert loan installments for Loan 1 (John Doe's loan)
-- Installments are due on the 1st of each month starting from February
INSERT INTO LoanInstallment (loanId, amount, paidAmount, dueDate, paymentDate, isPaid)
VALUES
    (1, 200.00, 0.00, '2025-02-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-03-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-04-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-05-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-06-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-07-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-08-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-09-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-10-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-11-01', NULL, FALSE),
    (1, 200.00, 0.00, '2025-12-01', NULL, FALSE),
    (1, 200.00, 0.00, '2026-01-01', NULL, FALSE);

-- Insert loan installments for Loan 2 (Jane Smith's loan)
-- Installments are due on the 1st of each month starting from March
INSERT INTO LoanInstallment (loanId, amount, paidAmount, dueDate, paymentDate, isPaid)
VALUES
    (2, 166.67, 0.00, '2025-03-01', NULL, FALSE),
    (2, 166.67, 0.00, '2025-04-01', NULL, FALSE),
    (2, 166.67, 0.00, '2025-05-01', NULL, FALSE),
    (2, 166.67, 0.00, '2025-06-01', NULL, FALSE),
    (2, 166.67, 0.00, '2025-07-01', NULL, FALSE),
    (2, 166.67, 0.00, '2025-08-01', NULL, FALSE);

-- Insert loan installments for Loan 3 (Alice Johnson's loan)
-- Installments are due on the 1st of each month starting from April
INSERT INTO LoanInstallment (loanId, amount, paidAmount, dueDate, paymentDate, isPaid)
VALUES
    (3, 208.33, 0.00, '2025-04-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-05-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-06-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-07-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-08-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-09-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-10-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-11-01', NULL, FALSE),
    (3, 208.33, 0.00, '2025-12-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-01-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-02-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-03-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-04-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-05-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-06-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-07-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-08-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-09-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-10-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-11-01', NULL, FALSE),
    (3, 208.33, 0.00, '2026-12-01', NULL, FALSE);

-- Insert loan installments for Loan 4 (Bob Williams' loan)
-- Installments are due on the 1st of each month starting from May
INSERT INTO LoanInstallment (loanId, amount, paidAmount, dueDate, paymentDate, isPaid)
VALUES
    (4, 333.33, 0.00, '2025-05-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-06-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-07-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-08-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-09-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-10-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-11-01', NULL, FALSE),
    (4, 333.33, 0.00, '2025-12-01', NULL, FALSE),
    (4, 333.33, 0.00, '2026-01-01', NULL, FALSE);
