-- 포인트 타입
INSERT INTO point_type (point_type, point_type_name, rate, is_cash) VALUES ('PT_0001', '구매 적립', 1.0, 'N');
INSERT INTO point_type (point_type, point_type_name, rate, expire_period, is_cash) VALUES ('PT_0009', '오늘만 사용', 100.0, 0, 'N');
INSERT INTO point_type (point_type, point_type_name, rate, expire_period, is_cash) VALUES ('PT_0002', '캐시 포인트', 100, 1825, 'Y');
INSERT INTO point_type (point_type, point_type_name, rate, expire_period, is_cash, use_yn) VALUES ('PT_0003', '포인트 구매', 100, 1825, 'Y', 'N');

-- 주문
INSERT INTO order_info (order_no, member_id, payment_at, `status`, order_date) VALUES (1000008, 10003, 19900, 'P2', '2023-01-14');
INSERT INTO order_info (order_no, member_id, payment_at, `status`, order_date) VALUES (1000009, 10003, 16300, 'P1', '2023-02-03');
INSERT INTO order_info (order_no, member_id, payment_at, `status`, order_date) VALUES (1000010, 10003, 0, 'P1', '2023-02-21');
INSERT INTO order_info (order_no, member_id, payment_at, `status`, order_date) VALUES (1000011, 10003, 21000, 'P1', '2023-03-07');
INSERT INTO order_info (order_no, member_id, payment_at, `status`, order_date) VALUES (1000012, 10003, 74500, 'P9', '2023-03-11');

-- 포인트 적립
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 2684, 0, 0, 'N', '2021-01-13', '2022-01-13');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 364, 0, 48, 'N', '2021-02-05', '2022-02-05');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 199, 0, 0, 'N', '2022-05-14', '2023-05-14');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 163, 0, 0, 'N', '2022-05-15', '2023-05-15');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 1731, 0, 0, 'N', '2022-06-17', '2023-06-17');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0009', '오늘만 사용', 3000, 0, 0, 'N', '2022-12-31', '2022-12-31');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0002', '캐시 포인트', 10000, 0, 0, 'Y', '2022-12-31', '2027-12-31');

INSERT INTO `point` (origin_point_no, member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (7, 10001, 'PT_0002', '캐시 포인트', 0, 10000, 0, 'Y', '2022-12-31', '2027-12-31');
INSERT INTO `point` (origin_point_no, member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (5, 10001, 'PT_0001', '구매 적립', 0, 1731, 0, 'N', '2022-06-17', '2023-06-17');
INSERT INTO `point` (origin_point_no, member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (4, 10001, 'PT_0001', '구매 적립', 0, 163, 0, 'N', '2022-05-15', '2023-05-15');
INSERT INTO `point` (origin_point_no, member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (3, 10001, 'PT_0001', '구매 적립', 0, 199, 0, 'N', '2022-05-14', '2023-05-14');
INSERT INTO `point` (origin_point_no, member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (6, 10001, 'PT_0009', '오늘만 사용', 0, 1000, 0, 'N', '2022-12-31', '2022-12-31');

INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 210, 210, 0, 'N', '2023-02-03', '2024-02-03');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 3561, 3561, 0, 'N', '2023-03-02', '2024-03-02');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10001, 'PT_0001', '구매 적립', 174, 174, 0, 'N', '2023-03-17', '2024-03-17');

INSERT INTO `point` (member_id, point_type, `description`, order_no, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10002, 'PT_0001', '구매 적립', 1000008, 199, 199, 0, 'N', '2022-01-13', '2023-01-13');
INSERT INTO `point` (member_id, point_type, `description`, accumulate_at, remain_at, expire_at, is_cash, save_date, expire_date) VALUES (10002, 'PT_0009', '오늘만 사용', 3000, 0, 0, 'N', '2023-02-21', '2023-02-21');

-- 포인트 사용
INSERT INTO point_use (member_id, order_no, free_point_at, cash_point_at, cancel_free_point_at, cancel_cash_point_at, use_date) VALUES (10001, 0, 3000, 0, 0, 0, '2021-12-24');
INSERT INTO point_use (member_id, order_no, free_point_at, cash_point_at, cancel_free_point_at, cancel_cash_point_at, use_date) VALUES (10001, 0, 5000, 10000, 3000, 10000, '2022-12-31');
INSERT INTO point_use (member_id, order_no, free_point_at, cash_point_at, cancel_free_point_at, cancel_cash_point_at, use_date) VALUES (10002, 1000010, 3000, 0, 0, 0, '2023-02-21');

INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (1, 1, 2684, 0);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (1, 2, 316, 0);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (2, 6, 3000, 1000);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (2, 3, 199, 199);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (2, 4, 163, 163);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (2, 5, 1731, 1731);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (2, 7, 10000, 10000);
INSERT INTO point_use_detail (point_use_no, point_no, point_at, cancel_point_at) VALUES (3, 17, 3000, 0);

-- 포인트 이력
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 1, 0, 2684, '2021-01-13 20:15:48');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 2, 0, 364, '2021-02-05 13:11:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 1, 1, 2684, '2021-12-24 10:01:59');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 2, 1, 316, '2021-12-24 10:01:59');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'X', 2, 0, 48, '2022-02-06 00:00:00');

INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 3, 0, 199, '2022-05-14 08:07:12');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 4, 0, 163, '2022-05-15 13:11:59');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 5, 0, 1731, '2022-06-17 21:07:01');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 6, 0, 3000, '2022-12-31 09:00:00');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 7, 0, 10000, '2022-12-31 11:59:01');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 5, 2, 3000, '2022-12-31 12:48:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 3, 2, 199, '2022-12-31 12:48:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 4, 2, 163, '2022-12-31 12:48:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 6, 2, 1731, '2022-12-31 12:48:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'U', 7, 2, 10000, '2022-12-31 12:48:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'C', 8, 2, 10000, '2023-01-03 18:38:16');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'C', 9, 2, 1731, '2023-01-03 18:38:16');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'C', 10, 2, 163, '2023-01-03 18:38:16');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'C', 11, 2, 199, '2023-01-03 18:38:16');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'C', 12, 2, 1000, '2023-01-03 18:38:16');

INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 13, 0, 210, '2023-02-03 19:21:10');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 14, 0, 3561, '2023-03-02 23:02:07');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10001, 'S', 15, 0, 174, '2023-03-17 18:59:07');

INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10002, 'S', 16, 0, 199, '2022-01-13 18:11:59');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10002, 'S', 17, 0, 3000, '2023-02-21 09:00:00');
INSERT INTO point_history (member_id, point_status, point_no, point_use_no, point_at, created_dt) VALUES (10002, 'U', 17, 3, 3000, '2023-02-21 17:11:24');

-- 포인트 잔액
INSERT INTO point_balance (member_id, free_point, cash_point) VALUES (10001, 7038, 10000);
INSERT INTO point_balance (member_id, free_point, cash_point) VALUES (10002, 199, 0);
