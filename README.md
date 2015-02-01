# ScrapeQL
SQL-like query parser to extraxt structured data from websites.

#Why?

In general, it is better to use a dedicated API to process external data.

**But...**
...sometimes this is not possible and you need to extract data from HTML. This can be a very complicated Task, because you have to deal with
 - untidy HTML
 - complex code to navigate though DOM
 - many loops, ifs,...

ScrapeQL gives you a handy query language, inspired by SQL which enables you to fetch website data as easy it is to query your database.

My motivation for this project came from the interest to learn ANTLR and the need for a simple website scraping framework.

#Features

Features I'm working on right now:

 - create a Query Language which is able to define website queries
 - Query websites and select the data you want (either with Java DSL or text queries)
 - Map data to Java Beans
 - Get data as JSON (for other languages than Java)

Feature Backlog:

 - Cache both website requests and query results
 - implement standalone server (like a DB server)
 - implement new language features like subqueries, functions,...

#Sample

This is how it (could) look like (i call it SrQL):

```sql
/*fetch HTML from example.com*/
LOAD "http://example.com/"; 

/*same as SELECT FIRST statement*/
SELECT 'table#table' AS table FROM root; 
/*make 'global' variable genericname*/
SELECT FIRST 'h1' AS header FROM root; 

/*select list of repeating elements which are all children of #maindiv*/
SELECT EVERY 
    'selector' AS name1, 
    'selector' AS name2, 
    CONCAT('selector1','selector2',"STATIC") AS name3, 
    header AS name4 
    IN 'tr'
    FROM table INTO listvariable;

/*mark variable as output variable*/
OUTPUT listvariable; 
```
produces something like this:

```
+-------------------------------------------------+
| listvariable                                    |
+--------+--------+----------------------+--------+
| name1  | name2  | name3                | name4  |
+--------+--------+----------------------+--------+
| val1.0 | val2.0 | val3.0val3.0.1STATIC | val4.0 |
+--------+--------+----------------------+--------+
| val1.1 | val2.1 | val3.1val3.1.1STATIC | val4.1 |
+--------+--------+----------------------+--------+
| val1.2 | val2.2 | val3.2val3.1.1STATIC | val4.2 |
+--------+--------+----------------------+--------+
|  ...   |  ...   |  ......STATIC        |  ...   |
+--------+--------+----------------------+--------+
```

#Disclaimer
Scraping Websites is illegal in some countries. Please handle this tool with care!

This is a proof of concept and WIP. It may not be suited for production enviroments.

#Used tools

 - JSOUP for HTML parsing
 - ANTLR for parsing the queries
