package com.github.cristianrb.smartnews.service.ai;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;

@Component
public class SchemaDescriptionProvider {

    private final String schemaSummary;
    private final Map<String, TableDef> tables = new LinkedHashMap<>();

    public SchemaDescriptionProvider(DataSource dataSource) {
        this.schemaSummary = loadSchema(dataSource);
    }

    private String loadSchema(DataSource ds) {
        try (Connection c = ds.getConnection()) {
            DatabaseMetaData meta = c.getMetaData();
            // Adjust catalog/schema patterns as needed
            try (ResultSet rs = meta.getTables(c.getCatalog(), null, "%", new String[]{"TABLE"})) {
                while (rs.next()) {
                    String table = rs.getString("TABLE_NAME");
                    if (table.startsWith("flyway_schema_history")) continue;
                    TableDef def = new TableDef(table);
                    try (ResultSet cols = meta.getColumns(c.getCatalog(), null, table, "%")) {
                        while (cols.next()) {
                            def.columns.add(cols.getString("COLUMN_NAME") + ":" + cols.getString("TYPE_NAME"));
                        }
                    }
                    try (ResultSet pk = meta.getPrimaryKeys(c.getCatalog(), null, table)) {
                        while (pk.next()) {
                            def.primaryKeys.add(pk.getString("COLUMN_NAME"));
                        }
                    }
                    tables.put(table.toLowerCase(), def);
                }
            }
        } catch (Exception e) {
            return "Schema unavailable: " + e.getMessage();
        }
        return tables.values().stream()
                .map(t -> t.name + "(" +
                        String.join(", ", t.columns) +
                        (t.primaryKeys.isEmpty() ? "" : "; pk=" + String.join(",", t.primaryKeys)) +
                        ")")
                .collect(Collectors.joining("\n"));
    }

    public String getSchemaSummary() {
        return schemaSummary;
    }

    @Tool(description = "Get DB schema overview or a specific table definition. Input: optional table name.")
    public String dbSchema(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            return schemaSummary;
        }
        TableDef def = tables.get(tableName.toLowerCase());
        if (def == null) {
            return "Table not found: " + tableName;
        }
        return def.name + " columns=" + def.columns + " pk=" + def.primaryKeys;
    }

    private static class TableDef {
        final String name;
        final List<String> columns = new ArrayList<>();
        final List<String> primaryKeys = new ArrayList<>();
        TableDef(String name) { this.name = name; }
    }
}