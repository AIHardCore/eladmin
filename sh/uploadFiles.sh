#!/usr/bin/expect
 
# 设置变量
set timeout 30
set host "43.139.31.171"
set username "root"
set password "Txy373737."
set src_file "/mnt/h/AllWorkSpase/IDEA_WorkSpase/eladmin/eladmin-app/target/Jar/lib/eladmin-common-2.7.jar  /mnt/h/AllWorkSpase/IDEA_WorkSpase/eladmin/eladmin-app/target/Jar/liblib/eladmin-tools-2.7.jar  /mnt/h/AllWorkSpase/IDEA_WorkSpase/eladmin/eladmin-app/target/Jar/lib/eladmin-logging-2.7.jar "
set dest_file "/home/xxj/server/xxj_app/"
 
# 启动scp命令
spawn scp $src_file $username@$host:$dest_file
 
# 等待密码提示
expect "*password:"
 
# 发送密码
send "$password\r"
 
# 交互模式，允许用户交互
interact
