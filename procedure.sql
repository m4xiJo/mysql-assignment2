DELIMITER //
DROP PROCEDURE IF EXISTS AddToCart;
CREATE PROCEDURE AddToCart(IN customer_id_param INT, order_id_param INT, product_id_param INT)
BEGIN
	DECLARE check_order_id int;
    DECLARE check_product_id int;
	SELECT orders.order_id INTO check_order_id FROM orders WHERE order_id = order_id_param;
	SELECT products.product_id INTO check_product_id FROM products WHERE product_id = product_id_param;
    
    IF (check_order_id IS NULL OR order_id_param IS NULL) THEN
		INSERT INTO Orders (customer_id) VALUES (customer_id_param);
        INSERT INTO ProductsPerOrders (product_id, quantity, order_id) VALUES (product_id_param, 1, LAST_INSERT_ID());
        UPDATE Products SET instock = instock - 1 WHERE product_id = product_id_param;
	ELSEIF (check_order_id = order_id_param AND check_product_id != product_id_param) THEN
		INSERT INTO ProductsPerOrders (product_id, quantity, order_id) VALUES (product_id_param, 1, order_id_param);
        UPDATE Products SET instock = instock - 1 WHERE product_id = product_id_param;
    ELSEIF (check_order_id = order_id_param AND check_product_id = product_id_param) THEN
		UPDATE ProductsPerOrders SET quantity = quantity + 1 WHERE order_id = order_id_pramam AND product_id = product_id_param;
        UPDATE Products SET instock = instock - 1 WHERE product_id = product_id_param;
	END IF;
END //

DECLARE EXIT HANDLER FOR 1062 
BEGIN
	ROLLBACK;
	RESIGNAL SET MESSAGE_TEXT = "Unique onstraint broken";
END;

-- CALL AddToCart (1, null, 1);