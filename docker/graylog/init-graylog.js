db = db.getSiblingDB('graylog'); // létrehozza a graylog adatbázist
db.createUser({
  user: "graylog",
  pwd: "strongpassword",
  roles: [{ role: "readWrite", db: "graylog" }]
});