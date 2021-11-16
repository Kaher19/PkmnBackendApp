package com.kirbey.config;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.kirbey.exception.ConflictException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenBlacklist {

    private String tableName = "blacklist_tokens";
    private String columnName = "token";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean isTokenOnBlacklist(String token){
        List<Map<String, Object>> list = jdbcTemplate.queryForList("SELECT `" +columnName +"` FROM `" +tableName +"` where `token` = '"+ token+"'");
        return !list.isEmpty();
    }

    public void addTokenToBlacklist(String token) throws DataAccessException{
        if (token.startsWith("Bearer ")){ token = token.substring(7); }
        try{
            jdbcTemplate.execute("INSERT INTO `" +tableName +"` (`" +columnName +"`) VALUES ('"+ token+"')");
        } catch(DataAccessException ex) {
            if(ex.getCause() != null && ex.getCause() instanceof SQLException) {
                SQLException se = (SQLException)ex.getCause();
                if(se.getErrorCode() == 1062) {
                    throw new ConflictException("Esta sesión ya había sido cerrada anteriormente.");
                }else{
                    throw ex; // do not swallow unhandled exceptions
                }
            }else{
                throw ex; // do not swallow unhandled exceptions
            }
        }
    }
}