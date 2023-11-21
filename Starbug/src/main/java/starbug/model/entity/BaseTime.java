package starbug.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


@MappedSuperclass
@EntityListeners( AuditingEntityListener.class ) //
@Setter@Getter
public class BaseTime {
    @CreatedDate                     //엔티티가 생성될때 시간이 자동 생성/주입
    private LocalDateTime cdate;     //레코드/엔티티 생성날짜
    @LastModifiedDate                //엔티티가 변경될때 시간이 자동 생성/주입
    private LocalDateTime udate;     //레코드/엔티티 수정날짜

}
