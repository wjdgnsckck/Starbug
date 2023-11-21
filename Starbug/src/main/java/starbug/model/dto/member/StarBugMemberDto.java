package starbug.model.dto.member;

import lombok.*;
import starbug.model.entity.BaseTime;
import starbug.model.entity.member.StarBugMemberEntity;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @Builder
public class StarBugMemberDto {
        private int mno; // 회원번호
        private String mname; // 회원이름
        private String mphone; // 회원 전화번호
        private int mage; // 회원 나이
        private String msex; // 회원 성별
        private LocalDateTime mdate; // 회원가입 날짜
        private String metc;    // 회원추가정보(메모)
        // + 쿠폰히스토리
        private List<CouponHistoryDto> couponHistoryDtos;

        public StarBugMemberEntity memberToEntity(){
                return StarBugMemberEntity.builder()
                        .mno(this.mno)
                        .mname(this.mname)
                        .mphone(this.mphone)
                        .mage(this.mage)
                        .msex(this.msex)
                        .metc(this.metc)
                        .build();
        }


}
