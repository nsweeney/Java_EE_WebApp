setlocal
SET JAVA_HOME=C:\Program Files\Java\jdk1.7.0_71
SET ANT_HOME=C:\Users\nick\Documents\localEcWksp\ant
SET ANT_BAT=%ANT_HOME%\bin\ant.bat
SET BUILD_XML=C:\Users\nick\Documents\localEcWksp\Atm_WebApp2\webapp\src\week12\build\build.xml
SET WEBAPP_DST_FLDR=C:\Users\nick\Documents\localEcWksp\webapp\atm_webapp2
SET SRC_FLDR=C:/Users/nick/Documents/localEcWksp/Atm_WebApp2/webapp/src

%ANT_BAT% -buildfile %BUILD_XML% -DsrcFolder=%SRC_FLDR% -DwebappDstFolder=%WEBAPP_DST_FLDR% %1

endlocal