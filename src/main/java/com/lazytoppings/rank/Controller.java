package com.lazytoppings.rank;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class Controller {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @PostMapping("/{key}")
  public boolean create(@PathVariable String key, @RequestBody UserScore userScore) {
    return redisTemplate.opsForZSet().add(key, userScore.getId(), userScore.getScore());
  }

  @GetMapping("/{key}/{id}/rank")
  public Long rank(@PathVariable String key, @PathVariable String id) {
    return redisTemplate.opsForZSet().reverseRank(key, id);
  }

  @GetMapping("/{key}/{id}/score")
  public Double score(@PathVariable String key, @PathVariable String id) {
    return redisTemplate.opsForZSet().score(key, id);
  }

  @GetMapping("/{key}")
  public Set<ZSetOperations.TypedTuple<String>> list(@PathVariable String key, Long start, Long end) {
    if (start == null) start = 0L;
    if (end == null) end = start + 100;
    return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
  }

  @Getter
  public static class UserScore {
    private String id;
    private int score;
  }
}
