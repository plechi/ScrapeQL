# ScrapeQL
SQL-like query parser to extraxt structured data from websites.

#Why?

Sometimes you need to extract data from HTML. This can be a rather complicated Task, because you have to deal with
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

#Disclaimer
Scraping Websites is illegal in some countries. Please handle this tool with care!

This is a proof of concept and WIP. It may not be suited for production enviroments.

#Used tools

 - JSOUP for HTML parsing
 - ANTLR for parsing the queries
