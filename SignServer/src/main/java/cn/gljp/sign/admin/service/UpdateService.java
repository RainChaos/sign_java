package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import cn.gljp.sign.admin.mapper.sign.ArticleMapper;
import cn.gljp.sign.admin.mapper.sign.ConfigMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UpdateService {
    @Autowired
    @Qualifier("signDataSource")
    DataSource dataSource;
    @Autowired
    private ConfigMapper configMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private OperatorMapper operatorMapper;
    List<String> sqlList = new ArrayList<>();

    public void initDb() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            for (int i = 0; i < sqlList.size(); i++) {
                ps = conn.prepareStatement(sqlList.get(i));
                ps.executeUpdate();
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                conn.setAutoCommit(true);
                if (ps != null) {
                    ps.close();
                }
                conn.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void executeSql(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
    private void executeQuery(String sql) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = dataSource.getConnection();
            ps = conn.prepareStatement(sql);
            ResultSet resultSet = ps.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }

    //    数据库脚本
    {

    }


    /***
     * 判断表字段是否存在，不存在就可新增、修改、删除，移除修改列名
     * @param tableName 表名
     * @param colName 字段名
     * @param param 字段参数 int(11) NULL DEFAULT NULL
     * @param type 操作类型 1.添加 2.修改 3.删除
     */
    private String createColStr(String tableName, String colName, String param,int type) {
        String sql= "CALL Pro_Temp_ColumnWork ('"+tableName+"','"+colName+"','"+param+"',"+type+")";
        return sql;
    }

    public void initTable() throws SQLException {

    }
}
