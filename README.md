# springbootsecurity
sql

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
