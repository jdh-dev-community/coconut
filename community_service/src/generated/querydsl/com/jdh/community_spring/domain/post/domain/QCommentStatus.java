package com.jdh.community_spring.domain.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentStatus is a Querydsl query type for CommentStatus
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentStatus extends EntityPathBase<CommentStatus> {

    private static final long serialVersionUID = -701799287L;

    public static final QCommentStatus commentStatus1 = new QCommentStatus("commentStatus1");

    public final com.jdh.community_spring.common.domain.QBaseEntity _super = new com.jdh.community_spring.common.domain.QBaseEntity(this);

    public final StringPath commentStatus = createString("commentStatus");

    public final NumberPath<Long> commentStatusId = createNumber("commentStatusId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCommentStatus(String variable) {
        super(CommentStatus.class, forVariable(variable));
    }

    public QCommentStatus(Path<? extends CommentStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentStatus(PathMetadata metadata) {
        super(CommentStatus.class, metadata);
    }

}

