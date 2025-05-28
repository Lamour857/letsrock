package org.wj.letsrock.utils.id;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:13
 **/
@Slf4j
public class SnowflakeIdGenerator {
    // 初始时间戳（2023-01-01 00:00:00）
    private final static long INITIAL_TIMESTAMP = 1672531200000L;

    // 各部分的位数
    private final static long DATACENTER_ID_BITS = 5L;
    private final static long WORKER_ID_BITS = 5L;
    private final static long SEQUENCE_BITS = 12L;

    // 最大值
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    // 位移量
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    private final long datacenterId;
    private final long workerId;
    private long lastTimestamp = -1L;
    private final AtomicLong sequence = new AtomicLong(0L);

    /**
     * 构造函数
     * @param datacenterId 数据中心ID (0~31)
     * @param workerId      机器ID (0~31)
     */
    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException("数据中心ID范围: 0~" + MAX_DATACENTER_ID);
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("机器ID范围: 0~" + MAX_WORKER_ID);
        }
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    /**
     * 生成下一个ID
     */
    public synchronized long nextId() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            // 时钟回拨异常处理
            throw new RuntimeException("时钟回拨，拒绝生成ID。回拨时间: " + (lastTimestamp - currentTimestamp) + "ms");
        }

        if (currentTimestamp == lastTimestamp) {
            // 同一毫秒内序列号自增
            long seq = sequence.incrementAndGet() & MAX_SEQUENCE;
            if (seq == 0) {
                // 当前毫秒序列号用尽，等待下一毫秒
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            // 新毫秒重置序列号
            sequence.set(0L);
        }

        lastTimestamp = currentTimestamp;

        // 组合各部分生成ID
        return ((currentTimestamp - INITIAL_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (datacenterId << DATACENTER_ID_SHIFT)
                | (workerId << WORKER_ID_SHIFT)
                | sequence.get();
    }

    /**
     * 等待下一毫秒
     */
    private long waitNextMillis(long lastTimestamp) {
        long current = System.currentTimeMillis();
        while (current <= lastTimestamp) {
            current = System.currentTimeMillis();
        }
        return current;
    }
}
