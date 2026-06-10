INSERT INTO investor (id, first_name, last_name, email, date_of_birth, age)
VALUES (1, 'Sithembiso', 'Mndebele', 'sthembiso@email.com', '1950-05-15', 75);

INSERT INTO investment_product (id, name, type, balance, investor_id)
VALUES (1, 'Growth Fund A', 'RETIREMENT', 500000.00, 1),
       (2, 'Income Fund B', 'SAVINGS', 120000.00, 1);
