
/*New Query format: Single Query=Single result (multiple results with batch queries)*/
SELECT master.$('selector') AS alias1, 
    child.$('otherselector') AS alias2, 
    external.$('h1') AS title
FROM 'test.html'.$('base_selector') master
JOIN master.$('subselect') child
JOIN PARAM_URL('single-{?}.html', ATTR(alias2,'data-id')) external;



LOAD $('selector'), $('selector') FROM 'test.html' BASE $('base_selector') INTO relation_name