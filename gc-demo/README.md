# Java 1.8 常用GC参数速查表


# GC信息打印

### -verbose:gc 
开启输出JVM GC日志

### -verbose:class

查看类加载信息明细

### -XX:+PrintGCDetails
GC日志打印详细信息

### -XX:+PrintGCDateStamps
GC日志打印时间戳信息

### -XX:+PrintHeapAtGC
在GC前后打印GC日志

### -XX:+PrintGCApplicationStoppedTime
打印应用暂停时间

### -XX:+PrintGCApplicationConcurrentTime
打印每次垃圾回收前，程序未中断的执行时间

### -Xloggc:./gc.log
指定GC日志目录何文件名

### -XX:+HeapDumpOnOutOfMemoryError
当发生 OOM(OutOfMemory)时，自动转储堆内存快照，缺省情况未指定目录时，JVM 会创建一个名称为 java_pidPID.hprof 的堆 dump 文件在 JVM 的工作目录下

### -XX:HeapDumpPath=/data/log/gc/dump/
指定OOM时堆内存转储快照位置

### -XX:+PrintClassHistogramBeforeFullGC、-XX:+PrintClassHistogramAfterFullGC
Full GC前后打印跟踪类视图

### -XX:+PrintTenuringDistribution
打印Young GC各个年龄段的对象分布

### -XX:+PrintTLAB
打印TLAB(线程本地分配缓存区)空间使用情况

# CMS/G1通用内存区域设置

### -Xmx1024M
 JVM最大堆内存大小

### -Xms1024M
 JVM初始内存大小，建议与-Xmx一致

### -Xmn1536M
年轻代空间大小，使用G1收集器是不建议设置该值

### -Xss1M
每个线程的堆栈大小

### -XX:MaxMetaspaceSize=512M
最大元空间大小

### -XX:MetaspaceSize=512M
初始元空间大小

### -XX:SurvivorRatio=8
年轻代中Eden区与Survivor区的大小比值，缺省默认值为8

### -XX:MaxDirectMemorySize=40M
最大堆外内存大小


# CMS/G1通用阈值设置
### -XX:MaxTenuringThreshold=15

设置新生代需要经历多少次GC晋升到老年代中的最大阈值，缺省默认值为15

### -XX:PretenureSizeThreshold=1M
代表分配在新生代一个对象占用内存最大值，超过该最大值对象直接在old区分配，默认值缺省是0，代表对象不管多大都是先在Eden中分配内存


# CMS/G1通用开关设置

### -XX:+DisableExplicitGC
设置忽略System.gc()的调用，不建议设置该参数，因为很多NIO框架，例如Netty基于显示调用System.gc()来触发Full GC来处理虚应用，从而触发清理堆外内存，设置该参数之后会导致堆外内存得不到清理
参考：[为什么不推荐使用-XX:+DisableExplicitGC](https://www.ezlippi.com/blog/2017/10/why-not-expliclitgc.html)

### -XX:+ParallelRefProcEnabled
开启尽可能并行处理Reference对象，建议开启

# CMS/G1通用线程数设置
### -XX:ParallelGCThreads=10
设置并行收集垃圾器在应用线程STW期间时GC处理线程数

### -XX:ConcGCThreads=10
设置垃圾收集器在与应用线程并发执行标记处理(非STW阶段)时的线程数


# CMS常用
### -XX:+UseConcMarkSweepGC
设置使用CMS作为老年代垃圾收集器

### -XX:CMSInitiatingOccupancyFraction=70
设置老年代空间使用的比率阈值多少时触发CMS GC，范围1~100，建议70，需要和-XX:+UseCMSInitiatingOccupancyOnly参数搭配使用才生效

### -XX:+UseCMSInitiatingOccupancyOnly
设置CMS严格按照-XX:CMSInitiatingOccupancyFraction参数设置的阈值来触发CMS GC，如果没有设置，虚拟机会根据收集的数据决定是否触发，建议线上环境带上这个参数，不然会加大问题排查的难度

### -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses
保证显式调用System.gc()触发的是一个并发GC周期而不是Full GC，建议开启

### -XX:+CMSClassUnloadingEnabled
发送CMS GC时触发类卸载，推荐开启

### -XX:+CMSScavengeBeforeRemark
在CMS的重新标记阶段之前执行年轻代Young GC，可减少相当一部分的需要标记的对象，减少CMS重新标记时间的开销，建议开启

### -XX:UseCMSCompactAtFullCollection
是否在CMS发生Full GC之后是否进行空间整理，缺省默认开启(推荐)

### -XX:CMSFullGCsBeforeCompaction
进行多少次Full GC之后进行一次空间整理，缺省默认值为0(推荐)，即每次Full GC过后都进行空间整理，空间整理期间STW

# G1常用
### -XX:+UseG1GC
使用 G1 垃圾收集器

### -XX:MaxGCPauseMillis=200
设置期望达到的最大GC停顿时间指标，JVM会尽力实现，但不保证达到

### -XX:InitiatingHeapOccupancyPercent=45
启动并发GC周期时的堆内存占用百分比。G1之类的垃圾收集器基于整个整个堆的使用率触发并发GC周期,而不只是某年轻代或者老年代的内存的使用比.，值为 0 则表示”一直执行GC循环”。 默认值为 45

### -XX:G1ReservePercent=10
预留内存占堆内存比值，默认值是10，代表使用10%的堆内存为预留内存，当Survivor区域没有足够空间容纳新晋升对象时会尝试使用预留内存

### -XX:G1HeapRegionSize=2M
设置的 G1 Region区域的大小，当G1因为频繁分配巨型对象失败导致Full GC，可以尝试增大该参数
