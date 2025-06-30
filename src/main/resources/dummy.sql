-- 1. 환자 (tbl_patient)
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
VALUES (1, 2, 1, 'ACCEPTED', NOW(), NOW(), 'inv-1-2-20250529-7f3d9a', now(),
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
        '365일 진료하는 종합병원입니다.',
        '현재 대기시간은 30분입니다.',
        0,
        '123-45-67890',
        now(),
        now());

-- 병원 스케줄 (9시 ~ 18시, 점심시간 13:00 ~ 14:00)
INSERT INTO tbl_hospital_schedule (hospital_schedule_id,
                                   hospital_id,
                                   day_of_week,
                                   open_time,
                                   close_time,
                                   lunch_start,
                                   lunch_end)
VALUES
    (1, 1, 'MONDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (2, 1, 'TUESDAY',   '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (3, 1, 'WEDNESDAY', '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (4, 1, 'THURSDAY',  '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (5, 1, 'FRIDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (6, 1, 'SATURDAY',  '10:00:00', '20:00:00', '13:00:00', '14:30:00'),
    (7, 1, 'SUNDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00');


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

-- 의사 스케줄 (9시 ~ 18시, 점심시간 13:00 ~ 14:00)
INSERT INTO tbl_doctor_schedule (schedule_id,
                                 doctor_id,
                                 day_of_week,
                                 start_time,
                                 end_time,
                                 lunch_start,
                                 lunch_end,
                                 created_at,
                                 updated_at)
VALUES
    (1, 1, 'MONDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (2, 1, 'TUESDAY',   '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (3, 1, 'WEDNESDAY', '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (4, 1, 'THURSDAY',  '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (5, 1, 'FRIDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (6, 1, 'SATURDAY',  '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now()),
    (7, 1, 'SUNDAY',    '10:00:00', '20:00:00', '13:00:00', '14:30:00', now(), now());
