#!/bin/sh
list_file=$1
cat $list_file | while read line
do
   src_file=`echo $line | awk '{print $1}'`
   dest_file=`echo $line | awk '{print $2}'`
   type=`echo $line | awk '{print $3}'`

   if [ $type -eq 1 ]
   then
      echo "开始处理文件夹：$src_file"
      ./uploadFiles.sh $src_file $dest_file
   else
      echo "开始处理文件：$src_file"
      ./uploadFile.sh $src_file $dest_file
   fi
   
done
