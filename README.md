# gmall
谷粒商城

9002 http
jestclielnt 非官方，更新慢
resttemplate 模拟http请求，es很多操作需要自己封装，麻烦
elasticsearch-rest-cilent：官方restclient，封装了es操作，api层级分明
------------------------------------------------------
1.启动nacos
F:\nacos\谷粒商城\资料\Nacos\nacos\bin\startup.cmd
1.启动elasticsearch 
D:\Program Files\elasticsearch\elasticsearch-6.8.13\bin\elasticsearch.bat


启动 kibana
D:\Program Files\kibana-6.8.13-windows-x86_64\bin\kibana.bat

分布式事务支持，需启动
F:\nacos\谷粒商城\资料\seata\seata-server\bin\seata-server.bat
同步启动nginx D:\Program Files\nginx-1.17.6\nginx.exe

-------------------------------------------------
项目用：
elastic6.8.13 
spring-boot-start-data-elasticsearch 2.2.7
spring-data-elasticsearch 3.2.7
spring-boot 2.2.7

有关spring-boot-start-data-elasticsearch版本(包含spring-data-elasticsearch)
   spirng-data-elasticsearch
   elasticsearch
   spring-boot 
   四个版本规则参见：https://docs.spring.io/spring-data/elasticsearch/docs/4.1.5/reference/html/#preface.versions
-----------------------------------------------------------------------------------------------------------------

 ## 待扩展：    
    seata是否可以可启用，可关闭，做到灵活可控
 
