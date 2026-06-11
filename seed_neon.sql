-- ============================================================
-- SEED DATA - Hotel Booking System (PostgreSQL / Neon.tech)
-- Password all accounts: Admin@123
-- Thứ tự insert đúng theo foreign key dependency
-- ============================================================

-- ============================================================
-- 1. USERS (thêm vào 17 user mới, đã có id 1,2,3)
-- ============================================================
INSERT INTO users (username,password_hash,provider,email,email_verified,phone,full_name,gender,date_of_birth,role,status,created_at,updated_at) VALUES
('customer03','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer03@gmail.com',true,'0934567890','Le Thi C','FEMALE','1997-03-22','CUSTOMER',1,NOW(),NOW()),
('customer04','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer04@gmail.com',true,'0945678901','Pham Van D','MALE','1992-07-14','CUSTOMER',1,NOW(),NOW()),
('customer05','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer05@gmail.com',true,'0956789012','Hoang Thi E','FEMALE','2000-11-30','CUSTOMER',1,NOW(),NOW()),
('customer06','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer06@gmail.com',true,'0967890123','Nguyen Van F','MALE','1988-05-05','CUSTOMER',1,NOW(),NOW()),
('customer07','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer07@gmail.com',true,'0978901234','Tran Thi G','FEMALE','1995-09-18','CUSTOMER',1,NOW(),NOW()),
('customer08','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer08@gmail.com',true,'0989012345','Bui Van H','MALE','1993-12-25','CUSTOMER',1,NOW(),NOW()),
('customer09','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer09@gmail.com',true,'0990123456','Vu Thi I','FEMALE','1999-04-10','CUSTOMER',1,NOW(),NOW()),
('customer10','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer10@gmail.com',true,'0901234568','Do Van J','MALE','1991-08-28','CUSTOMER',1,NOW(),NOW()),
('cleaner01','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','cleaner01@hotel.com',true,'0902345678','Nguyen Thi Lan','FEMALE','1990-02-14','ADMIN',1,NOW(),NOW()),
('cleaner02','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','cleaner02@hotel.com',true,'0903456789','Tran Van Minh','MALE','1988-06-20','ADMIN',1,NOW(),NOW()),
('receptionist01','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','receptionist01@hotel.com',true,'0904567890','Le Thi Hoa','FEMALE','1996-09-15','ADMIN',1,NOW(),NOW()),
('receptionist02','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','receptionist02@hotel.com',true,'0905678901','Pham Van Nam','MALE','1994-03-08','ADMIN',1,NOW(),NOW()),
('customer11','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer11@gmail.com',true,'0906789012','Hoang Van K','MALE','1987-11-11','CUSTOMER',1,NOW(),NOW()),
('customer12','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer12@gmail.com',true,'0907890123','Mai Thi L','FEMALE','2001-01-20','CUSTOMER',1,NOW(),NOW()),
('customer13','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer13@gmail.com',true,'0908901234','Dinh Van M','MALE','1996-07-07','CUSTOMER',1,NOW(),NOW()),
('admin02','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','admin02@hotel.com',true,'0909012345','Nguyen Thanh Long','MALE','1985-05-15','ADMIN',1,NOW(),NOW()),
('customer14','$2a$10$DwKH/hTt7bxVBszi/TE3TuqTCxZSJ/Vjnbqo2BMNuBcZjn368.rPy','LOCAL','customer14@gmail.com',true,'0910123456','Vo Thi N','FEMALE','1998-12-12','CUSTOMER',1,NOW(),NOW());

-- ============================================================
-- 2. HOTEL (1 khách sạn duy nhất)
-- ============================================================
INSERT INTO hotel (name, description, star_rating, address, phone_number, email, map_url, created_at, updated_at) VALUES
('Khach san Binh Minh', 'Khach san 4 sao tai trung tam Da Nang, noi bat voi kien truc hien dai, dich vu chuyen nghiep va vi tri dat dep ngay gan bien My Khe.', 4, '123 Duong Tran Hung Dao, Hai Chau, Da Nang', '02363123456', 'contact@binhminhhotel.com', 'https://maps.google.com/?q=Da+Nang', NOW(), NOW());

-- ============================================================
-- 3. HOTEL IMAGES
-- ============================================================
INSERT INTO hotelimages (hotel_id, image_url, is_primary, caption) VALUES
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1774416942/hotel/room-types/pmdhqiqd5pvwuv7wc7pm.jpg',true,'Mat tien khach san'),
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341218/Screenshot_2026-05-21_122631_j586vn.png',false,'Khu vuc ho boi'),
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341303/Screenshot_2026-05-21_122803_yttutl.png',false,'Khu vuc nha hang'),
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341346/Screenshot_2026-05-21_122850_mklaae.png',false,'Sanh tiep tan'),
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341388/Screenshot_2026-05-21_122932_egqu3o.png',false,'Khu vuc bar lounge');

