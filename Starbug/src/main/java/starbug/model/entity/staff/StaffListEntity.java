package starbug.model.entity.staff;

import lombok.*;
import starbug.model.dto.staff.StaffListDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "staffList")
@AllArgsConstructor@NoArgsConstructor@Getter@Setter@ToString@Builder
public class StaffListEntity { // 급여관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sno; // 급여관리 번호
    @Column(nullable = false , length = 30)
    private String sname; // 이름
    @Column(nullable = false , length = 30)
    private String sposition; // 직급
    @Column(nullable = false , length = 6 )
    private String  sidnum1; // 주민등록번호
    @Column(nullable = false , length = 250 ,unique = true)
    private String  sidnum2; // 주민등록번호
    @Column(nullable = false , length = 100)
    private String  ssex; // 성별
    @Column(nullable = false , length = 10)
    private String smarry; // 기혼,미혼
    @Column(nullable = false , length = 10)
    private String ssorl; // 양력,음력
    @Column(nullable = false , length = 11)
    private String  sphone; // 전화번호
    @Column(nullable = false , length = 50)
    private String semail; // 이메일
    @Column(nullable = false , length = 11)
    private String  sephone; // 집전화
    @Column(nullable = false , length = 100)
    private String saddress; // 주소
    @Column(nullable = false , length = 100)
    private String sdaddress; // 상세 주소
    @Column(nullable = false)
    private String sjoined; // 입사일
    @Column(name="sfile",columnDefinition = "longtext", nullable = true)
    private String sfile;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "staffListEntity", cascade = CascadeType.ALL)
    private List<SalaryEntity> salaryEntities = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "staffListEntity" ,cascade = CascadeType.ALL)
    private List<TimeAndAttendanceEntity> timeAndAttendanceEntities = new ArrayList<>();


    // dto 변환
    public StaffListDto toDto(){

        return StaffListDto.builder()
                .sno(this.sno)
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
    }


} // class
