package cn.com.school.classinfo.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * 双元素组成的对象
 * 主要用于接口返回结果
 *
 * @author dongpp
 * @date 2018/11/1
 */
@Getter
public class Pair<L, R> {

    private L left;

    private R right;

    private Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @JsonCreator
    public static <L, R> Pair<L, R> of(@JsonProperty("left") final L left,
                                       @JsonProperty("right") final R right) {
        return new Pair<>(left, right);
    }

}
