
/*New Query format: Single Query=Single result (multiple results with batch queries)*/
SELECT relation1.column AS alias, relation2.column, function(relation1.column1)
FROM (
    LOAD $('selector1') AS column, $('selector2') AS column1
    FROM LOAD_HTML('http://example.com',$('base_selector'))
    ) relation1,
    (LOAD $('selector1') AS column FROM LOAD_CSV('file:///path/to/file.csv')) relation2
WHERE relation2.column IS function(relation1.column)
GROUP BY relation1.column1 --optional
ORDER BY alias
LIMIT 10 OFFSET 0

/*
Conventions:
 - no columns in LOAD clause: all entities from the next level under base/root
 - unique column names/alias


 */