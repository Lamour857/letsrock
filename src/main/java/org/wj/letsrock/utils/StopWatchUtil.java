package org.wj.letsrock.utils;

import org.springframework.lang.NonNull;
import org.springframework.util.StopWatch;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:00
 **/
public class StopWatchUtil extends StopWatch {
    public StopWatchUtil(String id) {
        super(id);
    }

    @Override
    public @NonNull String shortSummary() {
        return "StopWatch '" + getId() + "': running time = " + getTotalTimeMillis() + " ms";
    }

    @Override
    public @NonNull String prettyPrint() {
        StringBuilder sb = new StringBuilder(shortSummary());
        sb.append('\n');
        sb.append("---------------------------------------------\n");
        sb.append("ms         %     Task name\n");
        sb.append("---------------------------------------------\n");
        for (TaskInfo task : getTaskInfo()) {
            sb.append(String.format("%-10d  %-5.1f%%  %s%n",
                    task.getTimeMillis(),
                    task.getTimeSeconds() / getTotalTimeSeconds() * 100,
                    task.getTaskName()));
        }
        return sb.toString();
    }
}
