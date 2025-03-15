INSERT INTO "user" (id,
                    username,
                    password,
                    role,
                    date_created,
                    last_updated)
VALUES (1000,
        'guest',
        '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
        'GUEST',
        '2024-09-02 14:30:00.000000+00',
        '2024-09-02 14:30:00.000000+00');

INSERT INTO "user" (id,
                    username,
                    password,
                    role,
                    date_created,
                    last_updated)
VALUES (1001,
        'manager',
        '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
        'MANAGER',
        '2024-09-03 14:30:00.000000+00',
        '2024-09-03 14:30:00.000000+00');

INSERT INTO "user" (id,
                    username,
                    password,
                    role,
                    date_created,
                    last_updated)
VALUES (1002,
        'owner',
        '{bcrypt}$2a$10$FMzmOkkfbApEWxS.4XzCKOR7EbbiwzkPEyGgYh6uQiPxurkpzRMa6',
        'OWNER',
        '2024-09-04 14:30:00.000000+00',
        '2024-09-04 14:30:00.000000+00');
