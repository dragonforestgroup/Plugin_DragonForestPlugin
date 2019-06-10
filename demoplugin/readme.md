#gradle插件制作步骤：
1. 新建Android library

2. 去掉src/main 下所有目录和文件，并创建 groovy和resources文件夹,在groovy下创建自己的包名，在resources下建立META-INF/gradle-plugins目录；

3. 去掉moudle中的build.gradle 中的代码，并替换：
```
apply plugin: 'groovy'
apply plugin: 'maven'

dependencies {
    compile gradleApi()
    compile localGroovy()
}

repositories {
    mavenCentral()
}

//打包maven
uploadArchives {
    repositories {
        mavenDeployer {
            // 生成的文件夹的名字 repo
            repository(url: uri('repo'))
            // 设置插件的GAV参数
            // 项目中引入mavan的格式就是 com.dragon.plugin:myPlugin:1.0
            pom.groupId = 'com.dragon.plugin'
            pom.artifactId = 'myPlugin'
            pom.version = 1.0
        }
    }
}
```

4. 在groovy包下建立 xxx.groovy文件，创建类并实现plugin接口；要实现的逻辑写在apply()中；

5. 在resource/META-INF/gradle-plugins下创建 xxx.properties 文件，这个文件的名字就是之后apply 后面使用的名字，声明使用的插件类：
```
implementation-class=com.dragon.plugin.myPlugin
```

在右边可以看到upload uploadArchives ,双击就可以生成repo文件夹，并可以看到maven文件了；


