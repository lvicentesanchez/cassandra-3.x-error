CREATE KEYSPACE IF NOT EXISTS db WITH replication = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

CREATE TABLE IF NOT EXISTS db.measure_error (
  id TEXT,
  value INT,
  PRIMARY KEY (id)
);

INSERT INTO db.measure_error (id, value ) VALUES ( '1', 11 );
INSERT INTO db.measure_error (id, value ) VALUES ( '2', 7 );
INSERT INTO db.measure_error (id, value ) VALUES ( '3', 23 );

CREATE TABLE IF NOT EXISTS db.measure_ok (
  id TEXT,
  entry INT,
  value INT,
  PRIMARY KEY (id, entry)
);

INSERT INTO db.measure_ok (id, entry, value ) VALUES ( '1', 0, 11 );
INSERT INTO db.measure_ok (id, entry, value ) VALUES ( '2', 0, 7 );
INSERT INTO db.measure_ok (id, entry, value ) VALUES ( '3', 0, 23 );
