create database app CHARACTER SET utf8mb4;
use app;

drop table if exists user_info;
CREATE TABLE `user_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(50) NOT NULL COMMENT '用户名',
  `pwd` varchar(50) NOT NULL COMMENT '密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 comment '管理员表';
insert into user_info(user_name,pwd) values ('user1','123456');
insert into user_info(user_name,pwd) values ('user2','123456');
insert into user_info(user_name,pwd) values ('user3','123456');
insert into user_info(user_name,pwd) values ('user4','123456');