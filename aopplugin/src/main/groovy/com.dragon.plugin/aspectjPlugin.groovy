package com.dragon.plugin

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

/**
 * ApsectJ 引用插件
 *
 */
class aspectjPlugin implements Plugin<Project> {

    // ===========module类型枚举==========
    /**
     * 应用类型
     */
    private static final int TYPE_APPLICATION = 1;
    /**
     * 库类型
     */
    private static final int TYPE_LIBRARY = 2;

    // ========变量==============
    /**
     * 当前模块是否是库类型
     */
    boolean isLib = false
    /**
     * 是否执行成功 库类型和应用类型有一个成功即可
     */
    boolean isSuccess = true

    /**
     * 错误信息
     */
    String errMsg;
//
//    @Override
//    void apply(Project project) {
//        final def log = project.logger
//        log.error("╔══════════════════════════════════════════════════════════════")
//        log.error("║DragonForest AspectJ插件开始收集class==当前模块：" + project.getName() + "")
//        log.error("╟──────────────────────────────────────────────────────────────")
//        if (!startCollect(project, TYPE_APPLICATION)) {
//            isLib = true
//            if (!startCollect(project, TYPE_LIBRARY)) {
//                isSuccess = false
//            }
//        }
//        if (isLib) {
//            log.error("║DragonForest AspectJ执行模块类型为library")
//            log.error("╟──────────────────────────────────────────────────────────────")
//        } else {
//            log.error("║DragonForest AspectJ执行模块类型为application")
//            log.error("╟──────────────────────────────────────────────────────────────")
//        }
//        if (!isSuccess) {
//            log.error("║DragonForest AspectJ执行失败,libErrorMsg:" + libErrMsg + ",appErrorMsg:" + appErrMsg);
//            log.error("╟──────────────────────────────────────────────────────────────")
//        }
//        log.error("║DragonForest AspectJ 执行结束")
//        log.error("╚══════════════════════════════════════════════════════════════")
//    }


    @Override
    void apply(Project project) {
        project.dependencies{
            implementation 'org.aspectj:aspectjrt:1.9.2'
        }
        final def log = project.logger
        log.error("╔══════════════════════════════════════════════════════════════")
        log.error("║DragonForest AspectJ 插件开始收集class,当前模块：" + project.getName())
        log.error("╟──────────────────────────────────────────────────────────────")
        if (project.android.hasProperty("applicationVariants")) {
            // application
            log.error("║DragonForest AspectJ 执行模块类型为application")
            log.error("╟──────────────────────────────────────────────────────────────")
            isSuccess = startCollect(project, TYPE_APPLICATION);
        } else if (project.android.hasProperty("libraryVariants")) {
            // library
            log.error("║DragonForest AspectJ 执行模块类型为library")
            log.error("╟──────────────────────────────────────────────────────────────")
            isSuccess = startCollect(project, TYPE_LIBRARY);
        } else {
            // 错误
            log.error("║DragonForest AspectJ 执行模块类型为未知类型！！！AspectJ不执行收集class")
            log.error("╟──────────────────────────────────────────────────────────────")
            isSuccess = false;
        }
        if (!isSuccess) {
            log.error("║DragonForest AspectJ 执行失败,errMsg:" + errMsg);
        } else {
            log.error("║DragonForest AspectJ 执行成功")
        }
        log.error("╚══════════════════════════════════════════════════════════════")
    }

    /**
     * 执行收集
     *
     * @param project
     * @param type
     * @return
     */
    private boolean startCollect(Project project, int type) {
        final def log = project.logger
        try {
            def variants
            if (type == TYPE_APPLICATION) {
                variants = project.android.applicationVariants
            } else if (type == TYPE_LIBRARY) {
                variants = project.android.libraryVariants
            }
            variants.all { variant ->
                //在编译后 增加行为
                JavaCompile javaCompile = variant.javaCompile
                javaCompile.doLast {
                    String[] args = ["-showWeaveInfo",
                                     "-1.8",
                                     "-inpath", javaCompile.destinationDir.toString(),
                                     "-aspectpath", javaCompile.classpath.asPath,
                                     "-d", javaCompile.destinationDir.toString(),
                                     "-classpath", javaCompile.classpath.asPath,
                                     "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
                    MessageHandler handler = new MessageHandler(true);
                    new Main().run(args, handler);
                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                break;
                            case IMessage.WARNING:
                                log.warn message.message, message.thrown
                                break;
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break;
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break;
                        }
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            errMsg = e.getMessage()
            return false;
        }
    }


}