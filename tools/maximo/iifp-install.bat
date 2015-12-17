@ECHO OFF

SETLOCAL

REM iifp-install.bat -- 
REM		Runs com.interlocsolutions.maximo.tools.IFramePortletEnvSetup
REM

call commonEnv.bat

..\java\jre\bin\java  -classpath .;.\classes;%MAXIMO_CLASSPATH%;.\lib\jsap.jar;.\lib\jaxen.jar;.\lib\isiframe-tools.jar;.\lib\MaximoPortlet-tools.jar  com.interlocsolutions.maximo.tools.IFramePortletEnvSetup %*
internal\runscriptfile.bat -cisiframe -fiframe_001