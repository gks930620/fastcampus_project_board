package com.fastcampus.projectboard.domain;

import com.fastcampus.projectboard.dto.ArticleCommentDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    private Article article; // 게시글 (ID)

    @Setter
    @JoinColumn(name = "userId")
    @ManyToOne(optional = false)
    private UserAccount userAccount; // 유저 정보 (ID)

    @Setter
    @Column(nullable = true,updatable = false)   //최상위 댓글은 null, 대댓글 쓰면 부모글이 바뀌는 경우X
    private Long parentCommentId;  //부모의  댓글Id.   대댓글 구현해야지


    @ToString.Exclude
    @OrderBy(value ="createdAt") //대댓글은 보통 최신게 아래로.
    @OneToMany(mappedBy ="parentCommentId" , cascade = CascadeType.ALL) //부모지우면 다 지워짐. 근데 요즘엔 삭제된댓글입니다의 댓글로 내용 남아있는 경우 많음
    private Set<ArticleComment> childComments=new LinkedHashSet<>();


    @Setter @Column(nullable = false, length = 500) private String content; // 본문


    protected ArticleComment() {}

    private ArticleComment(Article article, UserAccount userAccount,Long parentCommentId, String content) {
        this.article = article;
        this.userAccount = userAccount;
        this.parentCommentId=parentCommentId;
        this.content = content;
    }

    public static ArticleComment of(Article article, UserAccount userAccount, String content) {
        return new ArticleComment(article, userAccount, null,content);
    }

    public static ArticleComment of(Article article, UserAccount userAccount, Long parentCommentId,String content) {
        return new ArticleComment(article, userAccount, parentCommentId,content);
    }

    public void addChildComment(ArticleComment child){
        child.setParentCommentId(this.getId());
        this.getChildComments().add(child);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

}
