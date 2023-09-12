-- drop all foreign keys
alter table url_checks drop constraint if exists fk_url_checks_url_id;
drop index if exists ix_url_checks_url_id;

-- drop all
drop table if exists url_checks;

drop table if exists urls;

