create table bank_slip (
  id bigserial not null,
  uuid varchar(255) not null,
  customer varchar(255) not null,
  due_date date not null,
  payment_date date,
  total_in_cents numeric not null,
  status varchar(8) not null,
  primary key (id)
);