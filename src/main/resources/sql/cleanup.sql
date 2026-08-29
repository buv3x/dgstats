DELETE FROM datas.basket_variation_round_division bvrd
WHERE bvrd.round_division_id IN (
    SELECT rd.id
    FROM datas.round_division rd
             JOIN datas.round r ON r.id = rd.round_id
             JOIN datas.competition c ON c.id = r.competition_id
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.hole_score hs
WHERE hs.round_result_id IN (
    SELECT rr.id
    FROM datas.round_result rr
             JOIN datas.round_division rd ON rd.id = rr.round_division_id
             JOIN datas.round r ON r.id = rd.round_id
             JOIN datas.competition c ON c.id = r.competition_id
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.round_result rr
WHERE rr.round_division_id IN (
    SELECT rd.id
    FROM datas.round_division rd
             JOIN datas.round r ON r.id = rd.round_id
             JOIN datas.competition c ON c.id = r.competition_id
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.round_division rd
WHERE rd.round_id IN (
    SELECT r.id
    FROM datas.round r
             JOIN datas.competition c ON c.id = r.competition_id
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.layout_hole lh
WHERE lh.layout_id IN (
    SELECT l.id
    FROM datas.layout l
             JOIN datas.competition c ON c.id = l.competition_id
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.competition_division cd
WHERE cd.competition_id IN (
    SELECT c.id
    FROM datas.competition c
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.round r
WHERE r.competition_id IN (
    SELECT c.id
    FROM datas.competition c
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.layout l
WHERE l.competition_id IN (
    SELECT c.id
    FROM datas.competition c
    WHERE c.pdga_id = 104676
);

DELETE FROM datas.competition c
WHERE c.pdga_id = 104676;

DELETE FROM datas.competition_import ci
WHERE ci.competition_id = 104676;

DELETE FROM datas.player p
WHERE NOT EXISTS (
    SELECT 1
    FROM datas.round_result rr
    WHERE rr.player_id = p.id
);

DELETE FROM datas.course co
WHERE NOT EXISTS (
    SELECT 1
    FROM datas.competition c
    WHERE c.course_id = co.id
)
  AND NOT EXISTS (
    SELECT 1
    FROM datas.layout l
    WHERE l.course_id = co.id
);
