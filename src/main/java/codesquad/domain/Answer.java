package codesquad.domain;

import codesquad.web.HttpSessionUtils;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    private User writer;
    @Lob
    private String contents;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_to_question"))
    private Question question;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    public Answer() {
        this.createDate = LocalDateTime.now();
    }

    public Result update(User loginedUser, String contents) {
        if (!loginedUser.equals(writer)) {
            return Result.fail("You can't update,delete another user's answer");
        }
        this.contents = contents;
        this.updateDate = LocalDateTime.now();
        return Result.ok();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public Long getIdOfQuestion() {
        return question.getId();
    }

    public String getFormattedCreateDate() {
        if (createDate == null) {
            return "";
        }
        if (updateDate != null){
            return updateDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", writer=" + writer +
                ", contents='" + contents + '\'' +
                ", question=" + question +
                '}';
    }
}
