create table filmix.cinema
(
    cinema_id         bigint       not null comment '影院编号'
        primary key,
    cinema_name       varchar(64)  not null comment '影院名称',
    address           varchar(256) not null comment '影院',
    cinema_deprecated tinyint(1)   null comment '影院废弃标识'
)
    comment '影院';

create table filmix.movie
(
    movie_id      bigint                                                 not null comment '电影编号'
        primary key,
    movie_name    varchar(64)                                            null comment '电影名称',
    duration      int                                                    null comment '时长',
    release_time  date                                                   null comment '上映日期',
    poster        varchar(256)                                           null comment '电影海报url',
    language      enum ('EN', 'ZH', 'CAN', 'FR', 'ES', 'AR', 'JA', 'RU') null comment '语言',
    source        varchar(32)                                            null comment '片源地',
    movie_deleted tinyint(1)                                             null comment '电影软删除标识'
)
    comment '电影表';

create table filmix.room
(
    room_id         bigint                                                                  not null comment '影厅编号'
        primary key,
    cinema_id       bigint                                                                  not null comment '所属影院编号',
    room_name       varchar(32)                                                             not null comment '影厅名称',
    ros             int                                                                     null comment '坐位总行数',
    cols            int                                                                     not null comment '坐位总列数',
    room_type       enum ('IMAX', 'Dolby-Cinema', 'Dolby-Atmos', 'DTS', 'CINITY', 'Normal') not null comment '影厅类型',
    support_3D      tinyint(1)                                                              not null comment '是否支持3D',
    room_deprecated tinyint(1)                                                              null comment '影厅废弃标识',
    constraint room_cinema_cinema_id_fk
        foreign key (cinema_id) references filmix.cinema (cinema_id)
)
    comment '影厅表';

create table filmix.tag
(
    tag_id      bigint      not null comment '标签编号'
        primary key,
    tag_name    varchar(32) not null comment '标签名（英文）',
    tag_name_ZH varchar(32) not null comment '标签名（中文）'
)
    comment '电影标签表';

create table filmix.tag_include
(
    movie_id bigint not null comment '电影编号',
    tag_id   bigint not null comment '标签编号',
    primary key (movie_id, tag_id),
    constraint tag_include_movie_movie_id_fk
        foreign key (movie_id) references filmix.movie (movie_id),
    constraint tag_include_tag_tag_id_fk
        foreign key (tag_id) references filmix.tag (tag_id)
)
    comment '电影标签关系表';

create table filmix.ticket
(
    ticket_id    bigint     not null comment '场次编号'
        primary key,
    movie_id     bigint     not null comment '所放映电影编号',
    show_time    datetime   not null comment '放映时间',
    end_time     datetime   not null comment '散场时间',
    cinema_id    bigint     not null comment '所在影院编号',
    room_id      bigint     not null comment '所在影厅编号',
    canceled     tinyint(1) not null comment '是否被取消',
    ticket_price decimal    not null comment '价格',
    constraint ticket_cinema_cinema_id_fk
        foreign key (cinema_id) references filmix.cinema (cinema_id),
    constraint ticket_movie_movie_id_fk
        foreign key (movie_id) references filmix.movie (movie_id),
    constraint ticket_room_room_id_fk
        foreign key (room_id) references filmix.room (room_id)
)
    comment '电影场次表';

create table filmix.user
(
    user_id   bigint                          not null comment '用户编号'
        primary key,
    username  varchar(64)                     not null comment '用户名',
    phone     varchar(20)                     not null comment '用户手机号',
    email     varchar(128)                    not null comment '用户邮箱',
    password  varchar(64)                     not null comment '密码',
    user_type enum ('USER', 'STAFF', 'ADMIN') null comment '用户类型'
)
    comment '用户表';

create table filmix.purchase
(
    purchase_id  bigint                                               not null comment '订单编号'
        primary key,
    user_id      bigint                                               not null comment '所属用户',
    ticket_id    bigint                                               not null comment '电影场次',
    order_status enum ('New', 'Unpaid', 'Paid', 'Closed', 'Refunded') not null comment '订单状态',
    order_price  decimal                                              null comment '支付金额',
    trading_time datetime                                             null comment '交易时间',
    constraint purchase_ticket_ticket_id_fk
        foreign key (ticket_id) references filmix.ticket (ticket_id),
    constraint purchase_user_user_id_fk
        foreign key (user_id) references filmix.user (user_id)
)
    comment '订单记录表';

create table filmix.purchase_log
(
    log_id            bigint auto_increment comment '订单日志id'
        primary key,
    purchase_id       bigint                                               null comment '订单id',
    change_at         datetime                                             null comment '日志记录时间',
    order_status_from enum ('New', 'Unpaid', 'Paid', 'Closed', 'Refunded') null comment '原订单状态',
    order_status_to   enum ('New', 'Unpaid', 'Paid', 'Closed', 'Refunded') null comment '新订单状态',
    note              varchar(64)                                          null comment '日志备注',
    constraint purchase_log_purchase_purchase_id_fk
        foreign key (purchase_id) references filmix.purchase (purchase_id)
);

create table filmix.purchase_seat
(
    purchase_seat_id bigint     not null comment '记录编号'
        primary key,
    purchase_id      bigint     not null comment '订单编号',
    ticket_id        bigint     not null comment '场次编号',
    ro               int        not null comment '行',
    col              int        not null comment '列',
    valid            tinyint(1) not null comment '购买记录是否有效',
    constraint purchase_seat_purchase_purchase_id_fk
        foreign key (purchase_id) references filmix.purchase (purchase_id),
    constraint purchase_seat_ticket_ticket_id_fk
        foreign key (ticket_id) references filmix.ticket (ticket_id)
)
    comment '订单所购买的座位记录表';
#视图
create view filmix.now_showing_movie as
select `M`.`movie_id`                 AS `movie_id`,
       any_value(`M`.`movie_name`)    AS `movie_name`,
       any_value(`M`.`duration`)      AS `duration`,
       any_value(`M`.`release_time`)  AS `release_time`,
       any_value(`M`.`poster`)        AS `poster`,
       any_value(`M`.`language`)      AS `language`,
       any_value(`M`.`source`)        AS `source`,
       any_value(`M`.`movie_deleted`) AS `movie_deleted`
from `filmix`.`movie` `M`
where (`M`.`movie_id` in (select `filmix`.`ticket`.`movie_id`
                          from `filmix`.`ticket`
                          where ((`filmix`.`ticket`.`show_time` >= now())
                              and (`filmix`.`ticket`.`canceled` = false))
                          group by `filmix`.`ticket`.`movie_id`) and (`M`.`movie_deleted` = false));

create trigger filmix.new_purchase_trigger
    after insert
    on filmix.purchase
    for each row
    INSERT INTO purchase_log (purchase_id, change_at, order_status_from, order_status_to, note) VALUES(NEW.purchase_id,NOW(),'New','Unpaid',NULL);
