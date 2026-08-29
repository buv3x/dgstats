INSERT INTO datas.basket_course(
    name)
VALUES ('Alytus');

INSERT INTO datas.basket(
    name, basket_course_id)
select a.n, 1 from generate_series(1, 18) as a(n);