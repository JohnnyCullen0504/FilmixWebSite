package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.User;
import com.hhu.filmix.enumeration.UserType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;
@Configuration
public class UserRowMapperFactory {
    @Bean
    public RowMapper<User> userRowMapper(){
        return  (resultSet, rowNum)->{
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("username"));
            user.setEmail(resultSet.getString("email"));
            user.setPhone(resultSet.getString("phone"));
            user.setPassword(resultSet.getString("password"));
            user.setType(UserType.valueOf(resultSet.getString("user_type")));
            return user;
        };
    }
}
