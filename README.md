s## Simple Anagrammer

A learning experiment with Scala and the [Cask Framework](http://www.lihaoyi.com/cask/index.html)

Runs on port 8080 by default

Execute SQL scripts against Postgres Docker:

cat createLochLomond.sql | docker exec -i postgres1 psql -U overbeck
cat populateLochLomond.sql | docker exec -i postgres1 psql -U overbeck