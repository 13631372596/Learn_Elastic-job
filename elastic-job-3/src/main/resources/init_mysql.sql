DROP TABLE IF EXISTS tb_order;
CREATE TABLE tb_order(
 id int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
 amount NUMERIC(11) NOT NULL,
 order_name VARCHAR(10) NOT NULL,
 order_status INT(1) NOT NULL,
 create_time datetime NOT NULL,
 update_time datetime NOT NULL
)