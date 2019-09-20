# gc分析demo

# 常见gc日志打印启动参数
* -XX:+PrintGCDetails 输出GC的详细日志
* -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
* -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式，如2013-05-04T21:53:59.234+0800）
* -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
* -Xloggc:../logs/gc.log 日志文件的输出路径