-- ============================================================
-- 4. HOTEL AMENITIES
-- ============================================================
INSERT INTO hotelamenities (hotel_id, name, description) VALUES
(1,'Wifi mien phi','Wifi toc do cao toan khuon vien'),
(1,'Ho boi vo cuc','Ho boi ngoai troi tang thuong view bien'),
(1,'Phong Gym','Phong tap day du may moc hien dai 24/7'),
(1,'Spa & Massage','Dich vu spa cao cap thu gian toan than'),
(1,'Nha hang buffet','Buffet sang quoc te phong phu'),
(1,'Bai do xe mien phi','Bai do xe rong rai co camera giam sat'),
(1,'Ho tro dua don san bay','Xe dua don san bay Lien Chieu'),
(1,'Phong hop hoi nghi','Phong hop 200 cho ngoi day du trang thiet bi');

-- ============================================================
-- 5. ROOM TYPES
-- ============================================================
INSERT INTO roomtypes (name, description, status, price_per_day, price_per_hour, target_daily_percentage, target_hourly_percentage, max_adults, max_children, bed_count, bed_type, room_size_m2, created_at, updated_at) VALUES
('STANDARD','Phong tieu chuan tien nghi co ban, phu hop cho cap doi hoac khach di cong tac ngan ngay.',1,400000.00,120000.00,50,50,2,1,1,'SINGLE',20.5,NOW(),NOW()),
('DELUXE','Phong sang trong khong gian rong rai, noi that cao cap, co cua so lon huong nhin ra pho.',1,750000.00,180000.00,70,30,2,1,1,'DOUBLE',32.0,NOW(),NOW()),
('VIP','Phong thuong hang dang cap, co phong khach rieng biet, bon tam suc jacuzzi va huong bien toan canh.',1,1500000.00,300000.00,100,0,4,2,2,'KING SIZE',60.0,NOW(),NOW());

