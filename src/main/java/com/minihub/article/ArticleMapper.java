package com.minihub.article;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Insert("""
            INSERT INTO articles (title, content, author_id)
            VALUES (#{title}, #{content}, #{authorId})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Article article);

    @Select("""
            SELECT
                id,
                title,
                content,
                view_count AS viewCount,
                like_count AS likeCount,
                version,
                author_id AS authorId,
                created_at AS createdAt,
                updated_at AS updatedAt,
                deleted_at AS deletedAt
            FROM articles
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    Article findById(Long id);

    @Select("""
            <script>
            SELECT
                id,
                title,
                content,
                view_count AS viewCount,
                like_count AS likeCount,
                version,
                author_id AS authorId,
                created_at AS createdAt,
                updated_at AS updatedAt,
                deleted_at AS deletedAt
            FROM articles
            <where>
                deleted_at IS NULL

                <if test="keyword != null and keyword != ''">
                    AND (
                        LOWER(title) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                        OR LOWER(content) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                    )
                </if>

                <if test="authorId != null">
                    AND author_id = #{authorId}
                </if>

                <if test="title != null and title != ''">
                    AND LOWER(title) LIKE CONCAT('%', LOWER(#{title}), '%')
                </if>
            </where>
            ORDER BY id DESC
            LIMIT #{offset}, #{pageSize}
            </script>
            """)
    List<Article> findPage(
            @Param("keyword") String keyword,
            @Param("authorId") Long authorId,
            @Param("title") String title,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );

    @Select("""
            <script>
            SELECT COUNT(*)
            FROM articles
            <where>
                deleted_at IS NULL

                <if test="keyword != null and keyword != ''">
                    AND (
                        LOWER(title) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                        OR LOWER(content) LIKE CONCAT('%', LOWER(#{keyword}), '%')
                    )
                </if>

                <if test="authorId != null">
                    AND author_id = #{authorId}
                </if>

                <if test="title != null and title != ''">
                    AND LOWER(title) LIKE CONCAT('%', LOWER(#{title}), '%')
                </if>
            </where>
            </script>
            """)
    long count(
            @Param("keyword") String keyword,
            @Param("authorId") Long authorId,
            @Param("title") String title
    );

    @Select("""
            SELECT COUNT(*)
            FROM articles
            WHERE author_id = #{authorId}
              AND deleted_at IS NULL
            """)
    long countByAuthorId(Long authorId);

    @Update("""
            UPDATE articles
            SET
                title = #{title},
                content = #{content},
                version = version + 1,
                updated_at = NOW()
            WHERE id = #{id}
              AND version = #{version}
              AND deleted_at IS NULL
            """)
    int update(Article article);

    @Update("""
            UPDATE articles
            SET deleted_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int softDeleteById(Long id);

    @Update("""
            UPDATE articles
            SET
                view_count = view_count + 1,
                updated_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int increaseViewCount(Long id);

    @Update("""
            UPDATE articles
            SET
                like_count = like_count + 1,
                updated_at = NOW()
            WHERE id = #{id}
              AND deleted_at IS NULL
            """)
    int increaseLikeCount(Long id);
}