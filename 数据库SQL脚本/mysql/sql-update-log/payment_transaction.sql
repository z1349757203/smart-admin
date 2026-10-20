-- ----------------------------
-- 支付交易流水表
-- 用于记录各聚合支付平台（连连国际、PingPong、汇付天下等）的支付/退款流水
-- ----------------------------
DROP TABLE IF EXISTS `t_payment_transaction`;
CREATE TABLE `t_payment_transaction`
(
    `transaction_id`   bigint         NOT NULL AUTO_INCREMENT COMMENT '交易流水id',
    `channel_code`     int            NOT NULL COMMENT '支付渠道 1连连国际 2PingPong 3汇付天下',
    `trade_type`       int            NOT NULL COMMENT '交易类型 1支付 2退款',
    `out_trade_no`     varchar(64)    NOT NULL COMMENT '商户系统内部订单号',
    `channel_trade_no` varchar(128)   DEFAULT NULL COMMENT '渠道交易号',
    `out_refund_no`    varchar(64)    DEFAULT NULL COMMENT '商户系统内部退款单号',
    `channel_refund_no` varchar(128)  DEFAULT NULL COMMENT '渠道退款单号',
    `subject`          varchar(128)   DEFAULT NULL COMMENT '订单标题',
    `total_amount`     decimal(18, 2) DEFAULT NULL COMMENT '交易总金额',
    `refund_amount`    decimal(18, 2) DEFAULT NULL COMMENT '退款金额',
    `currency`         varchar(8)     DEFAULT NULL COMMENT '币种',
    `status`           int            NOT NULL DEFAULT 0 COMMENT '交易状态 0待支付 1支付中 2成功 3失败 4已关闭 5退款中 6已退款',
    `paid_time`        datetime       DEFAULT NULL COMMENT '支付完成时间',
    `client_ip`        varchar(64)    DEFAULT NULL COMMENT '客户端ip',
    `notify_url`       varchar(256)   DEFAULT NULL COMMENT '异步回调地址',
    `return_url`       varchar(256)   DEFAULT NULL COMMENT '同步返回地址',
    `channel_response` text           DEFAULT NULL COMMENT '渠道原始响应报文',
    `extra_data`       text           DEFAULT NULL COMMENT '扩展数据(json)',
    `remark`           varchar(256)   DEFAULT NULL COMMENT '备注',
    `deleted_flag`     tinyint(1)     NOT NULL DEFAULT 0 COMMENT '删除标识',
    `create_user_id`   bigint         DEFAULT NULL COMMENT '创建人id',
    `update_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_time`      datetime       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`transaction_id`),
    KEY `idx_out_trade_no` (`out_trade_no`),
    KEY `idx_channel_trade_no` (`channel_trade_no`),
    KEY `idx_channel_status` (`channel_code`, `status`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='支付交易流水表';
