package application.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Forum {
    private String title;
    private String user;
    private String slug;
    private long posts;
    private int threads;

    public void checkNull(Forum forum){
        if(this.title == null)
            this.title = forum.getTitle();
        if(this.user == null)
            this.user = forum.getUser();
        if(this.slug == null)
            this.slug = forum.getSlug();
    }

    public void checkNull(){
        if(this.title == null)
            this.title = "";
        if(this.user == null)
            this.user = "";
        if(this.slug == null)
            this.slug = "";
    }
}
