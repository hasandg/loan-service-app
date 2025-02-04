INSERT INTO Customer (id, name, surname, credit_limit, used_credit_limit, version) VALUES
    (1, 'John', 'Doe', 10000.00, 2000.00, 0),
    (2, 'Jane', 'Smith', 5000.00, 1500.00, 0),
    (3, 'Alice', 'Johnson', 8000.00, 1000.00, 0),
    (4, 'Bob', 'Williams', 12000.00, 4000.00, 0);

INSERT INTO Loan (customer_id, loan_amount, interest_rate, number_of_installments, create_date, is_paid, version) VALUES
    (1, 2000.00, 0.2, 12, '2025-01-01', FALSE, 0),  -- John Doe's loan
    (2, 1000.00, 0.15, 6, '2025-02-01', FALSE, 0),  -- Jane Smith's loan
    (3, 5000.00, 0.25, 24, '2025-03-01', FALSE, 0), -- Alice Johnson's loan
    (4, 3000.00, 0.18, 9, '2025-04-01', FALSE, 0);  -- Bob Williams' loan

INSERT INTO loan_installment (loan_id, amount, paid_amount, due_date, payment_date, is_paid, version) VALUES
    (1, 200.00, 0.00, '2025-02-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-03-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-04-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-05-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-06-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-07-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-08-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-09-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-10-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-11-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2025-12-01', NULL, FALSE, 0),
    (1, 200.00, 0.00, '2026-01-01', NULL, FALSE, 0);

INSERT INTO loan_installment (loan_id, amount, paid_amount, due_date, payment_date, is_paid, version) VALUES
    (2, 166.67, 0.00, '2025-03-01', NULL, FALSE, 0),
    (2, 166.67, 0.00, '2025-04-01', NULL, FALSE, 0),
    (2, 166.67, 0.00, '2025-05-01', NULL, FALSE, 0),
    (2, 166.67, 0.00, '2025-06-01', NULL, FALSE, 0),
    (2, 166.67, 0.00, '2025-07-01', NULL, FALSE, 0),
    (2, 166.67, 0.00, '2025-08-01', NULL, FALSE, 0);

INSERT INTO loan_installment (loan_id, amount, paid_amount, due_date, payment_date, is_paid, version) VALUES
    (3, 208.33, 0.00, '2025-04-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-05-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-06-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-07-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-08-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-09-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-10-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-11-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2025-12-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-01-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-02-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-03-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-04-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-05-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-06-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-07-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-08-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-09-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-10-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-11-01', NULL, FALSE, 0),
    (3, 208.33, 0.00, '2026-12-01', NULL, FALSE, 0);

INSERT INTO loan_installment (loan_id, amount, paid_amount, due_date, payment_date, is_paid, version) VALUES
    (4, 333.33, 0.00, '2025-05-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-06-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-07-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-08-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-09-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-10-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-11-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2025-12-01', NULL, FALSE, 0),
    (4, 333.33, 0.00, '2026-01-01', NULL, FALSE, 0);