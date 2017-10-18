# lembrae
A shared memo keeper made with React and Java as a Backend

script de alteração:
```mysql
ALTER TABLE users MODIFY password CHAR(128);
ALTER TABLE users ADD COLUMN salt CHAR(32) AFTER username;
ALTER TABLE users ADD COLUMN avatar VARCHAR (35);
ALTER TABLE notes ADD COLUMN last_user VARCHAR(20);
ALTER TABLE notes ADD COLUMN commentary VARCHAR(2048) DEFAULT '{ "commentary" : []} ';
ALTER TABLE notes ADD COLUMN concluded TINYINT(1) NOT NULL;
```




