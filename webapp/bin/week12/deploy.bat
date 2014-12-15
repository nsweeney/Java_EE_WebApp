setlocal
SET SRC=C:\Users\nick\Documents\localEcWksp\ATM_WebApp2\webapp\src\week12\atm_webapp2.war
SET DST=C:\Users\nick\Desktop\apache_tomcat\apache-tomcat-8.0.14-windows-x64\apache-tomcat-8.0.14\webapps
xcopy %SRC% %DST% /Y
endlocal