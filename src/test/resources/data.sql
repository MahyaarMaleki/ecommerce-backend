-- passwords are in the format: Password<UserLetter>123. unless specified otherwise.
-- encrypted using https://www.javainuse.com/onlineBcrypt with 10 rounds

INSERT INTO local_user (username, password, email, first_name, last_name, is_email_verified)
    VALUES ('UserA', '$2a$10$3bZCS4hX0IbNQ3oOc5uEjORvEiawAwCLXiyOQFagYVbrg79sqKe9i', 'UserA@junit.com', 'UserA-FirstName', 'UserA-LastName', true)
    , ('UserB', '$2a$10$LEck2loBDa97dUeMweD5aumWpDLLt8R3kR8.z62UY/mzmqizejUva', 'UserB@junit.com', 'UserB-FirstName', 'UserB-LastName', false);
