DROP TABLE IF EXISTS SalesOrder;
CREATE TABLE SalesOrder (
    System_ID VARCHAR(50) PRIMARY KEY,
    Customer_ID VARCHAR(50),
    Order_Date DATE,
    Total_Amount DECIMAL(10, 2),
    Order_Status ENUM('Pending', 'Shipped', 'Delivered', 'Canceled'),
    Item_ID VARCHAR(50),
    Product_Name VARCHAR(255),
    Quantity INT,
    Price DECIMAL(10, 2)
);

INSERT INTO SalesOrder (System_ID, Customer_ID, Order_Date, Total_Amount, Order_Status, Item_ID, Product_Name, Quantity, Price) VALUES
    ('722f6397-6b66-4ee2-a17c-6b857f6b6a0f', 'f9f27b13-e7ef-4057-b2ce-dd9a181612b5', '2022-02-01', 250.00, 'Shipped', '4d67d8c5-40e9-435b-96f0-5e48403ff5f7', 'Product A', 2, 50.00),
    ('da4b35d3-825a-47e5-9072-7b29e4b15ab4', 'adf13a27-c4bd-415e-aa34-63173457a504', '2022-02-02', 150.00, 'Delivered', '1a7f967c-69ea-4a6e-8e7b-4c1daaa3d28d', 'Product B', 1, 100.00),
    ('1ea4c4b6-891f-4e98-994f-b5342a493e6f', 'c84712d4-2e0f-4f7a-9f44-c41908c49185', '2022-02-03', 100.00, 'Canceled', 'c8d7ec60-1e11-45f5-860e-86e152ae97c1', 'Product C', 3, 30.00),
    ('d5b1c4c7-2b74-4522-8edc-bd07f64d5e5d', 'b51df661-f5d6-4c08-b400-8ab09de7b40b', '2022-02-04', 300.00, 'Pending', 'b6a07f0d-2743-4b4d-ae13-7083cb005433', 'Product D', 1, 50.00),
    ('a92480a5-4ec4-4fe9-bd92-8a9e0fc20022', '0d2e618e-9a5c-4943-af42-cd7aa07b793a', '2022-02-05', 50.00, 'Shipped', '6c4fe836-7812-4d16-9809-e36d10dd5edc', 'Product E', 2, 75.00),
    ('8472f488-eb01-4d2e-90f3-b981d7ea2e9e', '3165bc21-62b6-41d3-87ce-375b219958fc', '2022-02-06', 200.00, 'Delivered', 'b8c6d3a6-8f9f-45c9-9792-ff8a7fbab8f5', 'Product F', 1, 25.00),
    ('3a0b9cf2-8982-4489-857e-166b7f88b02b', '5979cbcf-c134-4590-8b69-9cf780f6c919', '2022-02-07', 100.00, 'Shipped', '2437047d-8a06-4f05-bc65-d8491f84ec14', 'Product G', 2, 40.00),
    ('f171c8b4-65f2-4f26-bcf5-2eac94477a1b', '1cce9e0a-6474-41c3-8e11-d40002c60283', '2022-02-08', 150.00, 'Delivered', '7ff587c6-21e1-47cd-90c7-119a0d9e9672', 'Product H', 1, 15.00),
    ('1397c6e7-4c76-458c-80fe-e7f5b988b738', '75e88a8c-9bed-4667-a293-4c3406ea7afd', '2022-02-13', 250.00, 'Pending', '6f6d9189-2c2c-44a1-ae87-3b1266f3b0b1', 'Product I', 3, 30.00),
    ('5f90e864-7c1c-4082-884e-4f9d9335246a', 'c50b71e1-77b9-4d38-918c-c3023b199968', '2022-02-14', 150.00, 'Shipped', '8279d8ea-e61d-4763-8793-cbd9f6b55e5d', 'Product J', 2, 20.00),
    ('3c2a6a96-24d1-4de5-b970-5eefb0f63eae', 'ea4dd2a3-9f74-4c83-a9d8-3aa0b8898ddf', '2022-02-15', 100.00, 'Delivered', '4d9c3ef3-889e-4c2e-823d-17f83a39f4a9', 'Product K', 1, 10.00);