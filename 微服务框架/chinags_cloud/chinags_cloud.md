# post:
| 服务     | 服务名           | 端口号    | 地址      |  位置       |
| -------- | --------------- | -----    | -----       |---:        |                
| eureka   |  注册中心       | 9900      |  10.0.2.1 | D:\cloud\chinags_eureka-1.0-SNAPSHOT.jar
| zuul     |  网关           | 9901     |   10.0.2.1 |D:\cloud\chinags_zuul-1.0-SNAPSHOT.jar
| zipkin   |  链路追踪器      | 9921     |   10.0.2.1 |D:\cloud\zip-1.0-SNAPSHOT.jar
| admin   |  服务监控      | 9902     |   10.0.2.1 |D:\cloud\chinags_admin-1.0-SNAPSHOT.jar
| auth     |  认证中心        | 9912     |   10.0.2.3 | D:\Jdds\auth\service_auth-1.0-SNAPSHOT.jar
| auth前台     |  用户        | 9010     |   10.0.2.3 | D:\Jdds\authWeb\authWeb.jar
| 数据资源共享平台  |  数据资源共享平台  | 9012 |  10.0.2.3 | E:\qtdbra\qtdbra.jar
| dervice  |  设备基础维护中心 | 9920     |   10.0.2.7 | 
| dervice前台  |  设备维护中心 | 9010     |   10.0.2.7  | 
| actuator  |  监控 健康检查     | 9905     |   10.0.2.1 | D:\cloud\chinags_actuator-1.0-SNAPSHOT.jar
| turbine  |   监控           | 9905     |   10.0.2.1 | D:\cloud\chinags_turbine-1.0-SNAPSHOT.jar
| dbra  |   数据           | 9913     |   10.0.2.3 | D:\Jdds\dbra\service_dbra-1.0-SNAPSHOT.jar

##访问路径(具体访问路径端口好参考post)：
| 名称     | 访问路径           | 服务    |  地址 |
| -------- | --------------- | -----:    |---------|
| 注册中心   |  http://ip:端口号/       | eureka      |     10.0.2.1 10.0.2.2 
| hystrix  | http://ip:端口号/hystrix  | zuul        |     10.0.2.1 
| zipkin    | http://ip:端口号/        | zipkin      |     10.0.2.1
| 服务监控    | http://ip:端口号/      |admin          |    10.0.2.1