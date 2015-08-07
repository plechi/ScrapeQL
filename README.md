# ScrapeQL
SQL-like query parser to extraxt and combine structured data from different sources like
 - HTML & XML
 - CSV (not implemented yet)
 - JSON (not implemented yet)
 - SQL Databases (not implemented yet)

#Why?

In general, it is better to use a dedicated API to process external data.

**But...**
...sometimes this is not possible and you need to extract and combine data from several sources. This can be a very complicated task, because you have to deal with
 - untidy HTML
 - many CSV files
 - complex code to navigate though Data
 - many loops, ifs,...

ScrapeQL gives you a powerful SQL-like query language and JQuery-like Selectors with makes fetching and combining data as simple as writing a database query.

#Features v0.2 (2015-08-07)

- Run Queries
- Cartesian product & where clause (INNER JOIN, CROSS JOIN, JOIN,...)
    - There is a bug in the where clause parser when using AND, OR
- Multiple threads for loading data (waits for the slowest before merging data)
- Basic functions

#User guide

Please take a look at the [ScrapeQL Wiki](https://github.com/plechi/ScrapeQL/wiki)

#Sample

Sample query:

```sql
SELECT tag_text(wiki.key) AS key, tag_text(wiki.value) AS value 
FROM ( 
    LOAD $('th') AS key, $('td>*') AS value 
    FROM load_html(url('https://en.wikipedia.org/wiki/Java_(programming_language)'))
    $('table.infobox>tbody>tr')
) AS wiki 
WHERE str_length(regex_replace(tag_text(wiki.value),'[\W]+','')) > 0
```

Result:
```
+====================================================+
| key                    | value                     |
|====================================================|
| Paradigm               | multi-paradigm            |
| Paradigm               | object-oriented           |
| Paradigm               | class-based               |
| Paradigm               | structured                |
| Paradigm               | imperative                |
| Paradigm               | functional                |
| Paradigm               | generic                   |
| Paradigm               | reflective                |
| Paradigm               | concurrent                |
| Designed by            | James Gosling             |
| Designed by            | Sun Microsystems          |
| Developer              | Oracle Corporation        |
| First appeared         | ; 20 years ago            |
| First appeared         |  (1995)                   |
| First appeared         | [1]                       |
| Stable release         | [2]                       |
| Stable release         | ; 24 days ago             |
| Stable release         |  (2015-07-14)             |
| Stable release         | [2]                       |
| Preview release        | ; 6 months ago            |
| Preview release        |  (2015-01-20)             |
| Typing discipline      | Static, strong, safe      |
| Typing discipline      | nominative                |
| Typing discipline      | manifest                  |
| Implementation language| C                         |
| Implementation language| C++                       |
| OS                     | Cross-platform            |
| License                | GNU General Public License|
| License                | Java Community Process    |
| Filename extensions    | .class                    |
| Filename extensions    | .jar                      |
| Website                | Official Site             |
| Website                | For Java Developers       |
+----------------------------------------------------+
```

#Disclaimer
Scraping Websites is illegal in some countries. Please handle this tool with care!

This is a proof of concept and WIP. It may not be suited for production enviroments.

#Used tools

 - JSOUP for HTML parsing
 - Scala combination parsers for parsing the queries
 - Utilities like Guave, Lombok,...
