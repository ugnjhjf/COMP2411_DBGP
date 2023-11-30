INSERT INTO SELLER (SellerID, SellerName) VALUES (3001, 'Seller 1');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3002, 'Seller 2');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3003, 'Seller 3');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3004, 'Seller 4');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3005, 'Seller 5');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3006, 'Seller 6');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3007, 'Seller 7');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3008, 'Seller 8');
INSERT INTO SELLER (SellerID, SellerName) VALUES (3009, 'Seller 9');
select * from SELLER;

commit;

INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1001,'Jones1','pass');
INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1002,'Mary','pass');
INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1003,'David','pass');
INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1004,'MQax','pass');
INSERT INTO CUSTOMER (UserID, Username, Password) VALUES (1005,'ray','pass');

select * from CUSTOMER;



INSERT INTO PRODUCT (ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (0001,'product A',100,'speici','Desc',3000);
INSERT INTO PRODUCT (ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (0002,'product B',100,'speici','Desc',3001);
INSERT INTO PRODUCT (ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (0003,'product C',100,'speici','Desc',3001);
INSERT INTO PRODUCT (ProductID,ProductName,Price,Specification,Description,SellerID) VALUES (0004,'product D',100,'speici','Desc',3001);
select * from PRODUCT;

commit;

INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID, Shipping_address) VALUES (5001,0001,1,1001,'Xiabeize');
INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID, Shipping_address) VALUES (5002,0002,1,1002,'PolyU');
INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID, Shipping_address) VALUES (5003,0003,3,1003, 'Address 3');
INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID, Shipping_address) VALUES (5006,0004,2,1004, 'Address 4');
INSERT INTO PARCEL (ParcelID, ProductID, Quantity, UserID, Shipping_address) VALUES (5005,0005,1,1005, 'Address 5');
select * from PARCEL;

commit;

-- UserID  NUMBER(4) PRIMARY KE Y,
--     ProductID  NUMBER(4),
--     Quantity NUMBER(4),
INSERT INTO CART (UserID, ProductID, Quantity) VALUES (1001,0001,1);
INSERT INTO CART (UserID, ProductID, Quantity) VALUES (1002,0002,1);
INSERT INTO CART (UserID, ProductID, Quantity) VALUES (1003,0003,3);
INSERT INTO CART (UserID, ProductID, Quantity) VALUES (1004,0004,2);
select * from CART;
commit;

-- INSERT INTO REVIEW (ReviewID,ProductID,UserID,UserComment) VALUES (7001,0001,1001,'Strongly disagree');
-- INSERT INTO REVIEW (ReviewID,ProductID,UserID,UserComment) VALUES (7002,0002,1002,'OK');
-- INSERT INTO REVIEW (ReviewID,ProductID,UserID,UserComment) VALUES (7003,0003,1003,'I dont believe you');
-- SELECT * from REVIEW;
-- commit;

INSERT INTO CHECKOUT (UserID,ProductID,ParcelID) VALUES (1001,0001,5001);
INSERT INTO CHECKOUT (UserID,ProductID,ParcelID) VALUES (1002,0002,5002);
SELECT * from CHECKOUT;

--    ReviewID   NUMBER(4) PRIMARY KEY,
--                   ProductID  NUMBER(4),