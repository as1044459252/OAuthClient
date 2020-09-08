create table Customer(
uid int(20) primary KEY auto_increment,
username varchar(30) not null,
password varchar(30) not null,
tel varchar(20) not null,
state varchar(10) default null,
address varchar(100) default null
)engine=InnoDB default charset=utf8;

