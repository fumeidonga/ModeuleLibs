



# 1.ubuntu 安装配置 

<pre>
官方下载链接：https://download3.vmware.com/software/wkst/file/VMware-workstation-full-15.0.0-10134415.exe

永久许可证：ZC10K-8EF57-084QZ-VXYXE-ZF2XF


附：备用许可项

   UF71K-2TW5J-M88QZ-8WMNT-WKUY4

AZ7MK-44Y1J-H819Z-WMYNC-N7ATF

CU702-DRD1M-H89GP-JFW5E-YL8X6

YY5EA-00XDJ-480RP-35QQV-XY8F6

VA510-23F57-M85PY-7FN7C-MCRG0



windows和ubuntu通过samba文件互相访问

1、安装samba：sudo apt-get install samba samba-common

2、smba安装好之后，会在/etc/samba/文件夹下有一个smb.conf文件，
此文件是samba的一个配置文件，修改该配置文件：sudo gedit /etc/samba/smb.conf 

3、在文件末尾追加如下配置：

[src]
path = /home/src
public = yes
browseable = yes
writable = yes

4、此时通过windows资源管理器输入ubuntu的ip地址即可完成访问。

注：

如遇提示权限不够。请用chmod命令重置共享文件夹的权限（777）即拥有读写权限

sudo apt-get install git-core gnupg flex bison gperf build-essential zip curl zlib1g-dev gcc-multilib g++-multilib libc6-dev-i386 lib32ncurses5-dev x11proto-core-dev libx11-dev lib32z-dev ccache libgl1-mesa-dev libxml2-utils xsltproc unzip


ubuntu 14 安装jdk7

1.  下载jdk

2. sudo mkdir /usr/lib/jvm
3. sudo tar zxvf jdk-7u67-linux-x64.tar.gz -C /usr/lib/jvm
4. 设置环境变量

sudo gedit ~/.bashrc

　文件的末尾追加下面内容:

#set oracle jdk environment
export JAVA_HOME=/usr/lib/jvm/jdk1.7.0_80  ## 这里要注意目录要换成自己解压的jdk 目录
export JRE_HOME=${JAVA_HOME}/jre  
export CLASSPATH=.:${JAVA_HOME}/lib:${JRE_HOME}/lib  
export PATH=${JAVA_HOME}/bin:$PATH  

　使环境变量马上生效

 source ~/.bashrc
5.设置系统默认jdk 版本
sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk1.7.0_80/bin/java 300  
sudo update-alternatives --install /usr/bin/javac javac /usr/lib/jvm/jdk1.7.0_80/bin/javac 300  
sudo update-alternatives --install /usr/bin/jar jar /usr/lib/jvm/jdk1.7.0_80/bin/jar 300   
sudo update-alternatives --install /usr/bin/javah javah /usr/lib/jvm/jdk1.7.0_80/bin/javah 300   
sudo update-alternatives --install /usr/bin/javap javap /usr/lib/jvm/jdk1.7.0_80/bin/javap 300   

　然后执行:

sudo update-alternatives --config java

    若是初次安装jdk,会有下面的提示     

   There is only one alternative in link group java (providing /usr/bin/java): 
    /usr/lib/jvm/jdk1.7.0_60/bin/java

　否者,选择合适的jdk

测试jdk
java -version

java version "1.7.0_60"
  Java(TM) SE Runtime Environment (build 1.7.0_60-b19)
  Java HotSpot(TM) 64-Bit Server VM (build 24.60-b09, mixed mode)

  jdk 安装成功


</pre>

# 2. 安装git

sudo apt-get install git 

    git config --global user.name "Your Name"
    git config --global user.email "you@example.com"

查看当前路径命令：pwd

chmod -R 777 xxx/

cp -r aosp ./share

tar -zcvf /home/xahot.tar.gz /xahot

单个文件压缩打包 tar czvf my.tar file1

多个文件压缩打包 tar czvf my.tar file1 file2,...

单个目录压缩打包 tar czvf my.tar dir1

多个目录压缩打包 tar czvf my.tar dir1 dir2

解包至当前目录：tar xzvf my.tar

df -h 查看剩余空间的大小


# 3. 下载源码
1. 下载源码
https://pan.baidu.com/s/1W02O6cbtshaTqmschLBqsQ#list/path=%2FAndroid%E6%BA%90%E7%A0%81

2. 导入对应的文件
https://github.com/difcareer/AndroidSourceReader


ubuntu 编译

也可以自己下载，自己编译，然后导入

