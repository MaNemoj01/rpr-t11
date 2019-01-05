CREATE TABLE drzava (
  id int primary key,
  naziv text,
  glavni_grad int references grad(id) on delete cascade

);