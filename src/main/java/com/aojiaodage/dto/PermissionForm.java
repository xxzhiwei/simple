package com.aojiaodage.dto;

import com.aojiaodage.validator.interfaces.Del;
import com.aojiaodage.validator.interfaces.Save;
import com.aojiaodage.validator.interfaces.Update;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PermissionForm {

    @NotNull(message = "id不能为空", groups = {Del.class, Update.class})
    private Integer id;

    @NotNull(message = "type不能为空", groups = {Save.class})
    private Integer type;

    @NotNull(message = "value不能为空", groups = {Save.class, Update.class})
    private String value;

    @NotNull(message = "name不能为空", groups = {Save.class, Update.class})
    private String name;

    private String path;

    private Integer order;

    private String extra;

    private Integer parentId;
}
