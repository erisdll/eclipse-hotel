/*1. Quantos clientes temos na base?*/

SELECT
    COUNT(*) AS total_customers
FROM
    customers;

/*2. Quantos quartos temos cadastrados?*/

SELECT
    COUNT(*) AS total_rooms
FROM
    rooms;

/*3. Quantas reservas em aberto o hotel possui no momento?*/

SELECT
    COUNT(*) AS total_reservations_in_use
FROM
    reservations
WHERE
    reservations.status = 'IN_USE';

/*4. Quantos quartos temos vagos no momento?*/

SELECT
    (SELECT COUNT(*)
     FROM rooms) -
    (SELECT COUNT(room_id)
     FROM reservations
     WHERE status = 'IN_USE') AS total_vacant_rooms;

/*5. Quantos quartos temos ocupados no momento?*/

SELECT
    COUNT(room_id) AS total_currently_booked_rooms
FROM
    reservations
WHERE
    status = 'IN_USE';

/*6. Quantas reservas futuras o hotel possui?*/

SELECT
    COUNT(*) AS total_scheduled_reservations
FROM
    reservations
WHERE
    status = 'SCHEDULED';

/*7. Qual o quarto mais caro do hotel?*/

SELECT
    room_number,
    type,
    price
FROM
    rooms
WHERE
    price = (SELECT MAX(price) FROM rooms);

/*8. Qual o quarto com maior histórico de cancelamentos?*/

SELECT
    rooms.room_number,
    COUNT(*) AS most_cancelled_room
FROM
    reservations
JOIN
    rooms ON reservations.room_id = rooms.id
WHERE
    reservations.status = 'CANCELLED'
GROUP BY
    rooms.room_number
ORDER BY
    most_cancelled_room DESC;

/*9. Liste todos os quartos e a quantidade de clientes que já ocuparam cada um.*/

SELECT
    rooms.room_number,
    COUNT(DISTINCT reservations.customer_id) AS total_customers
FROM
    rooms
LEFT JOIN
    reservations ON rooms.id = reservations.room_id
GROUP BY
    rooms.room_number;

/*10. Quais são os 3 quartos que possuem um histórico maior de ocupações?*/

SELECT
    rooms.room_number,
    COUNT(reservations.id) AS total_reservations
FROM
    rooms
LEFT JOIN
    reservations ON rooms.id = reservations.room_id
GROUP BY
    rooms.room_number
ORDER BY
    total_occupations DESC
LIMIT 3;

/*11. No próximo mês, o hotel fará uma promoção para os seus 10 clientes que possuírem
maior histórico de reservas e você foi acionado pelo seu time para extrair esta
informação do banco de dados. Quem são os 10 clientes?*/

SELECT
    customers.name,
    customers.email,
    COUNT(reservations.id) AS total_reservations
FROM
    customers
JOIN
    reservations ON customers.id = reservations.customer_id
GROUP BY
    customers.id, customers.name, customers.email
ORDER BY
    total_reservations DESC;
