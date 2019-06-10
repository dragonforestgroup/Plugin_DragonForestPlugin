package com.dragon.plugin
import org.gradle.api.Plugin
import org.gradle.api.Project

class myPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        // 这里是插件的逻辑
        println("================================================================");
        println("==============this is hanlonglin plugin demo====================");
        println("==============this is hanlonglin plugin demo====================");
        println("==============this is hanlonglin plugin demo====================");
        println("==============this is hanlonglin plugin demo====================");
        println("==============this is hanlonglin plugin demo====================");
        println("================================================================");
    }
}