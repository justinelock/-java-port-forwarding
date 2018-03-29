## java-port-forwarding
> Across the intranet we can reach every corner in the world.

通过服务器，转发内网主机的TCP数据包，由服务器作为中间人，完成客户端到主机端的通信。

为`动态IP拨号上网`或`无网络管理权限`的主机提供对外服务，借以实现内网穿透。

加密转发数据，可避开路由器对端口及协议的封禁。

## 模块说明
### 名词解说
* 主机端：需要被转发服务的电脑端
* 服务器端：提供转发服务
* 客户端：任意设备

比如，出差在外，需要通过手机（客户端）访问位于家中的一台台式机（主机端），可以通过一台服务器，来转发两者间的数据通信。

### commander 服务器端
* 内置SpringBOOT，提供了主机管理与端口转发配置，默认访问地址：http://serverip:8888。
* 使用sqlite数据库
* 在`${server.command.port}`上开启监听，负责下发心跳测试包及指令到主机端。
* 在`${server.forward.port}`上开启监听，在服务器端下发转发指令后，主机端将连接到指令的端口上，然后同时也连接到服务器的此端口上，并将两者的输入输出流对接起来，以完成转发。

### messenger 主机端
* 无任何依赖
* 可转发本机或局域网内其它任何机器的TCP数据包

### 设置

## 快速开始
1. 按需修改两个模块下的配置文件，比如几个端口及主机端的服务器地址等。
2. 使用Maven打包，`mvn package`，将会在`target`目录下分别生成服务器端`original-commander-1.0-SNAPSHOT.jar`和主机端`messenger-1.0-SNAPSHOT.jar`。
3. 在拥有固定IP的服务器端执行`java -jar original-commander-1.0-SNAPSHOT.jar`。
4. 在主机端执行`java -jar messenger-1.0-SNAPSHOT.jar`，如果需要后台运行，可执行`javaw -jar messenger-1.0-SNAPSHOT.jar`。
5. 使用初始账号`admin`及密码`123456`登陆http://serverip:8888/，进行主机端的端口转发管理。
6. **别忘了修改初始登陆密码。**

### 指令数据包结构
* FA FA FA 协议头
* 00 00 00 00 加密后的数据体长度
* 00 00 00 00 主机ID
* 00 00 指令，最高2位用于描述加密类型，01表示DES加密，后14位表示指令
* ...... DES加密后的数据体

### 转发数据包结构
* FA FA FA 协议头
* 00 00 00 00 数据包长度
* ........... DES加密后的数据体