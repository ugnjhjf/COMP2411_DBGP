CREATE TABLE PRODUCT
(
    ProductID    NUMBER(4) PRIMARY KEY,
    ProductName  VARCHAR(50),
    Price Number(4),
    Specification  VARCHAR(50),
    Description  VARCHAR(50),
    SellerID NUMBER(4),
    CONSTRAINT FK_PRODUCT_SELLER
    FOREIGN KEY (SellerID)
    REFERENCES SELLER(SellerID)
);
