
1.创建数据库名称为lovesound
2.执行以下sql
3.然后执行txlcn-tm项目下resources下的tx-manager.sql
3.修改程序中所有mysql连接密码,Ctrl+H为eclipse的全局搜索快捷键

oauth2的表

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(48) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL COMMENT ''调取Api请求附带令牌，api增删查改等功能，而scopes的值就是all（全部权限），read，write等权限。就是第三方访问资源的一个权限，访问范围'',
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL COMMENT ''如：http://localhost:8005/getHello?code=XXFzbd&state=rOBMf7'',
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL COMMENT ''设置Access Token的存活时间'',
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL COMMENT ''如果autoApprove设置为true的话，客户端就不会进入用户授权页面。对于本系统的客户端就不需要用户确认授权操作'',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ''客户端列表'';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES (''app'', null, ''app'', ''app'', ''password,refresh_token'', null, null, null, null, null, null);
INSERT INTO `oauth_client_details` VALUES (''webApp'', null, ''webApp'', ''app'', ''authorization_code,password,refresh_token,client_credentials'', null, null, null, null, null, null);
INSERT INTO `oauth_client_details` VALUES (''ssoclient'', null, ''ssosecret'', ''all'', ''authorization_code,password,refresh_token,client_credentials'', null, null, null, null, null, "false");





-- ----------------------------
-- Table structure for oauth_access_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication_id` varchar(48) NOT NULL,
  `user_name` varchar(256) DEFAULT NULL,
  `client_id` varchar(256) DEFAULT NULL,
  `authentication` blob,
  `refresh_token` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ''token授权列表'';


-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
   `code` varchar(256) DEFAULT NULL,
   `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for oauth_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token` (
  `token_id` varchar(256) DEFAULT NULL,
  `token` blob,
  `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT ''token refresh刷新列表'';

-- ----------------------------
-- Records of oauth_refresh_token
-- ----------------------------
-- INSERT INTO `oauth_refresh_token` VALUES (''b96e057b4e1c4920428e833db48d4c15'', 0xACED00057372004C6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F6D6D6F6E2E44656661756C744578706972696E674F417574683252656672657368546F6B656E2FDF47639DD0C9B70200014C000A65787069726174696F6E7400104C6A6176612F7574696C2F446174653B787200446F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F6D6D6F6E2E44656661756C744F417574683252656672657368546F6B656E73E10E0A6354D45E0200014C000576616C75657400124C6A6176612F6C616E672F537472696E673B787074002462303132643438372D613930382D343361662D613865662D3533633533393963386264367372000E6A6176612E7574696C2E44617465686A81014B59741903000078707708000001613B74C98E78, 0xACED0005737200416F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F417574683241757468656E7469636174696F6EBD400B02166252130200024C000D73746F7265645265717565737474003C4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F4F4175746832526571756573743B4C00127573657241757468656E7469636174696F6E7400324C6F72672F737072696E676672616D65776F726B2F73656375726974792F636F72652F41757468656E7469636174696F6E3B787200476F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E416273747261637441757468656E7469636174696F6E546F6B656ED3AA287E6E47640E0200035A000D61757468656E746963617465644C000B617574686F7269746965737400164C6A6176612F7574696C2F436F6C6C656374696F6E3B4C000764657461696C737400124C6A6176612F6C616E672F4F626A6563743B787000737200266A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654C697374FC0F2531B5EC8E100200014C00046C6973747400104C6A6176612F7574696C2F4C6973743B7872002C6A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65436F6C6C656374696F6E19420080CB5EF71E0200014C00016371007E00047870737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A65787000000009770400000009737200426F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E617574686F726974792E53696D706C654772616E746564417574686F7269747900000000000001A40200014C0004726F6C657400124C6A6176612F6C616E672F537472696E673B787074000A524F4C455F61646D696E7371007E000D740006617069646F637371007E000D74000C64617461626173652F6C6F677371007E000D74000673797374656D7371007E000D740008757365722F6164647371007E000D74000B757365722F64656C6574657371007E000D740009757365722F656469747371007E000D740009757365722F766965777371007E000D740008757365724C6973747871007E000C707372003A6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E4F41757468325265717565737400000000000000010200075A0008617070726F7665644C000B617574686F72697469657371007E00044C000A657874656E73696F6E7374000F4C6A6176612F7574696C2F4D61703B4C000B726564697265637455726971007E000E4C00077265667265736874003B4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F70726F76696465722F546F6B656E526571756573743B4C000B7265736F7572636549647374000F4C6A6176612F7574696C2F5365743B4C000D726573706F6E7365547970657371007E0024787200386F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E70726F76696465722E426173655265717565737436287A3EA37169BD0200034C0008636C69656E74496471007E000E4C001172657175657374506172616D657465727371007E00224C000573636F706571007E00247870740006776562417070737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170F1A5A8FE74F507420200014C00016D71007E00227870737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F400000000000037708000000040000000274000A6772616E745F7479706574000870617373776F7264740008757365726E616D6574000561646D696E78737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65536574801D92D18F9B80550200007871007E0009737200176A6176612E7574696C2E4C696E6B656448617368536574D86CD75A95DD2A1E020000787200116A6176612E7574696C2E48617368536574BA44859596B8B7340300007870770C000000103F4000000000000174000361707078017371007E0033770C000000103F40000000000000787371007E002A3F40000000000000770800000010000000007870707371007E0033770C000000103F40000000000000787371007E0033770C000000103F40000000000000787372004F6F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E557365726E616D6550617373776F726441757468656E7469636174696F6E546F6B656E00000000000001A40200024C000B63726564656E7469616C7371007E00054C00097072696E636970616C71007E00057871007E0003017371007E00077371007E000B0000000977040000000971007E000F71007E001171007E001371007E001571007E001771007E001971007E001B71007E001D71007E001F7871007E003D737200176A6176612E7574696C2E4C696E6B6564486173684D617034C04E5C106CC0FB0200015A000B6163636573734F726465727871007E002A3F400000000000067708000000080000000271007E002C71007E002D71007E002E71007E002F780070737200326F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657200000000000001A40200075A00116163636F756E744E6F6E457870697265645A00106163636F756E744E6F6E4C6F636B65645A001563726564656E7469616C734E6F6E457870697265645A0007656E61626C65644C000B617574686F72697469657371007E00244C000870617373776F726471007E000E4C0008757365726E616D6571007E000E7870010101017371007E0030737200116A6176612E7574696C2E54726565536574DD98509395ED875B0300007870737200466F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E7573657264657461696C732E5573657224417574686F72697479436F6D70617261746F7200000000000001A4020000787077040000000971007E000F71007E001171007E001371007E001571007E001771007E001971007E001B71007E001D71007E001F787074000561646D696E);



