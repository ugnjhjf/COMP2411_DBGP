
-- CREATE TABLE REVIEW (
--     ReviewID NUMBER(4) PRIMARY KEY,
--     ProductID NUMBER(4),
--     UserID NUMBER(4),
--     UserComment VARCHAR(50),
--     CONSTRAINT FK_REVIEW_PRODUCT FOREIGN KEY (ProductID) REFERENCES PRODUCT(ProductID),
--     CONSTRAINT FK_REVIEW_CUSTOMER FOREIGN KEY (UserID) REFERENCES CUSTOMER(UserID)
-- );