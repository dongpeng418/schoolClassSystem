package cn.com.school.classinfo.config;

import cn.com.school.classinfo.utils.RedisUtil;
import com.google.common.collect.Maps;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Map;

/**
 * Redis相关配置
 *
 * @author dongpp
 * @date 2018/10/29
 */
@Configuration
@EnableCaching
public class RedisConfig {

    /**
     * RedisTemplate，使用Jackson对值进行序列化和反序列化
     * @param connectionFactory factory
     * @return template
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        return template;
    }

    /**
     * 缓存管理配置
     * @param connectionFactory 连接工厂
     * @return 缓存管理
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        RedisSerializationContext.SerializationPair<Object> valuePair = RedisSerializationContext
                .SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
        RedisSerializationContext.SerializationPair<String> keyPair = RedisSerializationContext
                .SerializationPair.fromSerializer(new StringRedisSerializer());
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeKeysWith(keyPair)
                .serializeValuesWith(valuePair)
                .prefixKeysWith(RedisUtil.projectKey())
                .disableCachingNullValues()
                .entryTtl(Duration.ofMinutes(10));
        return RedisCacheManager.builder(redisCacheWriter)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(getCacheConfigMap(defaultCacheConfig))
                .build();
    }

    /**
     * 针对不同的缓存设置不同的缓存配置（主要是设置不同的过期时间）
     * @param defaultCacheConfig 默认配置
     * @return 配置Map, key
     */
    private Map<String, RedisCacheConfiguration> getCacheConfigMap(RedisCacheConfiguration defaultCacheConfig){
        Map<String, RedisCacheConfiguration> configurationMap = Maps.newHashMap();
        for(CacheNameEnum cacheNameEnum : CacheNameEnum.values()){
            configurationMap.put(cacheNameEnum.cacheName,
                    defaultCacheConfig.entryTtl(Duration.ofMinutes(cacheNameEnum.expire)));
        }
        return configurationMap;
    }

    /**
     * 缓存名称及对应的过期时间
     */
    enum CacheNameEnum {

        /**
         * 区域
         */
        REGION("REGION", 24 * 60),

        /**
         * 字典
         */
        DICT("DICT", 24 * 60),

        /**
         * 中转接口
         */
        TRANS_API("TRANS_API", 5);

        /**
         * 缓存名称
         */
        private String cacheName;

        /**
         * 过期时间（分钟）
         */
        private int expire;

        CacheNameEnum(String cacheName, int expire){
            this.cacheName = cacheName;
            this.expire = expire;
        }

    }
}
