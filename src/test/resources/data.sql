-- passwords are in the format: Password<UserUppercaseLetter>123. unless specified otherwise.
-- encrypted using https://www.javainuse.com/onlineBcrypt with 10 rounds

INSERT INTO local_user (username, password, email, first_name, last_name, is_email_verified)
    VALUES ('UserA', '$2a$10$3bZCS4hX0IbNQ3oOc5uEjORvEiawAwCLXiyOQFagYVbrg79sqKe9i', 'UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', true)
    , ('UserB', '$2a$10$LEck2loBDa97dUeMweD5aumWpDLLt8R3kR8.z62UY/mzmqizejUva', 'UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', false)
    , ('UserC', '$2a$10$P.lcdo21atl1mIJYORLUYuToUvMwHy46Gf6dzdnfXo44kjikIVIr6', 'UserC@junit.com', 'UserC-FirstName', 'UserC-LastName', false);

INSERT INTO address(address_line_1, city, country, user_id)
    VALUES ('123 Tester Hill', 'Testerton', 'England', 1)
     , ('312 Spring Boot', 'Hibernate', 'England', 3);

INSERT INTO product (name, short_description, long_description, price)
    VALUES ('Product #1', 'Product one short description.', 'This is a very long description of product #1.', 5.50)
     , ('Product #2', 'Product two short description.', 'This is a very long description of product #2.', 10.56)
     , ('Product #3', 'Product three short description.', 'This is a very long description of product #3.', 2.74)
     , ('Product #4', 'Product four short description.', 'This is a very long description of product #4.', 15.69)
     , ('Product #5', 'Product five short description.', 'This is a very long description of product #5.', 42.59);

INSERT INTO inventory (product_id, quantity)
    VALUES (1, 5)
     , (2, 8)
     , (3, 12)
     , (4, 73)
     , (5, 2);

INSERT INTO web_order (address_id, user_id)
    VALUES (1, 1)
     , (1, 1)
     , (1, 1)
     , (2, 3)
     , (2, 3);

INSERT INTO web_order_quantities (web_order_id, product_id, quantity)
    VALUES (1, 1, 5)
     , (1, 2, 5)
     , (2, 3, 5)
     , (2, 2, 5)
     , (2, 5, 5)
     , (3, 3, 5)
     , (4, 4, 5)
     , (4, 2, 5)
     , (5, 3, 5)
     , (5, 1, 5);