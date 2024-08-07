package application.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String nickname;
    private String fullname;
    private String about;
    private String email;
    public void checkNull(User found){
        if(this.nickname == null)
            this.nickname = found.getNickname();
        if(this.fullname == null)
            this.fullname = found.getFullname();
        if(this.about == null)
            this.about = found.getAbout();
        if(this.email == null)
            this.email = found.getEmail();
    }

    public void checkNull(){
        if(this.nickname == null)
            this.nickname = "";
        if(this.fullname == null)
            this.fullname = "";
        if(this.about == null)
            this.about = "";
        if(this.email == null)
            this.email = "";
    }
}
