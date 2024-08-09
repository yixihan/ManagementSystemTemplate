############################################################################################################
###构建命令
############################################################################################################

echo $JAVA_HOME
echo "=============java变量路径:$JAVA_HOME"

# 项目路径, 一般是项目名
PROJECT_LOCATION=managementSystemTemplate
PROJECT=managementSystemTemplate
# 组 ID
GROUP_ID=com.yixihan

############################maven 打包#####################################
# 跳转到源代码目录, 执行 maven 命令
cd /var/jenkins_home/workspace/${PROJECT_LOCATION} && /var/jenkins_home/apache-maven/bin/mvn  clean install -am -pl ${GROUP_ID}:${PROJECT} -Dmaven.test.skip=true
###########################################################################

#############################删除原有镜像,容器###############################
TAG=`date "+%Y%m%d"`
# docker镜像名称
IMAGE_NAME=$PROJECT_LOCATION

echo "=============删除容器&镜像============="
#容器id
cid=$(docker ps -a | grep $IMAGE_NAME | awk '{print $1}')
#镜像id
iid=$(docker images | grep $IMAGE_NAME | awk '{print $3}')

# 删除容器
if [ -n "$cid" ]; then
  echo "存在容器$IMAGE_NAME，cid=$cid,删除容器。。。"
  docker rm -f $cid
else
   echo "不存在$IMAGE_NAME容器"
fi

# 删除镜像
if [ -n "$iid" ]; then
  echo "存在镜像$IMAGE_NAME，iid=$iid,删除容器镜像。。。"
  docker rmi -f $iid
else
   echo "不存在$IMAGE_NAME镜像"
fi

############################################################################

###############################构建镜像,运行容器#############################
# 跳转到源代码目录
cd /var/jenkins_home/workspace/${PROJECT_LOCATION} || exit

# 构建镜像
echo "=============构建镜像$IMAGE_NAME============="
docker build -t $IMAGE_NAME:$TAG .


# 启动容器
echo ""=============启动容器$IMAGE_NAME"============="
# docker 容器启动命令
docker run -p 18080:18080 -d --restart=always --name $IMAGE_NAME $IMAGE_NAME:$TAG
echo "启动服务成功"
###########################################################################