@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  DemoCST startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and DEMO_CST_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\DemoCST.jar;%APP_HOME%\lib\WS3DProxy.jar;%APP_HOME%\lib\cst-0.2.4.jar;%APP_HOME%\lib\json-20180813.jar;%APP_HOME%\lib\gson-2.7.jar;%APP_HOME%\lib\compiler-2.3.0.jar;%APP_HOME%\lib\logback-classic-1.0.11.jar;%APP_HOME%\lib\slf4j-api-1.7.14.jar;%APP_HOME%\lib\jfreechart-1.0.19.jar;%APP_HOME%\lib\jung-visualization-2.0.1.jar;%APP_HOME%\lib\jung-algorithms-2.0.1.jar;%APP_HOME%\lib\jung-graph-impl-2.0.1.jar;%APP_HOME%\lib\junit-4.9.jar;%APP_HOME%\lib\opt4j-tutorial-3.1.jar;%APP_HOME%\lib\opt4j-optimizers-3.1.jar;%APP_HOME%\lib\opt4j-viewer-3.1.jar;%APP_HOME%\lib\opt4j-benchmarks-3.1.jar;%APP_HOME%\lib\opt4j-operators-3.1.jar;%APP_HOME%\lib\opt4j-satdecoding-3.1.jar;%APP_HOME%\lib\opt4j-core-3.1.jar;%APP_HOME%\lib\antlr4-runtime-4.5.3.jar;%APP_HOME%\lib\guice-multibindings-3.0.jar;%APP_HOME%\lib\guice-3.0.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\cglib-2.2.1-v20090111.jar;%APP_HOME%\lib\asm-3.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\jung-api-2.0.1.jar;%APP_HOME%\lib\org.ow2.sat4j.pb-2.3.3.jar;%APP_HOME%\lib\org.ow2.sat4j.core-2.3.3.jar;%APP_HOME%\lib\org.ow2.sat4j.core-2.3.3-tests.jar;%APP_HOME%\lib\google-collections-1.0.jar;%APP_HOME%\lib\commons-beanutils-core-1.8.0.jar;%APP_HOME%\lib\commons-math3-3.0.jar;%APP_HOME%\lib\annotations-12.0.jar;%APP_HOME%\lib\jcommon-1.0.23.jar;%APP_HOME%\lib\collections-generic-4.01.jar;%APP_HOME%\lib\colt-1.2.0.jar;%APP_HOME%\lib\hamcrest-core-1.1.jar;%APP_HOME%\lib\logback-core-1.0.11.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar;%APP_HOME%\lib\concurrent-1.3.4.jar

@rem Execute DemoCST
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %DEMO_CST_OPTS%  -classpath "%CLASSPATH%" ExperimentMain %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable DEMO_CST_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%DEMO_CST_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
