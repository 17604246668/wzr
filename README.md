项目采用springboot2.2.8.RELEASE + jdk1.8 + maven3.0 + mysql5.7

注: 该代码仅仅为demo基础代码，所有是用此代码开发的人员，请注意两点!!!!

1：huangshan为demo名称，开发人员clone后开始时请将所有huangshan名称修改为自己的项目名称。

2：该框架旨在提供一个基础框架，请勿在该框架上开发并push,所有开发人员如果使用一律复制一个新的地址再进行开发


项目模块描述:

    huangshan-admin: 对应管理后台请求接口
    huangshan-api: 对应非管理后台请求接口
    huangshan-common: 公共部门代码api、admin都可复用
    huangshan-model: 公共类
           domain: 对应数据生成的实体类
           dtos: 对应非数据库生成都实体类
               dto: 请求接口时对应的类
               vo: 返回数据时对应的类
    
配置文件介绍:

    -dev.yml: 对应开发环境配置
    -test.yml: 对应测试环境配置
    -prod.yml: 对应正式环境配置
    
    