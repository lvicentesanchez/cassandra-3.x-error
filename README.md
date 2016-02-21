# cassandra-3.x-error #

This repository reproduces an error in Cassandra 3.x when the fetch size is smaller than the number of rows in a column family. For the shake of this demo, fetch size would be 2 and the number of rows 3.

## Pre-steps ##

1. Use `src/main/cql/script.cql` to create the key space and column families.
2. Modify contact point and port in `src/main/scala/CassandraError.scala`.
3. Modify contact point and port in `src/main/scala/CassandraOk.scala`.
4. Open `sbt` by executing `./sbt` in the root of the project.

## Error ##

Execute `runMain CassandraError`. We would get this in our terminal:

```
[info] Measure(1,11)
[info] Measure(2,7)
[info] Measure(2,7)
[info] Measure(3,23)
[info] Measure(3,23)
```

If we run the same program against Cassandra 2.x, we would get this:

```
[info] Measure(1,11)
[info] Measure(2,7)
[info] Measure(3,23)
```

We were expecting Cassandra 3.x to produce the same output as Cassandra 2.x. It seems that there is an issue with simple primary keys and pagination.

## Ok ##

Execute `runMain CassandraOk`. Whether we ran it against Cassandra 2.x or Cassandra 3.x, we would get this in our terminal:

```
[info] Measure(1,0,11)
[info] Measure(2,0,7)
[info] Measure(3,0,23)
```
