package com.minihub.user;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("""
            INSERT INTO users (nickname, email, password_hash, role)
            VALUES (#{nickname}, #{email}, #{passwordHash}, #{role})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);

    @Select("""
            SELECT
                id,
                nickname,
                email,
                password_hash AS passwordHash,
                role,
                article_count AS articleCount,
                created_at AS createdAt,
                updated_at AS updatedAt,
                deleted_at AS deletedAt
            FROM users
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    User findById(Long id);

    @Select("""
            SELECT
                id,
                nickname,
                email,
                password_hash AS passwordHash,
                role,
                article_count AS articleCount,
                created_at AS createdAt,
                updated_at AS updatedAt,
                deleted_at AS deletedAt
            FROM users
            WHERE email = #{email}
              AND deleted_at IS NULL
            """)
    User findByEmail(String email);

    @Select("""
            SELECT
                id,
                nickname,
                email,
                password_hash AS passwordHash,
                role,
                article_count AS articleCount,
                created_at AS createdAt,
                updated_at AS updatedAt,
                deleted_at AS deletedAt
            FROM users
            WHERE deleted_at IS NULL
            ORDER BY id DESC
            LIMIT #{offset}, #{pageSize}
            """)
    List<User> findPage(
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    @Select("""
            SELECT COUNT(*)
            FROM users
            WHERE deleted_at IS NULL
            """)
    long countAll();

    @Update("""
            UPDATE users
            SET
                nickname = #{nickname},
                email = #{email},
                updated_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int update(User user);

    @Update("""
            UPDATE users
            SET deleted_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int softDeleteById(Long id);

    @Update("""
            UPDATE users
            SET
                article_count = article_count + 1,
                updated_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int increaseArticleCount(Long id);

    @Update("""
            UPDATE users
            SET
                article_count = GREATEST(article_count - 1, 0),
                updated_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int decreaseArticleCount(Long id);
}