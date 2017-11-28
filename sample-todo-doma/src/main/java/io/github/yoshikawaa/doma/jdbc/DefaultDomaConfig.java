package io.github.yoshikawaa.doma.jdbc;

import javax.sql.DataSource;

import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.JdbcLogger;
import org.seasar.doma.jdbc.SqlFileRepository;
import org.seasar.doma.jdbc.dialect.Dialect;
import org.slf4j.event.Level;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

public class DefaultDomaConfig implements Config {

    private DataSource dataSource;
    private Dialect dialect;
    private SqlFileRepository sqlFileRepository;
    private Level jdbcLogLevel;

    @Override
    public DataSource getDataSource() {
        return new TransactionAwareDataSourceProxy(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }

    @Override
    public SqlFileRepository getSqlFileRepository() {
        return sqlFileRepository;
    }

    public void setSqlFileRepository(SqlFileRepository sqlFileRepository) {
        this.sqlFileRepository = sqlFileRepository;
    }

    public void setJdbcLogLevel(Level jdbcLogLevel) {
        this.jdbcLogLevel = jdbcLogLevel;
    }

    @Override
    public JdbcLogger getJdbcLogger() {
        return (jdbcLogLevel == null) ? new Slf4JJdbcLogger() : new Slf4JJdbcLogger(jdbcLogLevel);
    }

}
