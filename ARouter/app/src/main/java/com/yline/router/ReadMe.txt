地址:
https://github.com/alibaba/ARouter/blob/master/README_CN.md   // 官网地址


插件:
ARouter Helper :
安装后 插件无任何设置，可以在跳转代码的行首找到一个图标 (navigation) 点击该图标，即可跳转到标识了代码中路径的目标类

混淆:
app/proguard-rules.pro 文件中


ModuleFunction 和 ModuleTest 相互之间没有依赖关系，这种场景最适合使用Arouter




注意事项:
1,  如果 拦截器或目标 {
kotlin, 则配置方式需要单独配置 kapt
java, 版本大于2.2 -> annotationProcessor, 小于 -> apt
}


2,  路由文件生成的地址{
  1, 仅仅生成当前module 中, 所有的router.... 即: 没有整体输出
  2, 每个module 都需要单独设置

  3, 输出路径:
  {
    build/generated/source/apt/(debug or release)/com.alibaba.android.arouter/docs/arouter-map-of-${moduleName}.json

    build/generated/source/kapt/(debug or release)/com.alibaba.android.arouter/docs/arouter-map-of-${moduleName}.json
  }
}


todo 待研究:
{
  1, 配置信息，能否不要每个工程都配置
  2, DegradeService  -  自定义全局降级策略, 没有遇到过, 可以看看源码
  3, @Route(path = "/test/activity", extras = Consts.XXXX)    extras 应用 还没有遇到过

  4-1, PretreatmentService path 相同时，源码是怎么处理的。貌似有bug【不允许和其它的重复】
  4-2, PathReplaceService path 相同时，源码是怎么处理的。貌似有bug【不允许和其它的重复】
}

