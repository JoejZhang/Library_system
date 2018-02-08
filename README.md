# Library_system
图书管理系统。用Java实现的PC端软件。使用MySql作为DBMS操作本地数据库，用JDBC连接Java和数据库。实现图书管理系统的基本功能

### 项目介绍
该项目主要实现了图书管理系统几个主要的基本功能，做这小项目是为了简单学习数据库设计、包括ER图，关系模式，范式，完整性约束，安全性，视图等等。
学习简单SQL语言、学习使用JDBC对Java和数据库的连接等等

### 功能模块图
![](https://github.com/JoejZhang/Library_system/blob/master/image/gongneng.png)

### 完整E-R图
![](https://github.com/JoejZhang/Library_system/blob/master/image/er.jpg)

### 关系模型
学生表（ 学号，学生姓名，性别，年龄，学院）

![](https://github.com/JoejZhang/Library_system/blob/master/image/student.png)

图书表 （图书编号，图书名，图书总数，可借数目）

![](https://github.com/JoejZhang/Library_system/blob/master/image/book.png)

图书管理员（管理员编号，管理员姓名，性别，年龄）

![](https://github.com/JoejZhang/Library_system/blob/master/image/admin.png)

学生借还信息表（学号，图书编号，管理员编号，借书时间，归还时间，借书数目） 

![](https://github.com/JoejZhang/Library_system/blob/master/image/lend.png)

学生登录信息表（学号（作为账号），密码）

![](https://github.com/JoejZhang/Library_system/blob/master/image/login_student.png)

管理员登录信息表（管理员编号(作为账号)，密码）

![](https://github.com/JoejZhang/Library_system/blob/master/image/login_admin.png)

zjz_Student（ Sno，Sname，Ssex，Sage,Scollege,Sno→Sname，Sno→Ssex, Sno→Sage, Sno→Scollege）
学生表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

zjz_Book（Bno，Bname，BAllcount,Blendcount，Bno→Bname, Bno→BAllcount, Bno→Blendcount）
图书表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

zjz_Admin（Ano，Aname，Asex，Aage,Ano→Aname, Ano→Asex, Ano→Aage）
图书管理员表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

zjz_BS（Sno，Bno，Ano,BSlendtime，BSbacktime,BScount，(Sno，Bno，Ano,BSlendtime，BSbacktime) →BScount）
学生借还信息表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

zjz_Student_login（Sno，Spass，Sno→Spass）
学生登录信息表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

zjz_Admin_login（Ano，Apass,Ano→Apass）；
管理员登录信息表所有的非主属性完全依赖于码，没有主属性部分或传递依赖于码，所以属于BCNF。

### 视图

学生已借图书（学生名和图书名）视图
View_StudentBookName(zjz_Student.Sname,zjz_Book.Bname,zjz_Admin.Aname, BSlendtime，BSbacktime,BScount)

![](https://github.com/JoejZhang/Library_system/blob/master/image/view_sql.png)

![](https://github.com/JoejZhang/Library_system/blob/master/image/view.png)

### 安全性（用户类别和权限）设计和实现说明
登录或注册的时候用一个专门用于登录注册的数据库用户（account_library），用于查看或者更新学生登录信息表和管理员登录信息表，还有查看学生表。用于检验用户是否能登录成功，或创建用户。
学生登录信息表和管理员登录信息表用于保存所有的账户和密码。
有两个数据库用户（student_library，admin_library）分别用于学生和图书管理员用户登录后，用于连接数据库。学生使用student用户，只拥有查看学生表、图书表、借书还书记录表的权限，管理员使用admin用户拥有查看管理员表、学生表、图书表、借书还书记录表的权限，还有更新借书还书记录表的权限。

### 完整性（主、外码和用户自定义的完整性约束）设计和实现说明。

1、学生表（ 学号，学生姓名，性别，年龄，学院）
主码为学号，学生的年龄大于0,性别为男或女，姓名不为空

create  trigger zjz_Student_tri
  After insert on zjz_Student
  for each row
  begin
  if ((new.Ssex<>'男' && new.Ssex <>'女')||new.Sage <=0  )then INSERT into zjz_student VALUES('error');
  end if;
  end;
  

2、图书表 （图书编号，图书名，图书总数，可借数目）
主码为图书编号，图书总数大于0 可借数目大于等于0,可借数目小于等于可借总数，图书名不为空

create  trigger zjz_Book_tri_insert
  After insert on zjz_Book
  for each row
  begin
  if (new.BAllCount <= 0 || new.BlendCount <0 ||new.BlendCount > new.BAllCount )then INSERT into zjz_Book VALUES('error');
  end if;
  end;

3、图书管理员表（管理员编号，管理员姓名，性别，年龄）
主码为图书管理员编号，管理员年龄大于0,性别为男或女，姓名不为空

create  trigger zjz_Admin_tri
  After insert on zjz_Admin
  for each row
  begin
  if ((new.Asex<>'男' && new.Asex <>'女')||new.Aage <=0  )then INSERT into zjz_Admin VALUES('error');
  end if;
  end;

4、学生借还信息表（学号，图书编号，管理员编号，借书时间，归还时间，借书数目） 
主码为学号，图书编号，管理员编号，借书时间，归还时间，学号是外码，参照学生表，图书编号是外码，参照图书表，管理员编号是外码，参照图书管理员表。借书数目>0

create  trigger zjz_BS_tri
  After insert on zjz_bs
  for each row
  begin
  if (new.BScount <=0  )then INSERT into zjz_BS VALUES('error');
  end if;
  end;

5、学生登录信息表（学号（作为账号），密码）
主码为学号，学号为外码，参照学生表

6、管理员登录信息表（管理员编号(作为账号)，密码）
主码为管理员编号，管理员编号为外码，参照管理员编号


