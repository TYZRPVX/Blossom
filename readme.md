# 功能
* 利用注解（@Tie）绑定 View

# 预备知识
* 注解解析
    * http://www.importnew.com/14479.html
* lint：静态代码检测工具
    * http://www.jianshu.com/p/ba1ce1c1ae39
    * https://gold.xitu.io/entry/576b613579bc44005bdb0c12
    * http://tech.meituan.com/android_custom_lint.html
* Android 单元测试
    * http://www.jianshu.com/p/03118c11c199
* gradle 发布项目
    * https://rocko.xyz/2015/02/02/使用Gradle发布项目到JCenter仓库/

* apt 的使用
    * https://joyrun.github.io/2016/07/19/AptHelloWorld/
    * https://github.com/mohlendo/android-apt-example
    * 必须添加AutoService注解
    * processor 中的getSupportedAnnotationTypes必须添加注解类
    * 注解类必须要实际用在项目源代码里

* 笔记
    * 使用debug工具进行调试效率更高
    * javaPoet 处理泛型需要提前熟练
    * 反射新建对象需要使用该类自带的构建器

# 可选
* RxJava