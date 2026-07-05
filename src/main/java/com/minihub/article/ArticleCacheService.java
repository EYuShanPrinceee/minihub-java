package com.minihub.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;

@Service
public class ArticleCacheService {
    private static final Logger log = LoggerFactory.getLogger(ArticleCacheService.class);

    private static final Duration ARTICLE_DETAIL_TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate stringRedisTemplate;
    private final JsonMapper jsonMapper;

    public ArticleCacheService(
            StringRedisTemplate stringRedisTemplate,
            JsonMapper jsonMapper
    ) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jsonMapper = jsonMapper;
    }

    public ArticleResponse getDetail(Long articleId) {
        String key = detailKey(articleId);

        try {
            String json = stringRedisTemplate.opsForValue().get(key);

            if (json == null || json.isBlank()) {
                return null;
            }

            return jsonMapper.readValue(json, ArticleResponse.class);
        } catch (JacksonException e) {
            log.warn("文章详情缓存反序列化失败：articleId={}", articleId, e);
            deleteDetail(articleId);
            return null;
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            log.warn("Redis 读取失败，降级为查询数据库：articleId={}", articleId);
            return null;
        }
    }

    public void setDetail(Long articleId, ArticleResponse response) {
        String key = detailKey(articleId);

        try {
            String json = jsonMapper.writeValueAsString(response);

            stringRedisTemplate.opsForValue().set(
                    key,
                    json,
                    ARTICLE_DETAIL_TTL
            );
        } catch (JacksonException e) {
            log.warn("文章详情缓存序列化失败：articleId={}", articleId, e);
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            log.warn("Redis 写入失败，跳过写入缓存：articleId={}", articleId);
        }
    }

    public void deleteDetail(Long articleId) {
        String key = detailKey(articleId);

        try {
            stringRedisTemplate.delete(key);
        } catch (RedisConnectionFailureException | RedisSystemException e) {
            log.warn("Redis 删除缓存失败，跳过删除缓存：articleId={}", articleId);
        }
    }

    private String detailKey(Long articleId) {
        return "article:detail:" + articleId;
    }
}