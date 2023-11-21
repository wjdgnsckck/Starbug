package starbug.controller.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import starbug.model.dto.member.StarBugMemberDto;
import starbug.model.dto.page.PageDto;
import starbug.service.member.MemberService;

import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    // 회원가입시키기
    @PostMapping("/post")
    public boolean memberWrite(@RequestBody StarBugMemberDto starBugMemberDto){
        //System.out.println("컨트롤 starBugMemberDto = " + starBugMemberDto);
        boolean result = memberService.memberWrite( starBugMemberDto);

        return result;
    }

   // 모든 회원 조회
    @GetMapping("/get")
    public PageDto getAll(@RequestParam int page , @RequestParam String key , @RequestParam String keyword , @RequestParam int view ){
        return memberService.getAll( page , key , keyword , view);
        //System.out.println("컨트롤러회원조회 : "+result);


    }

    // 회원 1명 상세 조회
    @GetMapping("/getinfo")
    public StarBugMemberDto getInfo(@RequestParam int mno){
        //System.out.println("컨트롤러 mno : "+mno);
        StarBugMemberDto result = memberService.getInfo(mno);
        return result;
    }

    // 회원 수정
    @PutMapping("/put")
    public boolean memberUpdate(@RequestBody StarBugMemberDto starBugMemberDto){
        boolean result = memberService.memberUpdate(starBugMemberDto);
        //System.out.println("컨트롤러 : "+starBugMemberDto);
        return result;
    }

    // 회원 삭제
    @DeleteMapping("/delete")
    public boolean memberDelete(@RequestParam int mno){
        System.out.println("컨트롤러 : "+mno);
        boolean result = memberService.memberDelete(mno);
        return result;
    }


}
