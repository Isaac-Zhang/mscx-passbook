#用户应用子系统
##1. 上传优惠券 token
> GET: 127.0.0.1:9528/upload
> Param: merchantsId: 21
> Param: PassTemplateId: 5f41278ea1e04c33edef3ac45c3a46d7

##2. 创建用户
> POST: 127.0.0.1:9528/v1/user/register
```json
{
	"baseInfo":{
		"name":"isaac",
		"age":18,
		"sex":"m"
	},
	"otherInfo":{
		"phone":"15009299224",
		"address":"陕西西安果粒城"
	}
} 
Result:
{
    "errorCode": 0,
    "errorMsg": "",
    "data": {
        "id": 189283,
        "baseInfo": {
            "name": "isaac",
            "age": 18,
            "sex": "m"
        },
        "otherInfo": {
            "phone": "15009299224",
            "address": "陕西西安果粒城"
        }
    }
}
```
##3. 根据用户获取可用的优惠券库存
> GET: 127.0.0.1:9528/v1/passbook/user/inventory?user_id=189283
```json
{
    "errorCode": 0,
    "errorMsg": "",
    "data": {
        "userId": 189283,
        "passTemplateInfoVOS": [
            {
                "passTemplateVO": {
                    "id": 21,
                    "title": "满100-50券",
                    "summary": "张盼店铺满100-50券",
                    "desc": "详情：张盼店铺满100-50券",
                    "limit": 1000,
                    "hasToken": true,
                    "background": 3,
                    "start": "2019-06-09T16:00:00.000+0000",
                    "end": "2019-12-30T16:00:00.000+0000"
                },
                "merchants": {
                    "id": 21,
                    "name": "张盼店铺",
                    "logoUrl": "www.babydy.cn",
                    "businessLicenseUrl": "www.babydy.cn",
                    "phone": "15009299224",
                    "address": "陕西西安",
                    "isAudit": true
                }
            },
            {
                "passTemplateVO": {
                    "id": 21,
                    "title": "会员+1券",
                    "summary": "张盼店铺会员+1券",
                    "desc": "详情：张盼店铺会员+1券",
                    "limit": 1000,
                    "hasToken": false,
                    "background": 3,
                    "start": "2019-06-09T16:00:00.000+0000",
                    "end": "2019-12-30T16:00:00.000+0000"
                },
                "merchants": {
                    "id": 21,
                    "name": "张盼店铺",
                    "logoUrl": "www.babydy.cn",
                    "businessLicenseUrl": "www.babydy.cn",
                    "phone": "15009299224",
                    "address": "陕西西安",
                    "isAudit": true
                }
            }
        ]
    }
}
```
##4. 领取优惠券（带token）
> POST: 127.0.0.1:9528/v1/passbook/user/receive
```json
{
	"userId":189283,
	"passTemplateVO":{
		"id":21,
		"title":"会员+1券",
		"hasToken":true
	}
}
```
##5. 用户获取优惠券
> GET: 127.0.0.1:9528/v1/passbook/user/189283/un_used
```json
{
    "errorCode": 0,
    "errorMsg": "",
    "data": [
        {
            "passVO": {
                "userId": 189283,
                "rowKey": "38298192233704766163605235f41278ea1e04c33edef3ac45c3a46d7",
                "templateId": "5f41278ea1e04c33edef3ac45c3a46d7",
                "token": "token-2",
                "assignedDate": "2019-06-10T16:00:00.000+0000",
                "consumeDate": null
            },
            "passTemplateVO": {
                "id": 21,
                "title": "满100-50券",
                "summary": "张盼店铺满100-50券",
                "desc": "详情：张盼店铺满100-50券",
                "limit": 999,
                "hasToken": true,
                "background": 3,
                "start": "2019-06-09T16:00:00.000+0000",
                "end": "2019-12-30T16:00:00.000+0000"
            },
            "merchants": {
                "id": 21,
                "name": "张盼店铺",
                "logoUrl": "www.babydy.cn",
                "businessLicenseUrl": "www.babydy.cn",
                "phone": "15009299224",
                "address": "陕西西安",
                "isAudit": true
            }
        },
        {
            "passVO": {
                "userId": 189283,
                "rowKey": "38298192233704766166040839854f060c777db5e12e2352cd5fc63cf",
                "templateId": "9854f060c777db5e12e2352cd5fc63cf",
                "token": "-1",
                "assignedDate": "2019-06-10T16:00:00.000+0000",
                "consumeDate": null
            },
            "passTemplateVO": {
                "id": 21,
                "title": "会员+1券",
                "summary": "张盼店铺会员+1券",
                "desc": "详情：张盼店铺会员+1券",
                "limit": 999,
                "hasToken": false,
                "background": 3,
                "start": "2019-06-09T16:00:00.000+0000",
                "end": "2019-12-30T16:00:00.000+0000"
            },
            "merchants": {
                "id": 21,
                "name": "张盼店铺",
                "logoUrl": "www.babydy.cn",
                "businessLicenseUrl": "www.babydy.cn",
                "phone": "15009299224",
                "address": "陕西西安",
                "isAudit": true
            }
        }
    ]
}
```
##6. 创建评论
> POST: 127.0.0.1:9528/v1/passbook/user/feedback/create
```json
[{
	"userId":189283,
	"templateId":"5f41278ea1e04c33edef3ac45c3a46d7",
	"type":"app",
	"comment":"这个卡包系统真好啊！"
},
{
	"userId":189283,
	"templateId":"5f41278ea1e04c33edef3ac45c3a46d7",
	"type":"pass",
	"comment":"+1券真好用！"
}]

``` 