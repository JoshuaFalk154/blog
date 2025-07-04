create sequence posts_seq start with 1 increment by 50;
create sequence users_seq start with 1 increment by 50;
create table posts (author_id bigint, created_at timestamp(6), id bigint not null, updated_at timestamp(6), sub varchar(255) unique, username varchar(255) unique, primary key (id));
create table users (created_at timestamp(6), id bigint not null, updated_at timestamp(6), sub varchar(255) unique, username varchar(255) unique, primary key (id));
alter table if exists posts add constraint FK6xvn0811tkyo3nfjk2xvqx6ns foreign key (author_id) references users;
