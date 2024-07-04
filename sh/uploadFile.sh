#!/usr/bin/expect -f

# 设置变量
set timeout 30
set host "43.139.31.171"
set username "root"
set password "Txy373737."
set src_file [lindex $argv 0]
set dest_file [lindex $argv 1]


# 上传文件
spawn scp $src_file $username@$host:$dest_file

# 等待密码提示
expect "password:"

# 发送密码
send "$password\r"

# 交互模式
interact
