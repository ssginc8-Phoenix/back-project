-- 1. 유저 (tbl_user)
-- 1환자 2보호자 3병원 관리자 4의사 5어드민
INSERT INTO tbl_user (user_id,
                      uuid,
                      email,
                      password,
                      name,
                      phone,
                      address,
                      login_type,
                      role,
                      penalty,
                      is_suspended,
                      suspended_at,
                      suspension_expires_at,
                      created_at,
                      updated_at)
VALUES (1, '550e8400-e29b-41d4-a716-446655440000', 'user1@example.com', 'encodedPassword1', '홍길동', '010-1234-5678',
        '서울시 강남구', 'EMAIL', 'PATIENT', 0, false, NULL, NULL, now(), now()),
       (2, '550e8400-e29b-41d4-a716-446655440001', 'user2@example.com', 'encodedPassword2', '김철수', '010-1234-5678',
        '서울시 강남구', 'EMAIL', 'GUARDIAN', 0, false, NULL, NULL, now(), now()),
       (3, '550e8400-e29b-41d4-a716-446655440002', 'user3@example.com', 'encodedPassword3', '오수민', '010-1234-5678',
        '서울시 강남구', 'EMAIL', 'HOSPITAL_ADMIN', 0, false, NULL, NULL, now(), now()),
       (4, '550e8400-e29b-41d4-a716-446655440003', 'user4@example.com', 'encodedPassword4', '한민상', '010-1234-5678',
        '서울시 강남구', 'EMAIL', 'DOCTOR', 0, false, NULL, NULL, now(), now()),
       (5, '550e8400-e29b-41d4-a716-446655440004', 'user5@example.com', 'encodedPassword5', '박재성', '010-1234-5678',
        '서울시 강남구', 'EMAIL', 'SYSTEM_ADMIN', 0, false, NULL, NULL, now(), now());

-- 2. 환자 (tbl_patient)
INSERT INTO tbl_patient (patient_id,
                         user_id,
                         resident_registration_number,
                         created_at,
                         updated_at)
VALUES (1, 1, '900101-1234567', now(), now());

-- 3. 보호자 (tbl_patient_guardian)
-- 1번 환자가 2번 보호자에게 소속됨
INSERT INTO tbl_patient_guardian (patient_guardian_id,
                                  user_id,
                                  patient_id,
                                  status,
                                  invited_at,
                                  responded_at,
                                  invite_code,
                                  created_at,
                                  updated_at)
VALUES (1, 2, 1, 'ACCEPTED', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day', 'inv-1-2-20250529-7f3d9a', now(),
        now());

-- 4. 병원 (tbl_hospital)
-- 3 유저가 1 병원의 관리자
INSERT INTO tbl_hospital (hospital_id,
                          user_id,
                          name,
                          address,
                          latitude,
                          longitude,
                          phone,
                          introduction,
                          notice,
                          waiting,
                          business_registration_number,
                          created_at,
                          updated_at)
VALUES (1, -- 병원 PK
        3, -- 위 tbl_user의 user_id 참조 (HOSPITAL_ADMIN)
        '강남헬스케어병원',
        '서울시 강남구 테헤란로 123',
        37.498095,
        127.027610,
        '02-1234-5678',
        '365일 24시간 진료하는 종합병원입니다.',
        '현재 대기시간은 30분입니다.',
        0,
        '123-45-67890',
        now(),
        now());

-- 5. 병원 스케줄(tbl_hospital_schedule) - 24시간 운영, 쉬는시간 없음
INSERT INTO tbl_hospital_schedule (hospital_schedule_id,
                                   hospital_id,
                                   day_of_week,
                                   open_time,
                                   close_time,
                                   lunch_start,
                                   lunch_end)
VALUES (1, 1, 'MONDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (2, 1, 'TUESDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (3, 1, 'WEDNESDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (4, 1, 'THURSDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (5, 1, 'FRIDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (6, 1, 'SATURDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00'),
       (7, 1, 'SUNDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00');

-- 6. 의사 (tbl_doctor)
INSERT INTO tbl_doctor (doctor_id,
                        hospital_id,
                        user_id,
                        specialization,
                        capacity_per_half_hour,
                        created_at,
                        updated_at)
VALUES (1, -- 의사 PK
        1, -- 기존 병원 ID
        4, -- 의사 유저 ID (user_id=4)
        'CARDIOLOGY', -- 전문 분야
        0,
        now(),
        now());

-- 7. 의사 스케쥴 (tbl_doctor_schedule)
INSERT INTO tbl_doctor_schedule (schedule_id,
                                 doctor_id,
                                 day_of_week,
                                 start_time,
                                 end_time,
                                 lunch_start,
                                 lunch_end,
                                 created_at,
                                 updated_at)
VALUES (1, 1, 'MONDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (2, 1, 'TUESDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (3, 1, 'WEDNESDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (4, 1, 'THURSDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (5, 1, 'FRIDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (6, 1, 'SATURDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now()),
       (7, 1, 'SUNDAY', '2000-01-01 00:00:00', '2000-01-01 23:59:00', '2000-01-01 00:00:00', '2000-01-01 00:00:00',
        now(), now());
