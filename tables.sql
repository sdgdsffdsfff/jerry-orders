CREATE TABLE `orders` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `totalfee` decimal(11,2) unsigned NOT NULL COMMENT '订单金额+物流费',
  `orderfee` decimal(11,2) unsigned NOT NULL COMMENT '订单金额',
  `deliveryfee` decimal(11,2) unsigned NOT NULL COMMENT '物流费',
  `rebate` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '返利支付金额',
  `giftcard` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '充值卡支付金额',
  `recharge` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '充值支付金额',
  `direct` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '直接支付金额',
  `payed` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '实际支付金额',
  `reduce` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '优惠减免金额',
  `credit` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '余额支付',
  `outmoney` decimal(11,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '请求第三方支付金额',
  `mobile` varchar(30)  NOT NULL COMMENT '下单手机号',
  `ordertime` int(10) unsigned NOT NULL COMMENT '下单时间',
  `orderip` long(10) unsigned NOT NULL COMMENT '下单时的IP地址',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '支付时间',
  `paytype` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付方式',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '订单状态：0已下单 16部分支付 32全额支付 51备货中 52已发货 53已签收 64已退款 128 已取消',
  `attributes` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '订单属性',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`,`status`),
  KEY `idx_ordertime` (`ordertime`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='订单';

CREATE TABLE `cart` (
  `id` bigint(20) unsigned not null COMMENT '自增ID' AUTO_INCREMENT PRIMARY KEY,
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `itemid` bigint(20) NOT NULL DEFAULT 0 COMMENT 'itemid',
  `skuid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'skuid',
  `tid` varchar(128) NOT NULL DEFAULT '' COMMENT '唯一id，标示未登录用户',
  `quantity` int(10) unsigned NOT NULL COMMENT '商品数量',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0',
  `modtime` int(10) unsigned NOT NULL DEFAULT '0',
  KEY `idx_userid_itemid` (`userid`, `itemid`),
  KEY `skuid` (`skuid`),
  KEY `tid` (`tid`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='购物车表';


CREATE TABLE `deliveryinfo` (
  `orderid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `address` TEXT NOT NULL COMMENT '序列化的地址信息',
  `invoicetype` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '发票类型 0不开发票 1个人 2公司',
  `invoicecomment` varchar(1024) NOT NULL DEFAULT '' COMMENT '发票备注',
  `deliverycomment` varchar(1024) NOT NULL DEFAULT '' COMMENT '发货备注',
  PRIMARY KEY (`orderid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单发货信息';

CREATE TABLE `credit` (
  `userid` bigint(10) unsigned NOT NULL,
  `value` decimal(11,2) NOT NULL COMMENT '余额',
  `rebate` decimal(11,2) NOT NULL COMMENT '返利余额',
  `giftcard` decimal(11,2) NOT NULL COMMENT '充值卡余额',
  `recharge` decimal(11,2) NOT NULL COMMENT '充值余额',
  `direct` decimal(11,2) NOT NULL COMMENT '支付余额',
  `modtime` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户余额表';

CREATE TABLE `creditlog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(10) unsigned NOT NULL,
  `type` tinyint(3) unsigned NOT NULL COMMENT '类型',
  `objid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '类型对应的对象id',
  `value` decimal(11,2) NOT NULL COMMENT '金额 扣除用负数 存入用正数',
  `balance` decimal(11,2) NOT NULL COMMENT '余额',
  `rebate` decimal(11,2) NOT NULL COMMENT '返利金额',
  `giftcard` decimal(11,2) NOT NULL COMMENT '充值卡充值金额',
  `recharge` decimal(11,2) NOT NULL COMMENT '直接充值金额',
  `direct` decimal(11,2) NOT NULL COMMENT '针对订单支付金额',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `objid` (`objid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='余额流水表';

CREATE TABLE `notifylog` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `objid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '对象id，购买时为订单id，充值时为用户id',
  `outno` varchar(128) NOT NULL COMMENT '业务方唯一批次号',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付类型，0为购买，1为充值',
  `paytype` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付类型',
  `money` decimal(11,2) NOT NULL COMMENT '付款金额',
  `tradeno` varchar(128) NOT NULL COMMENT '支付平台的交易序列号',
  `buyer` varchar(255) NOT NULL COMMENT '支付平台用户识别信息',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '支付成功时间',
  `result` varchar(128) NOT NULL COMMENT '返回值',
  `sign` varchar(256) NOT NULL COMMENT '签名',
  `status` tinyint(3) unsigned NOT NULL COMMENT '处理状态：0 新建 64 处理成功  128 处>理失败',
  `error` varchar(64) NOT NULL COMMENT '失败原因',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0',
  `modtime` int(10) unsigned NOT NULL DEFAULT '0',
  `data` TEXT COMMENT '其他信息',
  PRIMARY KEY (`id`),
  KEY `objid` (`objid`),
  KEY `status` (`status`),
  KEY `outno` (`outno`,`paytype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方支付通知信息列表';

CREATE TABLE `notifysuccesslog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(10) unsigned NOT NULL COMMENT '用户id',
  `orderid` bigint(10) unsigned NOT NULL DEFAULT '0' COMMENT '充值时为0',
  `outno` varchar(128) NOT NULL COMMENT '发给支付平台的订单号',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付类型，0为购买，1为充值',
  `paytype` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付类型',
  `money` decimal(11,2) NOT NULL COMMENT '付款金额',
  `tradeno` varchar(128) NOT NULL COMMENT '支付平台的交易序列号',
  `buyer` varchar(255) NOT NULL COMMENT '支付平台用户识别信息email',
  `paytime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '支付成功时间',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0',
  `modtime` int(10) unsigned NOT NULL DEFAULT '0',
  `data` Text NOT NULL COMMENT '其他信息',
  `refund2third` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '已经退回到第三方的金额',
  `refund2credit` decimal(11,2) NOT NULL DEFAULT '0.00' COMMENT '已经退回到账户的金额',
  PRIMARY KEY (`id`),
  UNIQUE KEY `outno` (`outno`,`paytype`),
  KEY `orderid` (`orderid`),
  KEY `userid` (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='处理成功的notifylog';

CREATE TABLE `orderitems` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(10) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `orderid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `itemid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `skuid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'skuid',
  `quantity` int(10) unsigned NOT NULL COMMENT '商品数量',
  `buyprice` decimal(11,2) unsigned NOT NULL COMMENT '进价',
  `saleprice` decimal(11,2) unsigned NOT NULL COMMENT '售价',
  PRIMARY KEY (`id`),
  KEY `orderid` (`id`),
  KEY `itemid` (`itemid`),
  KEY `skuid` (`skuid`),
  KEY `idx_userid_itemid`(`userid`, `itemid`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='订单组合';

CREATE TABLE `refund` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户id',
  `orderid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单id',
  `paytype` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '支付类型',
  `money` decimal(11,2) DEFAULT '0.00' COMMENT '退款总金额',
  `recharge` decimal(11,2) DEFAULT '0.00' COMMENT '充值金额',
  `direct` decimal(11,2) DEFAULT '0.00' COMMENT '支付金额',
  `tradeno` varchar(128) NOT NULL COMMENT '支付平台的交易序列号',
  `outno` varchar(128) NOT NULL COMMENT '业务方唯一批次号',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '退款类型 0默认 1重复支付退款',
  `method` tinyint(3) unsigned NOT NULL COMMENT '退款方式：0:原路退回第三方 1:退回指定银行卡',
  `reason` int(10) unsigned NOT NULL COMMENT '退款理由',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '0:新申请 2:已拒绝 4:审核通过代付款 6：已付款待结果 8:退款成功 10:退款失败 12：充退失败',
  `deliveryinfo` TEXT NOT NULL COMMENT '用户填写的物流信息',
  `comment` TEXT NOT NULL COMMENT '备注',
  `sendtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '发送时间',
  `succtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '成功时间',
  `failtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '失败时间',
  `addtime` int(10) DEFAULT '0' COMMENT '创建时间',
  `modtime` int(10) DEFAULT '0' COMMENT '最后更新时间',
  `attributes` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '退款属性',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `orderid_paytype` (`orderid`,`paytype`),
  KEY `outno` (`outno`),
  KEY `tradeno` (`tradeno`),
  KEY `status_paytype` (`status`,`paytype`),
  KEY `sendtime` (`sendtime`),
  KEY `succtime` (`succtime`),
  KEY `failtime` (`failtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款表';

CREATE TABLE `refundlog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `refundid` int(10) unsigned NOT NULL COMMENT '退款id',
  `opid` int(10) unsigned NOT NULL COMMENT '操作人id',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '类型',
  `comment` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `refundid` (`refundid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款操作记录';

CREATE TABLE `deliverycompany` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(60) NOT NULL DEFAULT '' COMMENT '快递公司名称',
  `code` varchar(30) NOT NULL DEFAULT '' COMMENT '快递公司拼音简写',
  `website` varchar(1024) NOT NULL DEFAULT '' COMMENT '官网地址',
  `phone` varchar(128) NOT NULL DEFAULT '' COMMENT '官网客服电话',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '启用状态：0启用 1禁用',
  `sort` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '显示优先级',
  PRIMARY KEY (`id`),
  KEY `idx_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `feedback` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `userid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '用户ID',
  `itemid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '商品id',
  `orderid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '订单id',
  `skuid` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '单品id',
  `score` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '产品评分',
  `comment` text NOT NULL COMMENT '具体评价',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '管理状态',
  `attributes` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '属性位图',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '创建时间',
  `modtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `itemid` (`itemid`),
  KEY `orderid_skuid` (`skuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评价表';

create table `ordertrace` (
  `id` int(10) unsigned not null auto_increment primary key,
  `orderid` bigint(20) unsigned not null default '0' comment '订单ID',
  `deliveryCompanPinyin` varchar(30) not null default '' comment '物流公司拼音简写',
  `deliveryNum` varchar(128) not null default '' comment '运单号码',
  `status` tinyint(3) unsigned not null default '0' comment '跟踪状态',
  `data` Text not null comment '跟踪数据',
  `querytimes` int(10) unsigned not null default '0' comment '查询次数',
  `lastqtime` int(10) unsigned not null default '0' comment '上次查询时间',
  `addtime` int(10) unsigned not null default '0' comment '创建时间',
  `modtime` int(10) unsigned not null default '0' comment '更新时间',
  key `idx_orderid` (`orderid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT '订单物流跟踪';

CREATE TABLE `refunditems` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `refundid` int(10) unsigned NOT NULL COMMENT '退款ID',
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `orderid` bigint(20) unsigned NOT NULL COMMENT '订单ID',
  `itemid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `skuid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'skuid',
  `quantity` int(10) unsigned NOT NULL COMMENT '商品数量',
  `money` decimal(11,2) DEFAULT '0.00' COMMENT '实退金额',
  `status` tinyint(3) default '0' COMMENT '状态：0默认 1退款成功',
  PRIMARY KEY (`id`),
  KEY `orderid` (`id`),
  KEY `itemid` (`itemid`),
  KEY `skuid` (`skuid`),
  KEY `refundid` (`refundid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='退款sku表';

CREATE TABLE `purchase_restriction_policy` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `itemid` INT(11) NOT NULL COMMENT '商品编号',
  `type` varchar(50) NOT NULL COMMENT '政策类型',
  `title` text NOT NULL COMMENT '政策标题',
  `desc` text NOT NULL COMMENT '政策详情',
  `data` text NOT NULL COMMENT '数据',
  PRIMARY KEY (`id`),
  unique key `uidx_itemid` (`itemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='限购政策';

CREATE TABLE `freeorder` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `freeFlashid` int(10) unsigned NOT NULL COMMENT '众测活动id',
  `userid` bigint(20) unsigned NOT NULL COMMENT '用户ID',
  `itemid` bigint(20) unsigned NOT NULL COMMENT '商品ID',
  `skuid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'skuid',
  `status` tinyint(3) default '0' COMMENT '状态：0新申请 5审核通过 10审核拒绝 20已发货 30已签收 40评测编辑中 50评测完成 60评测审核通过 70评测审核拒绝',
  `applyip` bigint(10) unsigned NOT NULL COMMENT '申请时的IP地址',
  `deliveryInfo` TEXT not null COMMENT '物流信息',
  `addressInfo` TEXT not null COMMENT '地址信息',
  `addtime` int(10) unsigned not null default '0' comment '申请时间',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `itemid` (`itemid`),
  KEY `skuid` (`skuid`),
  KEY `freeflashid` (`freeFlashid`),
  KEY `addtime` (`addtime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='众测订单表';

CREATE TABLE `freeorderlog` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `freeOrderId` int(10) unsigned NOT NULL COMMENT '退款id',
  `opid` int(10) unsigned NOT NULL COMMENT '操作人id',
  `type` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '类型',
  `comment` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `addtime` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '生成时间',
  PRIMARY KEY (`id`),
  KEY `freeOrderId` (`freeOrderId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='众测订单操作记录';