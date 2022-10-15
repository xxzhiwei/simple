package com.aojiaodage.dto;

import com.aojiaodage.validator.interfaces.Del;
import com.aojiaodage.validator.interfaces.Save;
import com.aojiaodage.validator.interfaces.Update;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class UserForm {
    @NotNull(message = "id不能为空", groups = {Update.class, Del.class})
    private Integer id;
    @NotBlank(message = "username不能为空", groups = {Save.class, Update.class})
    private String username;
    @NotBlank(message = "password不能为空", groups = {Save.class})
    private String password;
    private String nickname;
    private String avatar;
    private String email;

    private List<Integer> removing;
    private List<Integer> assigning;
}
