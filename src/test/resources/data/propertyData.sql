INSERT INTO property (id,
                      address,
                      title,
                      price_per_day,
                      property_owner_id,
                      date_created,
                      last_updated)
VALUES (1100,
        'Nam liber tempor.',
        'Et ea rebum.',
        64.08,
        1002,
        '2024-09-02 14:30:00.000000+00',
        '2024-09-02 14:30:00.000000+00');

INSERT INTO property (id,
                      address,
                      title,
                      price_per_day,
                      property_owner_id,
                      date_created,
                      last_updated)
VALUES (1101,
        'Consetetur sadipscing.',
        'Eget est lorem.',
        65.08,
        1002,
        '2024-09-03 14:30:00.000000+00',
        '2024-09-03 14:30:00.000000+00');

-- Add property managers relationship
INSERT INTO property_managers (property_id, user_id)
VALUES (1100, 1001);

INSERT INTO property_managers (property_id, user_id)
VALUES (1101, 1001);
