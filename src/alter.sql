ALTER TABLE grad
  ADD column drzava int references drzava(id) on delete cascade;