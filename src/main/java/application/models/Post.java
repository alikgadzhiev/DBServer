package application.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private long id;
    private long parent;
    private String author;
    private String message;
    @JsonProperty
    private boolean isEdited;
    private String forum;
    private int thread;
    private String created;

    public void checkNull(Post found){
        if(this.message == null)
            this.message = found.getMessage();
    }
}
