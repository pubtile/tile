CREATE DATABASE IF NOT EXISTS pubtile_base_test DEFAULT CHARACTER SET = utf8mb4;
use pubtile_base_test;

DROP TABLE IF EXISTS `InvForTesting`;
CREATE TABLE `InvForTesting` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `inventory_no` varchar(32) NOT NULL DEFAULT '' COMMENT '库存编号',
  `sku_id` varchar(20) NOT NULL DEFAULT '' COMMENT '商品编码',
  `location_code` varchar(16) NOT NULL DEFAULT '' COMMENT '库位编码',
  `container_code` varchar(16) NOT NULL DEFAULT '' COMMENT '容器编码 实物容器或虚拟容器',
  `unique_code` varchar(20) NOT NULL COMMENT '唯一码',
  `owner_code` varchar(32) NOT NULL DEFAULT '' COMMENT '货主编码',
  `vendor_code` varchar(20) NOT NULL DEFAULT '' COMMENT '供应商编码',
  `on_hand_qty` int(11) NOT NULL DEFAULT '0' COMMENT '现有数量。可用数量=现有数量+移入数量-分配数量-冻结数量',
  `allocated_qty` int(11) NOT NULL DEFAULT '0' COMMENT '分配数量',
  `in_transit_qty` int(11) NOT NULL DEFAULT '0' COMMENT '移入数量',
  `suspense_qty` int(11) NOT NULL DEFAULT '0' COMMENT '冻结数量',
  `tenant_id` bigint(20) unsigned NOT NULL COMMENT '租户id',
  `md5` varchar(32) NOT NULL DEFAULT '' COMMENT 'md5',
  `entry_order_type` varchar(16) NOT NULL DEFAULT '' COMMENT '入库单据类型',
  `created_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建id',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改id',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `revision` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`) COMMENT '主键',
  KEY `idx_create_time` (`created_time`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_location_code` (`location_code`),
  KEY `idx_container_code` (`container_code`),
  KEY `idx_unique_code` (`unique_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='库存表测试';
