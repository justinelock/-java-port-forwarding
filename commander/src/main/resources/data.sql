replace into users (id, name, password, salt, accesstoken, rand, last_login_time, last_login_ip) values (1, 'admin', 'db01b39338fe96e8abaa773770a625c2', 'VH3940Y92YUIHW', '', '', 1514469634632, '192.168.1.1');
replace into hosts (id, user_id, name, state, accesstoken, ip, last_active_time) values (1, 0, '家里的台式机', 1, 'yCxok5PPO3LB8G6M317SkJ4fgHss7J0J4EN9gRF5xB2y8guAnhrMSD0vNJFIv5Zu', null, 0);
