package com.koreait.cloneinstagram.dm;

import com.koreait.cloneinstagram.dm.model.*;
import com.koreait.cloneinstagram.user.model.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DmMapper {
    int insDm(DmDto dto);
    int insDmUser(DmUserEntity entity);
    int insDmMsg(DmMsgDomain domain);

    List<DmDomain> selDmList(DmDto dto);
    UserEntity selDmOpponent(DmDto dto);
    List<DmMsgDomain> selDmMsgList(DmDto dto);

    int updDmLastMsg(DmMsgDomain domain);

}
