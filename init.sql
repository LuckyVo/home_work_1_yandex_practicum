drop schema if exists my_blog cascade;

create schema if not exists my_blog;

create table if not exists my_blog.comment
(
    id   bigint primary key GENERATED ALWAYS AS IDENTITY not null,
    text varchar                                         not null
);

create table if not exists my_blog.tag
(
    id    bigint primary key GENERATED ALWAYS AS IDENTITY not null,
    title varchar                                         not null
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
        references my_blog.comment (id) match simple
        on update cascade on delete cascade,
    tag_unique_id     bigint,
    constraint tag_fkey foreign key (tag_unique_id)
        references my_blog.tag (id) match simple
        on update cascade on delete cascade
);

create table if not exists my_blog.posts_tags
(
    post_id                bigint  not null,
    tag_id                bigint  not null,
    constraint pk_posts_tags primary key (post_id, tag_id),
    constraint fk_posts_tags_on_post foreign key (post_id) references my_blog.post (id) on delete cascade,
    constraint fk_posts_tags_on_tag foreign key (tag_id) references my_blog.tag (id) on delete cascade

)