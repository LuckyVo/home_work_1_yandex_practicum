drop schema if exists my_blog cascade;

create schema if not exists my_blog;

create table if not exists my_blog.comment
(
    id      bigint primary key GENERATED ALWAYS AS IDENTITY not null,
    text    varchar                                         not null,
    post_id bigint                                          not null
);

create table if not exists my_blog.tag
(
    id      bigint primary key GENERATED ALWAYS AS IDENTITY not null,
    title   varchar                                         not null,
    post_id bigint                                          not null
);

create table if not exists my_blog.post
(
    id                bigint primary key GENERATED ALWAYS AS IDENTITY not null,
    title             varchar,
    text              varchar,
    imagePath         text,
    likesCount        int,
    comment_unique_id bigint,
    constraint comment_fkey foreign key (comment_unique_id)
        references my_blog.comment (post_id) match simple
        on update cascade on delete cascade,
    tag_unique_id     bigint,
    constraint tag_fkey foreign key (tag_unique_id)
        references my_blog.tag (post_id) match simple
        on update cascade on delete cascade
);