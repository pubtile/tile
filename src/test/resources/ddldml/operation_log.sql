-- ----------------------------
-- Table structure for operation
-- ----------------------------
DROP TABLE IF EXISTS `operation`;
CREATE TABLE `operation` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `app_name` varchar(500) DEFAULT NULL,
  `object_name` varchar(500) NOT NULL DEFAULT '',
  `object_id` varchar(500) NOT NULL DEFAULT '',
  `operator` varchar(500) NOT NULL,
  `operation_name` varchar(500) NOT NULL DEFAULT '',
  `operation_alias` varchar(500) NOT NULL DEFAULT '',
  `extra_words` varchar(5000) DEFAULT NULL,
  `comment` mediumtext,
  `operation_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `app_name` (`app_name`) USING HASH,
  KEY `object_name` (`object_name`) USING HASH,
  KEY `object_id` (`object_id`) USING BTREE
);

-- ----------------------------
-- Table structure for attribute
-- ----------------------------
DROP TABLE IF EXISTS `attribute`;
CREATE TABLE `attribute` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `operation_id` bigint(20) unsigned NOT NULL,
  `attribute_type` varchar(500) NOT NULL DEFAULT '',
  `attribute_name` varchar(500) NOT NULL DEFAULT '',
  `attribute_alias` varchar(500) NOT NULL DEFAULT '',
  `old_value` mediumtext,
  `new_value` mediumtext,
  `diff_value` mediumtext,
  PRIMARY KEY (`id`),
  KEY `operation_id` (`operation_id`) USING BTREE
);