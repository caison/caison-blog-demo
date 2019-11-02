//package org.caison.springboot.sentineldemo;
//
//import com.alibaba.csp.sentinel.Entry;
//import com.alibaba.csp.sentinel.SphU;
//import com.alibaba.csp.sentinel.Tracer;
//import com.alibaba.csp.sentinel.slots.block.BlockException;
//
///**
// * @author ChenCaihua
// * @date 2019年10月14日
// */
//public class SimpleSentinelDemo {
//    public static void main(String[] args) throws BlockException {
//        String resourceName = "resourceName";
//        Entry entry = null;
//        try {
//            entry = SphU.entry(resourceName);
//            System.out.println("resource running");
//        } catch (BlockException e) {
//            // 限流
//            throw e;
//        } catch (Throwable e) {
//            e.printStackTrace();
//            throw e;
//        } finally {
//            if (entry != null) {
//                entry.exit();
//            }
//        }
//    }
//}
