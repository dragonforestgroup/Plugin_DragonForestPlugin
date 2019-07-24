# AspectJ 插件

引入插件后不必再手动引入aspectj依赖，也不用添加代码；

引入步骤：
1. project中build.gradle 
添加：
```
repositories {
       
       // 搜索路径根据自己的来
        maven {
            url uri('D:\\projects\\DragonForest\\DragonForestPlugin\\aopplugin\\repo')
        }
    }
    dependencies {
        ...
        classpath 'com.dragonforest.plugin:AspectjPlugin:1.0.0'
    }
```
2. module 的build.gradle中添加依赖：
```
implementation 'org.aspectj:aspectjrt:1.9.2'
```
3. module 的build.gradle中添加 ：
apply plugin: 'aspectj-plugin'

即可完成；

说明：
  1. 使用的aspectj插件版本 org.aspectj:aspectjtools:1.9.2
  2. 依赖的aspectj版本：org.aspectj:aspectjrt:1.9.2