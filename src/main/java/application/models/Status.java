package application.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Status {
    @JsonProperty("user")
    private int users;
    @JsonProperty("forum")
    private int forums;
    @JsonProperty("thread")
    private int threads;
    @JsonProperty("post")
    private long posts;

    public static long total = 0;

    public Status(int users, int forums, int threads, long posts) {
        this.users = users;
        this.forums = forums;
        this.threads = threads;
        this.posts = posts;
    }

    public long sumOfStatus(){
        return users + forums + threads + posts;
    }
}
