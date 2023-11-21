package starbug.model.entity.member;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners( AuditingEntityListener.class)
@Getter
@Setter
public class BaseTimeMember {
    @CreatedDate                     //엔티티가 생성될때 시간이 자동 생성/주입
    private LocalDateTime mdate;
}
