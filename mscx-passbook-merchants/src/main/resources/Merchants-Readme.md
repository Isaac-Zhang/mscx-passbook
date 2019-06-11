#部署系统list
##需要启动的服务
* hbase
* mysql
* kafka
* redis
##需要清空的数据
- hbase 4张表数据
```scheme
hbase(main):042:0> truncate 'pb:pass_template';
hbase(main):042:0> truncate 'pb:pass';
hbase(main):042:0> truncate 'pb:feedback';
hbase(main):042:0> truncate 'pb:user';
```
- mysql 商户数据
 ```mysql
delete from tb_merchants;
 ```   
- /tmp/token/ 下面的优惠券 token 数据
```txt
/temp/token/ rm -rf *
```
- redis 中的数据
```scheme
flushall
```

#系统使用顺序
##1. 创建商户 
> POST：127.0.0.1:9527/merchants/create
> header: token=token

```json
{
	"name": "张盼店铺",
	"logoUrl": "www.babydy.cn",
	"businessLicenseUrl": "www.babydy.cn",
	"phone": "15009299224",
	"address": "陕西西安"
}
```
> Result:
```json
{
    "errorCode": 0,
    "errorMsg": "",
    "data": {
        "id": 21
    }
}
```
##2. 查看商户
> GET: 127.0.0.1:9527/merchants/21
> header: token=token
> Result:
```json
{
    "errorCode": 0,
    "errorMsg": "",
    "data": {
        "id": 21,
        "name": "张盼店铺",
        "logoUrl": "www.babydy.cn",
        "businessLicenseUrl": "www.babydy.cn",
        "phone": "15009299224",
        "address": "陕西西安",
        "isAudit": true
    }
}
```
##3. 投放优惠券
> POST：127.0.0.1:9527/merchants/launch
> header: token=token
```json
{
	"background":3,
	"desc":"详情：张盼店铺满100-50券",
	"end":"2019-12-31",
	"hasToken":true,
	"id":21,
	"limit":1000,
	"start":"2019-06-10",
	"summary":"张盼店铺满100-50券",
	"title":"满100-50券"
},
{
	"background":3,
	"desc":"详情：张盼店铺会员+1券",
	"end":"2019-12-31",
	"hasToken":false,
	"id":21,
	"limit":1000,
	"start":"2019-06-10",
	"summary":"张盼店铺会员+1券",
	"title":"会员+1券"
}


```