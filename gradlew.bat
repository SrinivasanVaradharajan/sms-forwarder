# Copyright 2019 The Android Open Source Project
# Licensed under the Apache License, Version 2.0

@echo off
setlocal

set DEFAULT_JVM_OPTS="-Xmx2048m"
set APP_BASE_NAME=%~n0
set APP_HOME=%~dp0..

set CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

"%JAVA_HOME%\bin\java" %DEFAULT_JVM_OPTS% -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
