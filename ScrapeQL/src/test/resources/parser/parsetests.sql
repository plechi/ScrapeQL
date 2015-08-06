SELECT * FROM (LOAD $('test') FROM function());
SELECT * FROM (LOAD $('test') AS alias FROM function());

SELECT identifier FROM (LOAD $('test') FROM function());
SELECT identifier.alias FROM (LOAD $('test') AS alias FROM function());
SELECT identifier.alias FROM (LOAD $('test') AS alias FROM function() $('base'));

SELECT alias.* FROM (LOAD $('test') AS alias FROM function());
SELECT alias.* FROM (LOAD $('test') AS alias FROM function() $('base'));