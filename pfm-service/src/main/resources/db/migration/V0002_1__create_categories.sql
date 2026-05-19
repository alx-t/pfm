create table if not exists categories (
    id              serial primary key,
    title           varchar(50) not null check (length(trim(title)) >=3),
    operation_type  smallint not null,
    user_id         varchar(36) not null
);

create index idx_accounts_user_id on accounts using hash (user_id);
create index idx_categories_user_id on categories using hash (user_id);

