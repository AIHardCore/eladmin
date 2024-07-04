package me.zhengjie.modules.system.domain;


import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setMessage(rs.getString("message"));
        comment.setLikes(rs.getInt("likes"));
        Member member = new Member();
        member.setNickName(rs.getString("nick_name"));
        comment.setMember(member);
        comment.setVersion(rs.getInt("version"));
        comment.setActive(rs.getInt("active"));
        comment.setEnabled(rs.getBoolean("enabled"));
        comment.setCreateTime(rs.getTimestamp("create_time"));
        return comment;
    }
}
