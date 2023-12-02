
CREATE TABLE CUSTOMER(
    UserID NUMBER(4) PRIMARY KEY,
    Username varchar(20) NOT NULL,
    Password varchar(20) NOT NULL ,
    Tel varchar(8) NOT NULL ,
    Shipping_address varchar(100) NOT NULL
);
