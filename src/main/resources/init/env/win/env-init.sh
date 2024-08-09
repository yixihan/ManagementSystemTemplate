#!/bin/bash

function ensure_directory_exists() {
  local directory="$1"

  if [ -d "$directory" ]; then
    rm -rf "$directory"
    echo "Directory '$directory' has been created. removed now"
  fi
    mkdir -p "$directory"
    chmod 777 "$directory"
    echo "Directory '$directory' has been created."
}

# mkdir dir
baseDir=/d/data
mysqlConfDir="$baseDir"/management/mysql/conf
mysqlDataDir="$baseDir"/management/mysql/data
mysqlLogDir="$baseDir"/management/mysql/logs
redisConfDir="$baseDir"/management/redis/conf
redisLogDir="$baseDir"/management/redis/logs
redisDataDir="$baseDir"/management/redis/data

ensure_directory_exists "$mysqlConfDir"
ensure_directory_exists "$mysqlDataDir"
ensure_directory_exists "$mysqlLogDir"
ensure_directory_exists "$redisConfDir"
ensure_directory_exists "$redisLogDir"
ensure_directory_exists "$redisDataDir"

# write conf
mysqlConf="
[client]
port=3306
default-character-set=utf8mb4

[mysql]
no-auto-rehash
default-character-set=utf8mb4

[mysqld]
log_error=/logs/error.log
symbolic-links=0
pid-file=/var/run/mysqld/mysqld.pid
port=3306
character-set-server=utf8mb4
collation-server=utf8mb4_general_ci
init_connect='SET NAMES utf8mb4'
lower_case_table_names=1
max_connections=400
max_connect_errors=1000
explicit_defaults_for_timestamp=true

# slow log
slow_query_log=1
slow_query_log_file=/logs/slow.log
log_queries_not_using_indexes=1
log_throttle_queries_not_using_indexes=5
long_query_time=8
min_examined_row_limit=100
"

redisConf="
daemonize no
bind 0.0.0.0
port 6379

always-show-logo yes
notify-keyspace-events KEA
pidfile /var/run/redis.pid
timeout 0
databases 16
latency-monitor-threshold 1
slave-serve-stale-data yes
appendonly no
appendfsync everysec
no-appendfsync-on-rewrite no
activerehashing yes

# data dir
rdbcompression yes
dbfilename dump.rdb
dir /data/

# log
loglevel verbose
logfile /logs/logfile.log

# password
requirepass 123456

# RDB config
save 900 1
save 300 10
save 60 10000
"

echo "$mysqlConf" > "$mysqlConfDir/my.cnf"
echo "write mysql conf into $mysqlConfDir/my.cnf"
echo "$redisConf" > "$redisConfDir/redis.conf"
echo "write redis conf into $redisConfDir/redis.conf"