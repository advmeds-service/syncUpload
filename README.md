# syncUpload

## 初始化
> 可以在application onCreate 中调用
```
    RemoteController.init(application)
```

> 上传
1. 先构造HttpFormat对象
2. 调用RemoteController.upload(httpFormat)
```
    val httpFormat = HttpFormat(
                    requestType = HttpFormat.GET,
            baseUrl = "https://www.wosign.com/News/chrome-https.htm"
            )
            httpFormat.extraSuccessCodes = intArrayOf(1, 2, 3)

            RemoteController.upload(httpFormat) {
                LogUtils.d("code: ${it.code} ${it.getString()}")
            }
```

> 重传
```
    RemoteController.reUpload()
```



