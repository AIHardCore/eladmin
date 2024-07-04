package me.zhengjie.modules.system.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeParam {
    private CommentLike.CompositeKey key;
    private boolean like;
}
