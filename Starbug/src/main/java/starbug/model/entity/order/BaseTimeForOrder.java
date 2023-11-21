package starbug.model.entity.order;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners( AuditingEntityListener.class)
@Getter @Setter
public class BaseTimeForOrder {

    @LastModifiedDate
    private LocalDateTime odate;

}
