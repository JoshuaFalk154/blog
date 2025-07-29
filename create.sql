create table likes (created_at date, author_id uuid, id uuid not null, post_id uuid, primary key (id));
create table posts (created_at date, updated_at date, author_id uuid, id uuid not null, body varchar(255), title varchar(255), primary key (id));
create table users (created_at date, updated_at date, id uuid not null, email varchar(255) unique, sub varchar(255) unique, primary key (id));
alter table if exists likes add constraint FKry8tnr4x2vwemv2bb0h5hyl0x foreign key (post_id) references posts;
alter table if exists likes add constraint FK54b369bsw9nhid9w9wmsx151x foreign key (author_id) references users;
alter table if exists posts add constraint FK6xvn0811tkyo3nfjk2xvqx6ns foreign key (author_id) references users;