[ksxf](https://lug.ustc.edu.cn/wiki/mirrors/help/aosp)

[清华 aosp](https://mirrors.tuna.tsinghua.edu.cn/help/AOSP/)

[参考](https://blog.csdn.net/xlyrh/article/details/51427288)


## 方式 一

### 1. 下载打包的 AOSP 镜像

下载 ： https://mirrors.tuna.tsinghua.edu.cn/aosp-monthly/aosp-latest.tar，

下载地址2 ： http://mirrors.ustc.edu.cn/aosp-monthly/

请注意对比 checksum。

### 2.更改同步地址
修改 .repo/manifests.git/config，将

    url = https://android.googlesource.com/platform/manifest
修改成

    url = git://mirrors.ustc.edu.cn/aosp/platform/manifest
即可。

### 3. 下载 (同步) Android 源代码树

repo sync 就可以把代码都 checkout 出来。

    repo sync

## 方式 二
[官方教程](https://source.android.com/source/downloading.html)

[源代码标记和版本](https://source.android.com/source/build-numbers.html#source-code-tags-and-builds)

### 1. 安装 Repo
Repo 是一款工具，可让您在 Android 环境中更轻松地使用 Git。要详细了解 Repo，请参阅开发部分。

要安装 Repo，请执行以下操作：

1. 确保主目录下有一个 bin/ 目录，并且该目录包含在路径中：

        mkdir ~/bin
        PATH=~/bin:$PATH

2. 下载 Repo 工具，并确保它可执行：

        curl https://storage.googleapis.com/git-repo-downloads/repo > ~/bin/repo
        ## 如果上述 URL 不可访问，可以用下面的：
        ## curl -sSL  'https://gerrit-googlesource.proxy.ustclug.org/git-repo/+/master/repo?format=TEXT' |base64 -d > ~/bin/repo
        chmod a+x ~/bin/repo

3. 初始化 Repo 客户端

    安装 Repo 后，设置您的客户端以访问 Android 源代码代码库：
    
    创建一个空目录来存放您的工作文件。如果您使用的是 MacOS，必须在区分大小写的文件系统中创建该目录。为其指定一个您喜欢的任意名称：
    
        mkdir WORKING_DIRECTORY
        cd WORKING_DIRECTORY
    
    使用您的真实姓名和电子邮件地址配置 Git。要使用 Gerrit 代码审核工具，您需要一个与已注册的 Google 帐号关联的电子邮件地址。
    确保这是您可以接收邮件的有效地址。您在此处提供的姓名将显示在您提交的代码的提供方信息中。
    
        git config --global user.name "Your Name"
        git config --global user.email "you@example.com"
        
    运行 repo init 以获取最新版本的 Repo 及其最近的所有错误更正内容。您必须为清单指定一个网址，该网址用于指定 Android 源代码
    中包含的各个代码库将位于工作目录中的什么位置。
    
        repo init -u https://android.googlesource.com/platform/manifest
        ## 如果上述 URL 不可访问，可以用下面的：
        ## repo init -u git://mirrors.ustc.edu.cn/aosp/platform/manifest
        ## 如果提示无法连接到 gerrit.googlesource.com，可以编辑 ~/bin/repo，把 REPO_URL 一行替换成下面的：
        ## REPO_URL = 'https://gerrit-googlesource.proxy.ustclug.org/git-repo'
    
    要对“master”以外的分支进行校验，请使用 -b 来指定相应分支。要查看分支列表，请参阅*源代码标记和版本*。
    
        repo init -u https://android.googlesource.com/platform/manifest -b android-4.0.1_r1
        ## repo init -u git://mirrors.ustc.edu.cn/aosp/platform/manifest -b android-4.0.1_r1
    
    初始化成功后，系统将显示一条消息，告诉您 Repo 已在工作目录中完成初始化。客户端目录中现在应包含一个 .repo 目录，清单等文件将保存在该目录下。

# 4. 下载 (同步) Android 源代码树

修改git config 文件

添加前三行

<pre>
gedit aosp/.repo/manifests/.git/config

[user]
email="1181406436@qq.com"
name="david"

[core]
	repositoryformatversion = 0
	filemode = true
[filter "lfs"]
	smudge = git-lfs smudge --skip -- %f
[remote "origin"]
	url = git://mirrors.ustc.edu.cn/aosp/platform/manifest
	fetch = +refs/heads/*:refs/remotes/origin/*
[branch "default"]
	remote = origin
	merge = refs/heads/android-9.0.0_r1
</pre>
要将 Android 源代码树从默认清单中指定的代码库下载到工作目录，请运行以下命令：

    repo sync
Android 源代码文件将位于工作目录中对应的项目名称下。初始同步操作将需要 1 个小时或更长时间才能完成。要详细了解 repo sync 和其他 Repo 命令，请参阅开发部分。    

repo --trace sync -cdf 将repo的所有动作详细输出



# 5. 同步 完后就可以愉快的玩耍了
1. 在ubuntu的命令行 左上角 edit -> profiles -> edit -> scrolling 勾选unlimit 不限制行数

2. cd aosp/.repo/manifests

git branch 查看当前分支

git branch -a 查看所有的远程分支

[源代码标记和版本](https://source.android.com/source/build-numbers.html#source-code-tags-and-builds)

3. 切换分支
    
    1. repo init -u git://mirrors.ustc.edu.cn/aosp/platform/manifest -b android-9.0.0_r1
    
        或者 ： repo init -b android-9.0.0_r1
    2. repo sync 
    
        (not needed if your local copy is up to date)
    
    3. 全部切换到android-9.0.0_r1分支
    
    > repo start android-9.0.0_r1 --all
    
    4. repo branches      
    
    > android-9.0.0_r1          | in all projects
    
    5. cd aosp/.repo/manifests && git branch
    
    > android-9.0.0_r1

4. 使用android studio查看源码

    参考 https://github.com/difcareer/AndroidSourceReader 
    或者自行搜索

选择 Open an existing Android Studio project，找到源码目录，点击 Android.ipr