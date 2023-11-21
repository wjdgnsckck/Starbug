package starbug.model.dto.staff;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import starbug.model.entity.staff.StaffListEntity;

@AllArgsConstructor@NoArgsConstructor@Getter@Setter
@ToString@Builder
public class StaffListDto {

    private int sno; // 급여관리 번호
    private String sname; // 이름
    private String sposition; // 직급
    private String  sidnum1; // 주민등록번호
    private String  sidnum2; // 주민등록번호
    private String  ssex; // 성별
    private String smarry; // 기혼,미혼
    private String ssorl; // 양력,음력
    private String  sphone; // 전화번호
    private String semail; // 이메일
    private String  sephone; // 집전화
    private String saddress; // 주소
    private String sdaddress;// 상세주소
    private String sjoined; // 입사일
    private String sfile;    // 직원 사진 파일

    private MultipartFile file;//// 첨부파일 저장 [ 스프링 자체 지원 MultipartFile 라이브러리 ]

    // entity 로 저장위해 변환
    public StaffListEntity toEntity(){

        return StaffListEntity.builder()
                .sname(this.sname)
                .sposition(this.sposition)
                .sidnum1(this.sidnum1)
                .sidnum2(this.sidnum2)
                .ssex(this.ssex)
                .smarry(this.smarry)
                .ssorl(this.ssorl)
                .sphone(this.sphone)
                .semail(this.semail)
                .sephone(this.sephone)
                .saddress(this.saddress)
                .sjoined(this.sjoined)
                .sdaddress(this.sdaddress)
                .sfile(this.sfile)
                .build();
    }// todto
}//class
