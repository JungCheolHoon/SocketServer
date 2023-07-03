package socketserver.server.response.dao;

import java.sql.SQLException;
import java.util.Optional;
import socketserver.property.QueryLoader;
import socketserver.server.DBExecution;
import socketserver.server.response.dto.CacheDto;

public class CacheDao {

    private final DBExecution dbExecution;
    private final QueryLoader queryLoader;

    public CacheDao(DBExecution dbExecution) {
        this.dbExecution = dbExecution;
        this.queryLoader = new QueryLoader();
    }

    public String insert(CacheDto cacheDto) {
        try (var preparedStatement = dbExecution.get(
            queryLoader.getQuery("INSERT_CACHE"))) {
            preparedStatement.setString(1, cacheDto.getPath());
            preparedStatement.setBytes(2, cacheDto.getCacheData());
            preparedStatement.executeUpdate();
            dbExecution.commit();
        } catch (SQLException sqle) {
            try {
                dbExecution.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cacheDto.getPath();
    }

    public Optional<CacheDto> select(String path) {
        CacheDto cacheDto = null;
        try (var preparedStatement = dbExecution.get(queryLoader.getQuery("SELECT_CACHE_BY_PATH"))) {
            preparedStatement.setString(1, path);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                cacheDto = CacheDto.fromResultSet(resultSet);
            }
        } catch (SQLException sqle) {
            try {
                dbExecution.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return Optional.ofNullable(cacheDto);
    }
}
