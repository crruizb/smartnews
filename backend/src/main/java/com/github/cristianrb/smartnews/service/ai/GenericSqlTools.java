package com.github.cristianrb.smartnews.service.ai;

import java.util.List;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class GenericSqlTools {

    private final JdbcTemplate jdbcTemplate;
    private static final int MAX_CALLS = 7;
    private final ThreadLocal<Integer> callCount = ThreadLocal.withInitial(() -> 0);

    public GenericSqlTools(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Tool(description = "Execute a read-only SQL SELECT query over the news and user tables. Input: a complete SELECT. Output: rows as JSON.")
    public List<Map<String, Object>> executeSelect(String query) {
        int count = callCount.get();
        if (count >= MAX_CALLS) {
            throw new IllegalStateException("Tool usage limit exceeded (max " + MAX_CALLS + " per request).");
        }
        callCount.set(count + 1);
        System.out.println(query);
        String q = query.trim();
        String lower = q.toLowerCase();
        if (!lower.startsWith("select")) {
            throw new IllegalArgumentException("Only SELECT allowed");
        }

        if (!lower.contains("limit")) {
            q = q + " LIMIT 50";
        }
        return jdbcTemplate.queryForList(q);
    }
}