package application.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Thread{
    private int id;
    private String title;
    private String author;
    private String forum;
    private String message;
    private int votes;
    private String slug;
    private String created;

    public void checkNull(Thread thread){
        if(this.title == null){
            this.title = thread.getTitle();
        }
        if(this.message == null){
            this.message = thread.getMessage();
        }
    }

    public void checkNull(){
        if(this.title == null)
            this.title = "";
        if(this.message == null)
            this.message = "";
    }
}
