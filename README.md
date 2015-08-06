# ScrapeQL
SQL-like query parser to extraxt and combine structured data from different sources like
 - HTML
 - CSV
 - JSON
 - SQL Databases

#Why?

In general, it is better to use a dedicated API to process external data.

**But...**
...sometimes this is not possible and you need to extract and combine data from several sources. This can be a very complicated task, because you have to deal with
 - untidy HTML
 - many CSV files
 - complex code to navigate though Data
 - many loops, ifs,...

ScrapeQL gives you a powerful SQL-like query language, with makes fetching and combining data as simple as writing a database query.

#Features v0.2

Working features (or in testing state)
 - Run queries from Java via DSL or String query
 - Run queries via command line `ScrapeQL-cli`
 - Rudimentary JDBC driver (just normal statements or prepared statements) `ScrapeQL-jdbc` **not available for v0.2 yet**

Feature Backlog (work in progress):
 - group functions
 - jdbc driver

#Use

Please take a look at the [User Guide](https://github.com/plechi/ScrapeQL/wiki) (old version)

#Sample

This is how it (could) look like (i call it ScrQL):

```sql
select tbl1.col, tbl2.col
FROM (
    RELATION $('li>a') as coll 
    FROM load_html(url('htttp://www.example.com/') tbl1,
(
    RELATION $('h1') as col 
    FROM load_html(TXT>><span>one</span<span>two</span><<TXT))
```

#Disclaimer
Scraping Websites is illegal in some countries. Please handle this tool with care!

This is a proof of concept and WIP. It may not be suited for production enviroments.

#Used tools

 - JSOUP for HTML parsing
 - Scala Parsers for parsing the queries