-- ============================================================
-- 6. ROOM TYPE IMAGES
-- ============================================================
INSERT INTO roomtypeimages (room_type_id, image_url, is_primary, caption, created_at) VALUES
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341218/Screenshot_2026-05-21_122631_j586vn.png',true,'Khong gian tong the phong Standard',NOW()),
(1,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341303/Screenshot_2026-05-21_122803_yttutl.png',false,'Phong tam phong Standard',NOW()),
(2,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341346/Screenshot_2026-05-21_122850_mklaae.png',true,'Phong Deluxe huong pho ban ngay',NOW()),
(2,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341388/Screenshot_2026-05-21_122932_egqu3o.png',false,'Phong tam kinh hien dai phong Deluxe',NOW()),
(2,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341431/Screenshot_2026-05-21_123015_gba4ar.png',false,'Goc lam viec phong Deluxe',NOW()),
(3,'https://res.cloudinary.com/do8uakd0l/image/upload/v1774416942/hotel/room-types/pmdhqiqd5pvwuv7wc7pm.jpg',true,'Phong khach sang trong phong VIP',NOW()),
(3,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341218/Screenshot_2026-05-21_122631_j586vn.png',false,'Chi tiet giuong ngu King Size phong VIP',NOW()),
(3,'https://res.cloudinary.com/do8uakd0l/image/upload/v1779341303/Screenshot_2026-05-21_122803_yttutl.png',false,'Bon tam suc Jacuzzi phong VIP',NOW());

-- ============================================================
-- 7. BASE ITEMS (vật dụng phòng)
-- ============================================================
INSERT INTO baseitems (item_name, base_unit_price, description, created_at) VALUES
('LY THUY TINH',50000.00,'Ly thuy tinh cao cap',NOW()),
('GOI NHUNG',200000.00,'Goi nhung chat luong cao',NOW()),
('TIVI 4K',8000000.00,'Tivi Samsung 4K 55 inch',NOW()),
('KHAN TAM',150000.00,'Khan bong cao cap',NOW()),
('MAY SAY TOC',500000.00,'May say toc chuyen nghiep',NOW()),
('TU LANH MINI',2000000.00,'Tu lanh mini trong phong',NOW()),
('DIEU HOA',5000000.00,'Dieu hoa 2 chieu inverter',NOW()),
('BINH NUOC SIEU TOC',300000.00,'Binh nuoc sieu toc cao cap',NOW());

-- ============================================================
-- 8. ROOM TYPE ITEMS
-- ============================================================
INSERT INTO roomtypeitems (room_type_id, item_id, quantity) VALUES
(1,1,2),(1,2,2),(1,4,4),(1,8,1),
(2,1,2),(2,2,2),(2,3,1),(2,4,4),(2,5,1),(2,6,1),(2,8,1),
(3,1,4),(3,2,4),(3,3,1),(3,4,8),(3,5,1),(3,6,1),(3,7,1),(3,8,2);

-- ============================================================
-- 9. ROOMS (20 phòng vật lý)
-- ============================================================
INSERT INTO rooms (room_type_id, room_number, floor, allocated_for, status, is_active, created_at, updated_at) VALUES
(1,'S101',1,'DAILY','READY',true,NOW(),NOW()),
(1,'S102',1,'HOURLY','READY',true,NOW(),NOW()),
(1,'S103',1,'DAILY','DIRTY',true,NOW(),NOW()),
(1,'S104',1,'HOURLY','READY',true,NOW(),NOW()),
(1,'S105',1,'DAILY','READY',true,NOW(),NOW()),
(1,'S106',1,'HOURLY','MAINTENANCE',false,NOW(),NOW()),
(2,'D201',2,'DAILY','READY',true,NOW(),NOW()),
(2,'D202',2,'DAILY','READY',true,NOW(),NOW()),
(2,'D203',2,'HOURLY','DIRTY',true,NOW(),NOW()),
(2,'D204',2,'DAILY','READY',true,NOW(),NOW()),
(2,'D205',2,'HOURLY','READY',true,NOW(),NOW()),
(2,'D206',2,'DAILY','READY',true,NOW(),NOW()),
(2,'D207',2,'HOURLY','MAINTENANCE',false,NOW(),NOW()),
(3,'V301',3,'DAILY','READY',true,NOW(),NOW()),
(3,'V302',3,'DAILY','READY',true,NOW(),NOW()),
(3,'V303',3,'DAILY','DIRTY',true,NOW(),NOW()),
(3,'V304',3,'DAILY','READY',true,NOW(),NOW()),
(1,'S107',1,'DAILY','READY',true,NOW(),NOW()),
(2,'D208',2,'DAILY','READY',true,NOW(),NOW()),
(3,'V305',3,'DAILY','READY',true,NOW(),NOW());

-- ============================================================
-- 10. SHIFTS & SALARY CONFIG
-- ============================================================
INSERT INTO shifts (shift_name, start_time, end_time, description, is_active) VALUES
('CA SANG','06:00:00','14:00:00','Ca sang 8 gio',true),
('CA CHIEU','14:00:00','22:00:00','Ca chieu 8 gio',true),
('CA DEM','22:00:00','06:00:00','Ca dem 8 gio',true);

INSERT INTO rolesalaryconfig (staff_role, base_salary, is_active, created_at, updated_at) VALUES
('CLEANER',6000000.00,true,NOW(),NOW()),
('RECEPTIONIST',8000000.00,true,NOW(),NOW());

-- ============================================================
-- 11. BOOKINGS (20 booking với các trạng thái khác nhau)
-- ============================================================
INSERT INTO bookings (customer_id,customer_name,customer_phone,customer_email,room_type_id,requested_quantity,requested_checkin,requested_checkout,booking_type,booking_source,status,total_amount,payment_status,notes,created_at,updated_at) VALUES
-- CHECKED_OUT (đã hoàn thành) - customer01 (id=2)
(2,'Nguyen Van A','0912345678','customer01@gmail.com',1,1,'2026-05-01 14:00:00','2026-05-03 12:00:00','DAILY','WEB','CHECKED_OUT',800000.00,'PAID','Khach hang hai long',NOW()-INTERVAL '40 days',NOW()-INTERVAL '38 days'),
(2,'Nguyen Van A','0912345678','customer01@gmail.com',2,1,'2026-05-10 14:00:00','2026-05-12 12:00:00','DAILY','WEB','CHECKED_OUT',1500000.00,'PAID','Phong sach se thoai mai',NOW()-INTERVAL '31 days',NOW()-INTERVAL '29 days'),
-- CHECKED_OUT - customer02 (id=3)
(3,'Tran Thi B','0923456789','customer02@gmail.com',3,1,'2026-05-05 14:00:00','2026-05-08 12:00:00','DAILY','AGODA','CHECKED_OUT',4500000.00,'PAID','Khach tu Agoda, hai long dich vu',NOW()-INTERVAL '36 days',NOW()-INTERVAL '33 days'),
(3,'Tran Thi B','0923456789','customer02@gmail.com',1,1,'2026-05-15 09:00:00','2026-05-15 15:00:00','HOURLY','WEB','CHECKED_OUT',720000.00,'PAID','Thue theo gio',NOW()-INTERVAL '26 days',NOW()-INTERVAL '26 days'),
-- CHECKED_OUT - customer03..08
(4,'Le Thi C','0934567890','customer03@gmail.com',2,1,'2026-05-20 14:00:00','2026-05-22 12:00:00','DAILY','WEB','CHECKED_OUT',1500000.00,'PAID',NULL,NOW()-INTERVAL '21 days',NOW()-INTERVAL '19 days'),
(5,'Pham Van D','0945678901','customer04@gmail.com',1,2,'2026-05-18 14:00:00','2026-05-20 12:00:00','DAILY','BOOKING','CHECKED_OUT',1600000.00,'PAID','Dat 2 phong',NOW()-INTERVAL '23 days',NOW()-INTERVAL '21 days'),
(6,'Hoang Thi E','0956789012','customer05@gmail.com',3,1,'2026-05-25 14:00:00','2026-05-28 12:00:00','DAILY','WEB','CHECKED_OUT',4500000.00,'PAID','Khach VIP',NOW()-INTERVAL '16 days',NOW()-INTERVAL '13 days'),
(7,'Nguyen Van F','0967890123','customer06@gmail.com',1,1,'2026-05-22 10:00:00','2026-05-22 16:00:00','HOURLY','WALK-IN','CHECKED_OUT',720000.00,'PAID','Thue tai quay',NOW()-INTERVAL '19 days',NOW()-INTERVAL '19 days'),
(8,'Tran Thi G','0978901234','customer07@gmail.com',2,1,'2026-06-01 14:00:00','2026-06-03 12:00:00','DAILY','AGODA','CHECKED_OUT',1500000.00,'PAID',NULL,NOW()-INTERVAL '9 days',NOW()-INTERVAL '7 days'),
(9,'Bui Van H','0989012345','customer08@gmail.com',3,1,'2026-06-03 14:00:00','2026-06-06 12:00:00','DAILY','WEB','CHECKED_OUT',4500000.00,'PAID','Khach quay lai',NOW()-INTERVAL '7 days',NOW()-INTERVAL '4 days'),
-- CONFIRMED (đã xác nhận, chưa check-in)
(10,'Vu Thi I','0990123456','customer09@gmail.com',2,1,'2026-06-15 14:00:00','2026-06-17 12:00:00','DAILY','WEB','CONFIRMED',1500000.00,'PAID',NULL,NOW()-INTERVAL '2 days',NOW()-INTERVAL '2 days'),
(11,'Do Van J','0901234568','customer10@gmail.com',1,1,'2026-06-16 14:00:00','2026-06-18 12:00:00','DAILY','BOOKING','CONFIRMED',800000.00,'PAID',NULL,NOW()-INTERVAL '1 day',NOW()-INTERVAL '1 day'),
(14,'Hoang Van K','0906789012','customer11@gmail.com',3,1,'2026-06-18 14:00:00','2026-06-21 12:00:00','DAILY','WEB','CONFIRMED',4500000.00,'PAID','Dat phong VIP dip le',NOW(),NOW()),
-- CHECKED_IN (đang ở)
(15,'Mai Thi L','0907890123','customer12@gmail.com',1,1,'2026-06-10 14:00:00','2026-06-12 12:00:00','DAILY','WEB','CHECKED_IN',800000.00,'PAID',NULL,NOW()-INTERVAL '5 days',NOW()),
(16,'Dinh Van M','0908901234','customer13@gmail.com',2,1,'2026-06-09 14:00:00','2026-06-11 12:00:00','DAILY','WALK-IN','CHECKED_IN',750000.00,'PAID','Khach walk-in',NOW()-INTERVAL '6 days',NOW()),
(19,'Vo Thi N','0910123456','customer14@gmail.com',3,1,'2026-06-08 14:00:00','2026-06-12 12:00:00','DAILY','WEB','CHECKED_IN',6000000.00,'PAID','Dieu chinh ngay o',NOW()-INTERVAL '7 days',NOW()),
-- PENDING (chờ thanh toán)
(2,'Nguyen Van A','0912345678','customer01@gmail.com',1,1,'2026-06-20 14:00:00','2026-06-22 12:00:00','DAILY','WEB','PENDING',800000.00,'UNPAID','Dat phong truoc',NOW(),NOW()),
(3,'Tran Thi B','0923456789','customer02@gmail.com',2,1,'2026-06-25 14:00:00','2026-06-27 12:00:00','DAILY','WEB','PENDING',1500000.00,'UNPAID',NULL,NOW(),NOW()),
-- CANCELLED
(4,'Le Thi C','0934567890','customer03@gmail.com',1,1,'2026-05-28 14:00:00','2026-05-30 12:00:00','DAILY','WEB','CANCELLED',800000.00,'UNPAID','Khach huy vi ly do ca nhan',NOW()-INTERVAL '13 days',NOW()-INTERVAL '13 days'),
-- NO_SHOW
(5,'Pham Van D','0945678901','customer04@gmail.com',2,1,'2026-06-05 14:00:00','2026-06-07 12:00:00','DAILY','AGODA','NO_SHOW',1500000.00,'UNPAID','Khach khong den',NOW()-INTERVAL '5 days',NOW()-INTERVAL '3 days');

-- ============================================================
-- 12. ROOM SCHEDULES (cho các booking CHECKED_OUT, CHECKED_IN, CONFIRMED)
-- ============================================================
INSERT INTO roomschedules (booking_id,room_id,start_at,end_at,status,created_at,updated_at) VALUES
-- COMPLETED (booking CHECKED_OUT)
(1,1,'2026-05-01 14:00:00','2026-05-03 12:00:00','COMPLETED',NOW()-INTERVAL '40 days',NOW()-INTERVAL '38 days'),
(2,7,'2026-05-10 14:00:00','2026-05-12 12:00:00','COMPLETED',NOW()-INTERVAL '31 days',NOW()-INTERVAL '29 days'),
(3,14,'2026-05-05 14:00:00','2026-05-08 12:00:00','COMPLETED',NOW()-INTERVAL '36 days',NOW()-INTERVAL '33 days'),
(4,2,'2026-05-15 09:00:00','2026-05-15 15:00:00','COMPLETED',NOW()-INTERVAL '26 days',NOW()-INTERVAL '26 days'),
(5,8,'2026-05-20 14:00:00','2026-05-22 12:00:00','COMPLETED',NOW()-INTERVAL '21 days',NOW()-INTERVAL '19 days'),
(6,1,'2026-05-18 14:00:00','2026-05-20 12:00:00','COMPLETED',NOW()-INTERVAL '23 days',NOW()-INTERVAL '21 days'),
(7,15,'2026-05-25 14:00:00','2026-05-28 12:00:00','COMPLETED',NOW()-INTERVAL '16 days',NOW()-INTERVAL '13 days'),
(8,2,'2026-05-22 10:00:00','2026-05-22 16:00:00','COMPLETED',NOW()-INTERVAL '19 days',NOW()-INTERVAL '19 days'),
(9,10,'2026-06-01 14:00:00','2026-06-03 12:00:00','COMPLETED',NOW()-INTERVAL '9 days',NOW()-INTERVAL '7 days'),
(10,16,'2026-06-03 14:00:00','2026-06-06 12:00:00','COMPLETED',NOW()-INTERVAL '7 days',NOW()-INTERVAL '4 days'),
-- SCHEDULED (booking CONFIRMED)
(11,8,'2026-06-15 14:00:00','2026-06-17 12:00:00','SCHEDULED',NOW()-INTERVAL '2 days',NOW()-INTERVAL '2 days'),
(12,1,'2026-06-16 14:00:00','2026-06-18 12:00:00','SCHEDULED',NOW()-INTERVAL '1 day',NOW()-INTERVAL '1 day'),
(13,14,'2026-06-18 14:00:00','2026-06-21 12:00:00','SCHEDULED',NOW(),NOW()),
-- ACTIVE (booking CHECKED_IN)
(14,5,'2026-06-10 14:00:00','2026-06-12 12:00:00','ACTIVE',NOW()-INTERVAL '5 days',NOW()),
(15,9,'2026-06-09 14:00:00','2026-06-11 12:00:00','ACTIVE',NOW()-INTERVAL '6 days',NOW()),
(16,17,'2026-06-08 14:00:00','2026-06-12 12:00:00','ACTIVE',NOW()-INTERVAL '7 days',NOW());

-- ============================================================
-- 13. PAYMENTS (cho các booking đã PAID)
-- ============================================================
INSERT INTO payments (booking_id,amount,payment_method,gateway_name,payment_type,status,transaction_reference,payment_date,notes) VALUES
(1,800000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026050100001',NOW()-INTERVAL '40 days','Thanh toan qua VNPAY'),
(2,1500000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026051000002',NOW()-INTERVAL '31 days','Thanh toan online'),
(3,4500000.00,'ONLINE','PAYOS','FULL_ROOM_CHARGE','SUCCESS','PAYOS2026050500003',NOW()-INTERVAL '36 days','Thanh toan qua PayOS'),
(4,720000.00,'CASH',NULL,'FULL_ROOM_CHARGE','SUCCESS','CASH2026051500004',NOW()-INTERVAL '26 days','Tra tien mat tai quay'),
(5,1500000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026052000005',NOW()-INTERVAL '21 days','Thanh toan VNPAY'),
(6,1600000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026051800006',NOW()-INTERVAL '23 days','Dat 2 phong'),
(7,4500000.00,'ONLINE','PAYOS','FULL_ROOM_CHARGE','SUCCESS','PAYOS2026052500007',NOW()-INTERVAL '16 days','VIP booking'),
(8,720000.00,'CASH',NULL,'FULL_ROOM_CHARGE','SUCCESS','CASH2026052200008',NOW()-INTERVAL '19 days','Walk-in cash'),
(9,1500000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026060100009',NOW()-INTERVAL '9 days','Agoda booking'),
(10,4500000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026060300010',NOW()-INTERVAL '7 days','VIP quay lai'),
(11,1500000.00,'ONLINE','PAYOS','FULL_ROOM_CHARGE','SUCCESS','PAYOS2026060800011',NOW()-INTERVAL '2 days','Booking truoc'),
(12,800000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026060900012',NOW()-INTERVAL '1 day','Booking.com'),
(13,4500000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026061000013',NOW(),'Dat VIP dip le'),
(14,800000.00,'ONLINE','VNPAY','FULL_ROOM_CHARGE','SUCCESS','VNPAY2026061001014',NOW()-INTERVAL '5 days','Checked in'),
(15,750000.00,'CASH',NULL,'FULL_ROOM_CHARGE','SUCCESS','CASH2026061001015',NOW()-INTERVAL '6 days','Walk-in cash'),
(16,6000000.00,'ONLINE','PAYOS','FULL_ROOM_CHARGE','SUCCESS','PAYOS2026060801016',NOW()-INTERVAL '7 days','VIP extended');

-- ============================================================
-- 14. INVOICES
-- ============================================================
INSERT INTO invoices (booking_id,payment_id,customer_name,customer_email,customer_phone,amount_paid,invoice_description,issued_at,is_sent_email) VALUES
(1,1,'Nguyen Van A','customer01@gmail.com','0912345678',800000.00,'TIEN PHONG STANDARD 2 NGAY',NOW()-INTERVAL '38 days',true),
(2,2,'Nguyen Van A','customer01@gmail.com','0912345678',1500000.00,'TIEN PHONG DELUXE 2 NGAY',NOW()-INTERVAL '29 days',true),
(3,3,'Tran Thi B','customer02@gmail.com','0923456789',4500000.00,'TIEN PHONG VIP 3 NGAY',NOW()-INTERVAL '33 days',true),
(4,4,'Tran Thi B','customer02@gmail.com','0923456789',720000.00,'TIEN PHONG STANDARD THEO GIO',NOW()-INTERVAL '26 days',true),
(5,5,'Le Thi C','customer03@gmail.com','0934567890',1500000.00,'TIEN PHONG DELUXE 2 NGAY',NOW()-INTERVAL '19 days',true),
(6,6,'Pham Van D','customer04@gmail.com','0945678901',1600000.00,'TIEN PHONG STANDARD 2 PHONG',NOW()-INTERVAL '21 days',true),
(7,7,'Hoang Thi E','customer05@gmail.com','0956789012',4500000.00,'TIEN PHONG VIP 3 NGAY',NOW()-INTERVAL '13 days',true),
(8,8,'Nguyen Van F','customer06@gmail.com','0967890123',720000.00,'TIEN PHONG STANDARD THEO GIO',NOW()-INTERVAL '19 days',true),
(9,9,'Tran Thi G','customer07@gmail.com','0978901234',1500000.00,'TIEN PHONG DELUXE 2 NGAY',NOW()-INTERVAL '7 days',true),
(10,10,'Bui Van H','customer08@gmail.com','0989012345',4500000.00,'TIEN PHONG VIP 3 NGAY',NOW()-INTERVAL '4 days',true),
(11,11,'Vu Thi I','customer09@gmail.com','0990123456',1500000.00,'TIEN PHONG DELUXE 2 NGAY',NOW()-INTERVAL '2 days',false),
(12,12,'Do Van J','customer10@gmail.com','0901234568',800000.00,'TIEN PHONG STANDARD 2 NGAY',NOW()-INTERVAL '1 day',false),
(13,13,'Hoang Van K','customer11@gmail.com','0906789012',4500000.00,'TIEN PHONG VIP 3 NGAY',NOW(),false),
(14,14,'Mai Thi L','customer12@gmail.com','0907890123',800000.00,'TIEN PHONG STANDARD 2 NGAY',NOW()-INTERVAL '5 days',true),
(15,15,'Dinh Van M','customer13@gmail.com','0908901234',750000.00,'TIEN PHONG DELUXE 2 NGAY',NOW()-INTERVAL '6 days',true),
(16,16,'Vo Thi N','customer14@gmail.com','0910123456',6000000.00,'TIEN PHONG VIP 4 NGAY',NOW()-INTERVAL '7 days',true);

-- ============================================================
-- 15. REVIEWS (cho các booking CHECKED_OUT có rating 3-5 sao)
-- ============================================================
INSERT INTO reviews (booking_id,customer_id,customer_name,room_type_id,rating,comment,hotel_reply,replied_at,created_at) VALUES
(1,2,'Nguyen Van A',1,5,'Phong rat sach se, nhan vien le tan nhiet tinh. Se quay lai!','Cam on anh A da tin tuong su dung dich vu. Hen gap lai!',NOW()-INTERVAL '37 days',NOW()-INTERVAL '38 days'),
(2,2,'Nguyen Van A',2,4,'Phong Deluxe dep, view tot. Dich vu tot nhung wifi hoi cham.','Cam on anh da phan hoi. Chung toi se nang cap wifi.',NOW()-INTERVAL '28 days',NOW()-INTERVAL '29 days'),
(3,3,'Tran Thi B',3,5,'Phong VIP that su xung dang. Bon tam Jacuzzi tuyet voi!','Cam on chi B da chon phong VIP. Rat vui duoc phuc vu!',NOW()-INTERVAL '32 days',NOW()-INTERVAL '33 days'),
(4,3,'Tran Thi B',1,4,'Thue theo gio tien loi, phong sach. Nhan vien than thien.',NULL,NULL,NOW()-INTERVAL '26 days'),
(5,4,'Le Thi C',2,5,'Phong Deluxe rat xung dang voi gia tien. Breakfast ngon!','Cam on chi C. Chung toi ranh don ban trong tuong lai!',NOW()-INTERVAL '18 days',NOW()-INTERVAL '19 days'),
(6,5,'Pham Van D',1,3,'Phong ok nhung hoi on ao vi gan duong lon. Can cach am tot hon.','Cam on anh D da gop y. Chung toi se cai thien.',NOW()-INTERVAL '20 days',NOW()-INTERVAL '21 days'),
(7,6,'Hoang Thi E',3,5,'Phong VIP dang cap nhat Da Nang. Se gioi thieu cho ban be!','Cam on chi E! Moi chi quay lai va cho chung toi phuc vu.',NOW()-INTERVAL '12 days',NOW()-INTERVAL '13 days'),
(8,7,'Nguyen Van F',1,4,'Nhanh chong, tien loi. Nhan vien ho tro nhiet tinh.','Cam on anh F! Hen gap lai.',NOW()-INTERVAL '18 days',NOW()-INTERVAL '19 days'),
(9,8,'Tran Thi G',2,4,'Phong sach, view dep. Dich vu chuyen nghiep.','Cam on chi G da lua chon chung toi!',NOW()-INTERVAL '6 days',NOW()-INTERVAL '7 days'),
(10,9,'Bui Van H',3,5,'Lan 3 o day roi, lan nao cung rat hai long. Phong VIP xuat sac!','Cam on anh H luon tin tuong chung toi! Hen gap anh lan 4!',NOW()-INTERVAL '3 days',NOW()-INTERVAL '4 days');

-- ============================================================
-- 16. ASSIGN STAFF (phân công nhân viên - user id 12,13 là cleaner/receptionist)
-- ============================================================
INSERT INTO assignstaff (user_id,salary_id,shift_id,work_date) VALUES
(12,1,1,'2026-06-01'),(12,1,1,'2026-06-02'),(12,1,1,'2026-06-03'),
(12,1,1,'2026-06-04'),(12,1,1,'2026-06-05'),(12,1,1,'2026-06-06'),
(12,1,1,'2026-06-07'),(12,1,1,'2026-06-08'),(12,1,1,'2026-06-09'),
(12,1,1,'2026-06-10'),
(13,1,2,'2026-06-01'),(13,1,2,'2026-06-02'),(13,1,2,'2026-06-03'),
(13,1,2,'2026-06-04'),(13,1,2,'2026-06-05'),(13,1,2,'2026-06-06'),
(13,1,2,'2026-06-07'),(13,1,2,'2026-06-08'),(13,1,2,'2026-06-09'),
(13,1,2,'2026-06-10');

-- ============================================================
-- 17. ATTENDANCE (chấm công cho nhân viên)
-- ============================================================
INSERT INTO attendance (user_id,shift_assignment_id,check_in,check_out,work_hours,late_minutes,early_leave_minutes,status,created_at) VALUES
(12,1,'2026-06-01 06:05:00','2026-06-01 14:00:00',7.92,5,0,'LATE','2026-06-01 14:00:00'),
(12,2,'2026-06-02 06:00:00','2026-06-02 14:00:00',8.00,0,0,'PRESENT','2026-06-02 14:00:00'),
(12,3,'2026-06-03 06:00:00','2026-06-03 14:00:00',8.00,0,0,'PRESENT','2026-06-03 14:00:00'),
(12,4,'2026-06-04 06:00:00','2026-06-04 14:00:00',8.00,0,0,'PRESENT','2026-06-04 14:00:00'),
(12,5,'2026-06-05 06:15:00','2026-06-05 14:00:00',7.75,15,0,'LATE','2026-06-05 14:00:00'),
(12,6,'2026-06-06 06:00:00','2026-06-06 14:00:00',8.00,0,0,'PRESENT','2026-06-06 14:00:00'),
(12,7,'2026-06-07 06:00:00','2026-06-07 14:00:00',8.00,0,0,'PRESENT','2026-06-07 14:00:00'),
(12,8,'2026-06-08 06:00:00','2026-06-08 13:30:00',7.50,0,30,'PRESENT','2026-06-08 14:00:00'),
(12,9,'2026-06-09 06:00:00','2026-06-09 14:00:00',8.00,0,0,'PRESENT','2026-06-09 14:00:00'),
(12,10,'2026-06-10 06:00:00','2026-06-10 14:00:00',8.00,0,0,'PRESENT','2026-06-10 14:00:00'),
(13,11,'2026-06-01 14:00:00','2026-06-01 22:00:00',8.00,0,0,'PRESENT','2026-06-01 22:00:00'),
(13,12,'2026-06-02 14:05:00','2026-06-02 22:00:00',7.92,5,0,'LATE','2026-06-02 22:00:00'),
(13,13,'2026-06-03 14:00:00','2026-06-03 22:00:00',8.00,0,0,'PRESENT','2026-06-03 22:00:00'),
(13,14,'2026-06-04 14:00:00','2026-06-04 22:00:00',8.00,0,0,'PRESENT','2026-06-04 22:00:00'),
(13,15,'2026-06-05 14:00:00','2026-06-05 22:00:00',8.00,0,0,'PRESENT','2026-06-05 22:00:00'),
(13,16,'2026-06-06 14:00:00','2026-06-06 22:00:00',8.00,0,0,'PRESENT','2026-06-06 22:00:00'),
(13,17,'2026-06-07 00:00:00',NULL,0.00,0,0,'ABSENT','2026-06-07 22:00:00'),
(13,18,'2026-06-08 14:00:00','2026-06-08 22:00:00',8.00,0,0,'PRESENT','2026-06-08 22:00:00'),
(13,19,'2026-06-09 14:00:00','2026-06-09 22:00:00',8.00,0,0,'PRESENT','2026-06-09 22:00:00'),
(13,20,'2026-06-10 14:00:00','2026-06-10 22:00:00',8.00,0,0,'PRESENT','2026-06-10 22:00:00');

-- ============================================================
-- 18. BONUS PENALTY
-- ============================================================
INSERT INTO bonuspenalty (type,amount,reason,related_date,created_at) VALUES
('BONUS',500000.00,'Nhan vien xuat sac thang 5','2026-05-31',NOW()-INTERVAL '10 days'),
('BONUS',300000.00,'Hoan thanh tot nhiem vu dac biet','2026-05-15',NOW()-INTERVAL '25 days'),
('PENALTY',200000.00,'Di lam muon qua 3 lan trong thang','2026-05-31',NOW()-INTERVAL '10 days'),
('BONUS',1000000.00,'Nhan vien thang 5 - toat xuat nhat','2026-05-31',NOW()-INTERVAL '10 days'),
('PENALTY',150000.00,'Khong mac dong phuc dung quy dinh','2026-06-05',NOW()-INTERVAL '5 days');

-- ============================================================
-- 19. SALARY SHEET (bảng lương tháng 5)
-- ============================================================
INSERT INTO salarysheet (user_id,salary_id,month,year,total_salary,status,created_at,updated_at) VALUES
(12,1,5,2026,6300000.00,'PAID',NOW()-INTERVAL '10 days',NOW()-INTERVAL '10 days'),
(13,1,5,2026,5850000.00,'PAID',NOW()-INTERVAL '10 days',NOW()-INTERVAL '10 days'),
(14,2,5,2026,8500000.00,'PAID',NOW()-INTERVAL '10 days',NOW()-INTERVAL '10 days'),
(15,2,5,2026,8000000.00,'PAID',NOW()-INTERVAL '10 days',NOW()-INTERVAL '10 days'),
(12,1,6,2026,0.00,'PENDING',NOW(),NOW()),
(13,1,6,2026,0.00,'PENDING',NOW(),NOW()),
(14,2,6,2026,0.00,'PENDING',NOW(),NOW()),
(15,2,6,2026,0.00,'PENDING',NOW(),NOW());

-- ============================================================
-- VERIFY
-- ============================================================
SELECT 'users' as tbl, COUNT(*) as total FROM users
UNION ALL SELECT 'hotel', COUNT(*) FROM hotel
UNION ALL SELECT 'roomtypes', COUNT(*) FROM roomtypes
UNION ALL SELECT 'rooms', COUNT(*) FROM rooms
UNION ALL SELECT 'bookings', COUNT(*) FROM bookings
UNION ALL SELECT 'payments', COUNT(*) FROM payments
UNION ALL SELECT 'invoices', COUNT(*) FROM invoices
UNION ALL SELECT 'reviews', COUNT(*) FROM reviews
UNION ALL SELECT 'roomschedules', COUNT(*) FROM roomschedules
UNION ALL SELECT 'assignstaff', COUNT(*) FROM assignstaff
UNION ALL SELECT 'attendance', COUNT(*) FROM attendance
UNION ALL SELECT 'salarysheet', COUNT(*) FROM salarysheet
ORDER BY tbl;