-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id 			int(11) 		not null auto_increment    comment ''用户ID'',
  dept_id 			int(11) 		default null			   comment ''部门ID'',
  login_name 		varchar(30) 	not null 				   comment ''登录账号'',
  user_name 		varchar(30) 	not null 				   comment ''用户昵称'',
  user_type 		varchar(2) 	    default ''00'' 		       comment ''用户类型（00系统用户）'',
  email  			varchar(50) 	default '''' 				   comment ''用户邮箱'',
  phonenumber  		varchar(11) 	default '''' 				   comment ''手机号码'',
  sex  		        char(1) 	    default ''0'' 			   comment ''用户性别（0男 1女 2未知）'',
  avatar            varchar(100) 	default '''' 				   comment ''头像路径'',
  password 			varchar(50) 	default '''' 				   comment ''密码'',
  salt 				varchar(20) 	default '''' 				   comment ''盐加密'',
  status 			char(1) 		default ''0'' 			   comment ''帐号状态（0正常 1停用）'',
  del_flag			char(1) 		default ''0'' 			   comment ''删除标志（0代表存在 2代表删除）'',
  login_ip          varchar(20)     default ''''                 comment ''最后登陆IP'',
  login_date        datetime                                   comment ''最后登陆时间'',
  create_by         varchar(64)     default ''''                 comment ''创建者'',
  create_time 	    datetime                                   comment ''创建时间'',
  update_by         varchar(64)     default ''''                 comment ''更新者'',
  update_time       datetime                                   comment ''更新时间'',
  remark 		    varchar(500) 	default '''' 				   comment ''备注'',
  primary key (user_id)
) engine=innodb auto_increment=100 default charset=utf8 comment = ''用户信息表'';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  106, ''admin'', ''admin'', ''00'', ''admin@qq.com'', ''15888888888'', ''1'', '''', ''29c67a30398638269fe600f73a054934'', ''111111'', ''0'', ''0'', ''127.0.0.1'', ''2018-03-16 11-33-00'', ''admin'', ''2018-03-16 11-33-00'', ''admin'', ''2018-03-16 11-33-00'', ''管理员'');





