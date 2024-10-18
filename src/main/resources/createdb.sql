create type payment_type_enum as enum ('CASH', 'CARD', 'TRANSFER');

create table seller
(
  id                 serial primary key,
  name               varchar(30) not null,
  contact_info       varchar(30) unique not null,
  registration_date  timestamp not null default now()
);

create table transaction
(
  id                serial primary key,
  seller_id         int not null,
  amount            decimal(10, 2) not null,
  payment_type      payment_type_enum not null,
  transaction_date  timestamp not null default now(),
  constraint "fk_transaction_seller" foreign key (seller_id) references seller(id) on delete restrict on update restrict
);