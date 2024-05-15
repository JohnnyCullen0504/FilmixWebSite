package com.hhu.filmix.repository.impl;

import com.hhu.filmix.entity.User;
import com.hhu.filmix.repository.UserRepository;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private JdbcTemplate jdbcTemplate;
    private SnowFlake snowFlake;
    private RowMapper<User> userRowmapper;
    @Override
    public Optional<User> findUserById(Long userId) {
        String sql= """
                SELECT * FROM user WHERE user_id = ?;
                """;
        return jdbcTemplate.query(sql,userRowmapper,userId).stream().findFirst();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        String sql= """
                SELECT * FROM user WHERE email = ?;
                """;
        return jdbcTemplate.query(sql,userRowmapper,email).stream().findFirst();
    }

    @Override
    public Long insertUser(User user) {
        //生成主键
        user.setId(snowFlake.genId());
        String sql = """
                INSERT INTO user(user_id,username,email,phone,password,user_type) VALUES(?,?,?,?,?,?);
                """;
        jdbcTemplate.update(sql,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getType().type());
        //返回用户id
        return user.getId();
    }

    @Override
    public void updateUser(User user) {
        String sql = """
                UPDATE user SET username = ?, email = ?,phone = ? ,password = ?, user_type = ? WHERE user_id = ?;
                """;
        jdbcTemplate.update(
                sql,
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getPassword(),
                user.getType().name(),
                user.getId());

    }

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate,SnowFlake snowFlake,RowMapper<User> userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
        this.userRowmapper = userRowMapper;
    }
}