-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id 			int(11) 		not null auto_increment    comment ''角色ID'',
  role_name 		varchar(30) 	not null 				   comment ''角色名称'',
  role_key 		    varchar(100) 	not null 				   comment ''角色权限字符串'',
  role_sort         int(4)          not null                   comment ''显示顺序'',
  status 			char(1) 		not null 			       comment ''角色状态（0正常 1停用）'',
  create_by         varchar(64)     default ''''                 comment ''创建者'',
  create_time 		datetime                                   comment ''创建时间'',
  update_by 		varchar(64) 	default ''''			       comment ''更新者'',
  update_time 		datetime                                   comment ''更新时间'',
  remark 			varchar(500) 	default '''' 				   comment ''备注'',
  primary key (role_id)
) engine=innodb auto_increment=100 default charset=utf8 comment = ''角色信息表'';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values(''1'', ''管理员'',   ''admin'',  1,  ''0'', ''admin'', ''2018-03-16 11-33-00'', ''admin'', ''2018-03-16 11-33-00'', ''管理员'');
insert into sys_role values(''2'', ''普通角色'', ''common'', 2,  ''0'', ''admin'', ''2018-03-16 11-33-00'', ''admin'', ''2018-03-16 11-33-00'', ''普通角色'');


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id 			int(11) 		not null auto_increment    comment ''菜单ID'',
  menu_name 		varchar(50) 	not null 				   comment ''菜单名称'',
  parent_id 		int(11) 		default 0 			       comment ''父菜单ID'',
  order_num 		int(4) 			default null 			   comment ''显示顺序'',
  url 				varchar(200) 	default ''''				   comment ''请求地址'',
  menu_type 		char(1) 		default '''' 			       comment ''菜单类型（M目录 C菜单 F按钮）'',
  visible 			char(1) 		default 0 				   comment ''菜单状态（0显示 1隐藏）'',
  perms 			varchar(100) 	default '''' 				   comment ''权限标识'',
  icon 				varchar(100) 	default '''' 				   comment ''菜单图标'',
  create_by         varchar(64)     default ''''                 comment ''创建者'',
  create_time 		datetime                                   comment ''创建时间'',
  update_by 		varchar(64) 	default ''''			       comment ''更新者'',
  update_time 		datetime                                   comment ''更新时间'',
  remark 			varchar(500) 	default '''' 				   comment ''备注'',
  primary key (menu_id)
) engine=innodb auto_increment=2000 default charset=utf8 comment = ''菜单权限表'';


-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id 	int(11) not null comment ''用户ID'',
  role_id 	int(11) not null comment ''角色ID'',
  primary key(user_id, role_id)
) engine=innodb default charset=utf8 comment = ''用户和角色关联表'';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values (''1'', ''1'');

-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id 	int(11) not null comment ''角色ID'',
  menu_id 	int(11) not null comment ''菜单ID'',
  primary key(role_id, menu_id)
) engine=innodb default charset=utf8 comment = ''角色和菜单关联表'';







oauth2描述：
spring-oauth项目-
    configadapter包下的
        OAuthConfigurer： 授权方式定义
        SecurityConfiguration： 登录、退出、权限路径设置类
    filter包下的
        MyLoginAuthenticationFilter： 过滤器，用来判断登录方式的
    provider包下的
        都是自定义的类
        LoginSuccessHandler： 登录成功
        MyAuthenticationProvider： 自定的密码（验证码、密码等）验证
        MyCustomUserDetailsService： 账号验证
        MyGrantedAuthority： 自定义权限
    token包下的
        JwtAccessToken： 给jwt增加额外信息
        MyAuthenticationToken： 自定义
        SecurityUser： 实现了UserDetails接口，用户详情提供者
        User： 这个估计可以整合到上面SecurityUser这个类里面，

spring-client项目-
    security包下的
        OAuthConfigurer： 这个是token验证设置类（不同的方式，redis，mysql等）
        SecurityConfiguration： 登录、退出、权限路径设置类
        SecuritySettings： 这个是动态配置类，配置的权限的url，在yml里面
    springoauth.config.token包下的
        这里全是token解析的时候附带信息的类，每一个client都要这几个类