# 自动估价系统后台-估价业务模块
### 部署准备
1. 服务器3台 CPU核数：8核  内存32G  硬盘: 1TB 操作系统:CentOS Server  
2. 数据库服务器，3台，Mysql【数据库集群（主从热备，多副本多分片，降低数据库风险），3台服务器组建最小集群】   CPU核数：8核  内存32G  硬盘: 1TB 操作系统:CentOS Server
3. 缓存服务器2台，Redis   CPU核数：4核  内存16G  硬盘: 500GB 操作系统:CentOS Server
4. 测试域名6个（两渠道、四客户），配置Nginx代理

### 安装HTML转PDF插件
    sudo yum install xorg-x11-fonts-75dpi xorg-x11-fonts-Type1
    wget -d https://downloads.wkhtmltopdf.org/0.12/0.12.5/wkhtmltox-0.12.5-1.centos7.x86_64.rpm
    sudo rpm -ivh wkhtmltox-0.12.5-1.centos7.x86_64.rpm
### 配置短信接口Host
    192.168.3.117 api.crevalue.cn