package socketserver.server.response.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CacheDto {

    private final String path;
    private final byte[] cacheData;

    public CacheDto(String path, byte[] cacheData) {
        this.path = path;
        this.cacheData = cacheData;
    }

    public String getPath() {
        return path;
    }

    public byte[] getCacheData() {
        return cacheData;
    }

    public static CacheDto fromResultSet(ResultSet resultSet) throws SQLException {
        return new CacheDto(resultSet.getString("path_data"), resultSet.getBytes("cache_data"));
    }
}
