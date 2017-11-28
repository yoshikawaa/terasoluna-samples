package io.github.yoshikawaa.sample.test.helper;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class JdbcTemplateHelper<T> {

    private static final String SQL_TEMPLATE_SELECT = "SELECT * FROM";
    private static final String SQL_TEMPLATE_WHERE = "WHERE";
    private static final String SQL_TEMPLATE_WHERE_VALUE = "=?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<T> rowMapper;
    private final String sqlFindOne;
    private final String sqlFindAll;

    public JdbcTemplateHelper(JdbcTemplate jdbcTemplate, RowMapper<T> rowMapper, String tableName,
            String... primaryKeys) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;

        StringJoiner sql = new StringJoiner(" ");
        sql.add(SQL_TEMPLATE_SELECT).add(tableName);
        this.sqlFindAll = sql.toString();

        sql.add(SQL_TEMPLATE_WHERE);
        for (String primaryKey : primaryKeys) {
            sql.add(primaryKey + SQL_TEMPLATE_WHERE_VALUE);
        }
        this.sqlFindOne = sql.toString();
    }

    public T findOne(Object... args) {
        return jdbcTemplate.queryForObject(sqlFindOne, args, rowMapper);
    }

    public List<T> findAll() {
        return jdbcTemplate.query(sqlFindAll, rowMapper);
    }

}
