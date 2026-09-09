drop table item_ja
;
drop table product_ja
;
drop table category_ja
;

create table category_ja (
    catid char(10) not null,
    name varchar(80) null,
    descn varchar(255) null,
    constraint pk_category_ja primary key (catid)
)
;

create table product_ja (
    productid char(10) not null,
    category char(10) not null,
    name varchar(80) null,
    descn varchar(255) null,
    constraint pk_product_ja primary key (productid),
        constraint fk_product_1_ja foreign key (category)
        references category_ja (catid)
)
;

create table item_ja (
    itemid char(10) not null,
    productid char(10) not null,
    listprice decimal(10,2) null,
    unitcost decimal(10,2) null,
    supplier int null,
    status char(2) null,
    attr1 varchar(80) null,
    attr2 varchar(80) null,
    attr3 varchar(80) null,
    attr4 varchar(80) null,
    attr5 varchar(80) null,
    constraint pk_item_ja primary key (itemid),
        constraint fk_item_1_ja foreign key (productid)
        references product_ja (productid),
        constraint fk_item_2_ja foreign key (supplier)
        references supplier (suppid)
)
;
