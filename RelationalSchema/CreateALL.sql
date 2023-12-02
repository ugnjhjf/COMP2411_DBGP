CREATE TABLE SELLER
(
    SellerID NUMBER(4) PRIMARY KEY,
    SellerName VARCHAR(30)
);
INSERT INTO SELLER (SellerID, SellerName) VALUES (3001, 'Amiami');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3002, 'Dasiny');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3003, 'WAWEI');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3004, 'BokidnaG retailer');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3005, 'Kurumi');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3006, 'BlueMi');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3007, 'yao yao ling xian');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3008, 'UU accelerator');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3009, 'Nekoparo .Inc');

CREATE TABLE CUSTOMER(
     UserID NUMBER(4) PRIMARY KEY,
     Username varchar(20) NOT NULL,
     Password varchar(20) NOT NULL ,
     Tel varchar(8) NOT NULL ,
     Shipping_address varchar(100) NOT NULL
);
INSERT INTO CUSTOMER (UserID, Username, Password, Tel, Shipping_address) VALUES (1001, 'Echidna', '123456', '46002000', 'Beijing');
INSERT INTO CUSTOMER (UserID, Username, Password, Tel, Shipping_address) VALUES (1002,'HatsureMiku','mikuyyds','114514','Japan');
INSERT INTO CUSTOMER (UserID, Username, Password, Tel, Shipping_address) VALUES (1003,'Needy Girl','wyyyz','7355608','German');
INSERT INTO CUSTOMER (UserID, Username, Password, Tel, Shipping_address) VALUES (1004,'Rokidna','121212','23093821','Akihabara');
INSERT INTO CUSTOMER (UserID, Username, Password, Tel, Shipping_address) VALUES (1005,'ugnjhjf','139101','51000001','Tsim Sha Tsui');

CREATE TABLE PRODUCT
(
    ProductID    NUMBER(4) PRIMARY KEY,
    ProductName  VARCHAR(50),
    Price Number(4),
    Specification  VARCHAR(50),
    Description  VARCHAR(50),
    SellerID NUMBER(4),
    Quantity NUMBER(4) DEFAULT 500,
    CONSTRAINT FK_PRODUCT_SELLER
        FOREIGN KEY (SellerID)
            REFERENCES SELLER(SellerID)
);
INSERT INTO PRODUCT (ProductID, ProductName, Price, Specification, Description, SellerID, Quantity) VALUES (0001,'RPG 7',1500,'red','a powerful weapon',3004,10);
INSERT INTO PRODUCT (ProductID, ProductName, Price, Specification, Description, SellerID, Quantity) VALUES (0002,'Happy Potion',66,'colorful','let you dive into tRAnCe',3001,90);
INSERT INTO PRODUCT (ProductID, ProductName, Price, Specification, Description, SellerID, Quantity) VALUES (0003,'pencil',10,'blue','made in china',3002,500);
INSERT INTO PRODUCT (ProductID, ProductName, Price, Specification, Description, SellerID, Quantity) VALUES (0004,'Vanilla',9999,'La Soleil','',3009,500);
CREATE TABLE CART
(
    UserID  NUMBER(4),
    ProductID  NUMBER(4),
    Quantity NUMBER(4),
    CONSTRAINT FK_CHOOSE_USER FOREIGN KEY (UserID) REFERENCES Customer(UserID),
    CONSTRAINT FK_CHOOSE_PRODUCT FOREIGN KEY (ProductID) REFERENCES Product(ProductID),
    CONSTRAINT PK_CART_USER_PRODUCT PRIMARY KEY (UserID,ProductID)
);
CREATE TABLE PARCEL
(
    ParcelID   NUMBER(4) PRIMARY KEY,
    ProductID  NUMBER(4),
    Quantity NUMBER(4),
    UserID NUMBER(4) NOT NULL,
    Shipping_address VARCHAR(100),
    Status VARCHAR(20) default 'Preparing',
    CONSTRAINT FK_PARCEL_PRODUCT FOREIGN KEY (ProductID) REFERENCES PRODUCT(ProductID),
    CONSTRAINT FK_PARCEL_CUSTOMER FOREIGN KEY (UserID) REFERENCES CUSTOMER(UserID)
);
