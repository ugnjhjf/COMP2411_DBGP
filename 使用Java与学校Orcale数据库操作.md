Tag: #Java #编程 #数据库 #服务器 #运维 #后端 #OrcaleSQL #SQL #终端 #Coding 

总结流程：
登入PolyU个人虚拟机->上传文件到虚拟机->VM跑SQL指令->VM编译Java->VM用Java查询Orcale数据

终端登入
csdoor.comp.polyu.edu.hk

PolyU ID,PolyU pwd

打apollo

上传.java文件测试
（要先用SQLPLUS建立数据库）

SQLPLUS路径
source /compsoft/app/oracle/dbms.bashrc
sqlplus
ID: "PolyUID"@dbms <font color=red>" " 不可忽略</font>
密码：DBMS的密码（邮件）≠ PolyU pwd


输入@+文件地址，让sqlplus跑.sql文件
```sql

SQL> @/home/22084045d/OrcaleTest/employee_data.sql
DROP TABLE EMPLOYEES
           *
ERROR at line 1:
ORA-00942: table or view does not exist
Table created.


1 row created.


1 row created.


SQL> exit;
```


回到放employee和simpleapplication.java的路径
编译java
```powershell
javac -cp ojdbc8.jar employee.java 
```

运行java，注意下面用了冒号
```powershell
java -cp ojdbc8.jar:. employee
```


用户名："PolyUID" 没有@dbms <font color=red>" " 不可忽略</font>
密码：DB密码
```sql
1000 Jones
1001 Brown
1002 Green
1010 Clark
1011 Black
1012 Kent
1020 Jones
1021 Smith
1022 Laney
1025 Marsh
1028 Mary

employee number: 1000
1000 Jones 67226 2010-12-12

 Enter a number to continue or -1 to exit. 

```



