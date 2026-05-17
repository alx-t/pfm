create table if not exists accounts (
    id          serial primary key,
    title       varchar(50) not null check (length(trim(title)) >=3),
    acc_type    smallint not null,
    currency    varchar(3) not null check (length(trim(title)) =3),
    amount      numeric(15,6) default 0,
    amount_curr numeric(15,6) default 0,
    user_id     varchar(36) not null
);
