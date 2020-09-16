```java 
**密码登录**
http://localhost:8080/oauth/token?username=moon&password=123456&grant_type=password&scope=select&client_id=client_2&client_secret=123456
**client_credentials获取**
http://localhost:8080/oauth/token?grant_type=client_credentials&scope=select&client_id=client_1&client_secret=123456
**切记不能使用静态资源路径**
http://localhost:8080/testResource/1?access_token=4bf0dd12-0ac8-4b3c-a7db-cd67689c6ed3
```
```sql
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `Id` int NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) DEFAULT NULL COMMENT '姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `roles` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'moon', '$2a$10$.z9PiYrXkFlpVJ8ljGZIlOQ8I/Tz0WTvngQfNm3lLoyVUCU7.lusS', 'ROLE_MANAGER,ROLE_USER');
INSERT INTO `user` VALUES ('2', 'admin', '$2a$10$.z9PiYrXkFlpVJ8ljGZIlOQ8I/Tz0WTvngQfNm3lLoyVUCU7.lusS', 'ROLE_ADMIN,ROLE_MANAGER,ROLE_USER');
INSERT INTO `user` VALUES ('3', 'user', '$2a$10$.z9PiYrXkFlpVJ8ljGZIlOQ8I/Tz0WTvngQfNm3lLoyVUCU7.lusS', 'ROLE_USER');
```
